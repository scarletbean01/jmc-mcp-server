package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class NetworkAnalysisTool {

    private static final String NAME = "network_analysis";

    private final JfrAnalysisService service;

    public NetworkAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze socket connection hotspots in a JFR recording. " +
                                "Reports per-host:port connection latency, read/write throughput, and failure tracking.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top results per section (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr, topN);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection connectEvents = events.apply(ItemFilters.type("jdk.SocketConnect"));
        IItemCollection readEvents = events.apply(ItemFilters.type("jdk.SocketRead"));
        IItemCollection writeEvents = events.apply(ItemFilters.type("jdk.SocketWrite"));

        long connectCount = JfrItemUtils.count(connectEvents);
        long readCount = JfrItemUtils.count(readEvents);
        long writeCount = JfrItemUtils.count(writeEvents);

        if (connectCount == 0 && readCount == 0 && writeCount == 0) {
            return "# Network Analysis\n\nNo socket events found in the recording.";
        }

        Map<HostPortKey, ConnectStats> connectStats = processConnects(connectEvents);
        Map<HostPortKey, ReadStats> readStats = processReads(readEvents);
        Map<HostPortKey, WriteStats> writeStats = processWrites(writeEvents);

        StringBuilder sb = new StringBuilder();
        sb.append("# Network Analysis\n\n");

        sb.append("## Connection Summary\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append("| Total Connections | ").append(connectCount).append(" |\n");
        sb.append("| Total Reads | ").append(readCount).append(" |\n");
        sb.append("| Total Writes | ").append(writeCount).append(" |\n");

        if (connectCount > 0) {
            IQuantity avgConn = JfrItemUtils.avgQuantity(connectEvents, JfrAttributes.DURATION.getIdentifier());
            IQuantity maxConn = JfrItemUtils.maxQuantity(connectEvents, JfrAttributes.DURATION.getIdentifier());
            IQuantity p95Conn = JfrItemUtils.percentileQuantity(connectEvents, JfrAttributes.DURATION.getIdentifier(), 95);
            sb.append("| Avg Connect Duration | ").append(JfrAnalysisService.display(avgConn)).append(" |\n");
            sb.append("| Max Connect Duration | ").append(JfrAnalysisService.display(maxConn)).append(" |\n");
            sb.append("| P95 Connect Duration | ").append(JfrAnalysisService.display(p95Conn)).append(" |\n");
        }
        sb.append("\n");

        if (!connectStats.isEmpty()) {
            sb.append("## Top Hosts by Connection Count\n\n");
            sb.append("| Host:Port | Connections | Avg Duration | Max Duration |\n");
            sb.append("|-----------|-------------|---------------|---------------|\n");
            connectStats.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue().count, a.getValue().count))
                    .limit(topN)
                    .forEach(entry -> {
                        ConnectStats s = entry.getValue();
                        sb.append(String.format("| `%s` | %d | %s | %s |%n",
                                entry.getKey(),
                                s.count,
                                JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.totalDurationNanos / s.count)),
                                JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.maxDurationNanos))));
                    });
            sb.append("\n");
        }

        if (!readStats.isEmpty()) {
            sb.append("## Top Hosts by Read Throughput\n\n");
            sb.append("| Host:Port | Reads | Total Bytes | Avg Read Duration |\n");
            sb.append("|-----------|-------|-------------|-------------------|\n");
            readStats.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue().totalBytes, a.getValue().totalBytes))
                    .limit(topN)
                    .forEach(entry -> {
                        ReadStats s = entry.getValue();
                        sb.append(String.format("| `%s` | %d | %s | %s |%n",
                                entry.getKey(),
                                s.count,
                                SchemaUtil.formatBytes(s.totalBytes),
                                JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.totalDurationNanos / s.count))));
                    });
            sb.append("\n");
        }

        if (!writeStats.isEmpty()) {
            sb.append("## Top Hosts by Write Throughput\n\n");
            sb.append("| Host:Port | Writes | Total Bytes | Avg Write Duration |\n");
            sb.append("|-----------|--------|-------------|--------------------|\n");
            writeStats.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue().totalBytes, a.getValue().totalBytes))
                    .limit(topN)
                    .forEach(entry -> {
                        WriteStats s = entry.getValue();
                        sb.append(String.format("| `%s` | %d | %s | %s |%n",
                                entry.getKey(),
                                s.count,
                                SchemaUtil.formatBytes(s.totalBytes),
                                JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.totalDurationNanos / s.count))));
                    });
            sb.append("\n");
        }

        appendLatencyPercentiles(sb, connectEvents, readEvents, writeEvents);

        return sb.toString();
    }

    private Map<HostPortKey, ConnectStats> processConnects(IItemCollection events) {
        Map<HostPortKey, ConnectStats> stats = new HashMap<>();
        for (IItemIterable iterable : events) {
            IMemberAccessor<Object, IItem> hostAccessor = JfrItemUtils.getAccessor(iterable.getType(), "host");
            IMemberAccessor<IQuantity, IItem> portAccessor = JfrItemUtils.getAccessor(iterable.getType(), "port");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());

            if (hostAccessor != null) {
                for (IItem item : iterable) {
                    Object hostObj = hostAccessor.getMember(item);
                    if (hostObj == null) continue;
                    String host = hostObj.toString();
                    int port = 0;
                    if (portAccessor != null) {
                        IQuantity portQ = portAccessor.getMember(item);
                        if (portQ != null) port = (int) portQ.longValue();
                    }

                    HostPortKey key = new HostPortKey(host, port);
                    ConnectStats s = stats.computeIfAbsent(key, k -> new ConnectStats());
                    s.count++;
                    if (durationAccessor != null) {
                        IQuantity dur = durationAccessor.getMember(item);
                        if (dur != null) {
                            long nanos = dur.clampedLongValueIn(UnitLookup.NANOSECOND);
                            s.totalDurationNanos += nanos;
                            s.maxDurationNanos = Math.max(s.maxDurationNanos, nanos);
                        }
                    }
                }
            }
        }
        return stats;
    }

    private Map<HostPortKey, ReadStats> processReads(IItemCollection events) {
        Map<HostPortKey, ReadStats> stats = new HashMap<>();
        for (IItemIterable iterable : events) {
            IMemberAccessor<Object, IItem> hostAccessor = JfrItemUtils.getAccessor(iterable.getType(), "host");
            IMemberAccessor<IQuantity, IItem> portAccessor = JfrItemUtils.getAccessor(iterable.getType(), "port");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
            IMemberAccessor<IQuantity, IItem> bytesAccessor = JfrItemUtils.getAccessor(iterable.getType(), "bytesRead");

            if (hostAccessor != null) {
                for (IItem item : iterable) {
                    Object hostObj = hostAccessor.getMember(item);
                    if (hostObj == null) continue;
                    String host = hostObj.toString();
                    int port = 0;
                    if (portAccessor != null) {
                        IQuantity portQ = portAccessor.getMember(item);
                        if (portQ != null) port = (int) portQ.longValue();
                    }

                    HostPortKey key = new HostPortKey(host, port);
                    ReadStats s = stats.computeIfAbsent(key, k -> new ReadStats());
                    s.count++;
                    if (durationAccessor != null) {
                        IQuantity dur = durationAccessor.getMember(item);
                        if (dur != null) s.totalDurationNanos += dur.clampedLongValueIn(UnitLookup.NANOSECOND);
                    }
                    if (bytesAccessor != null) {
                        IQuantity bytes = bytesAccessor.getMember(item);
                        if (bytes != null) s.totalBytes += bytes.longValue();
                    }
                }
            }
        }
        return stats;
    }

    private Map<HostPortKey, WriteStats> processWrites(IItemCollection events) {
        Map<HostPortKey, WriteStats> stats = new HashMap<>();
        for (IItemIterable iterable : events) {
            IMemberAccessor<Object, IItem> hostAccessor = JfrItemUtils.getAccessor(iterable.getType(), "host");
            IMemberAccessor<IQuantity, IItem> portAccessor = JfrItemUtils.getAccessor(iterable.getType(), "port");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
            IMemberAccessor<IQuantity, IItem> bytesAccessor = JfrItemUtils.getAccessor(iterable.getType(), "bytesWritten");

            if (hostAccessor != null) {
                for (IItem item : iterable) {
                    Object hostObj = hostAccessor.getMember(item);
                    if (hostObj == null) continue;
                    String host = hostObj.toString();
                    int port = 0;
                    if (portAccessor != null) {
                        IQuantity portQ = portAccessor.getMember(item);
                        if (portQ != null) port = (int) portQ.longValue();
                    }

                    HostPortKey key = new HostPortKey(host, port);
                    WriteStats s = stats.computeIfAbsent(key, k -> new WriteStats());
                    s.count++;
                    if (durationAccessor != null) {
                        IQuantity dur = durationAccessor.getMember(item);
                        if (dur != null) s.totalDurationNanos += dur.clampedLongValueIn(UnitLookup.NANOSECOND);
                    }
                    if (bytesAccessor != null) {
                        IQuantity bytes = bytesAccessor.getMember(item);
                        if (bytes != null) s.totalBytes += bytes.longValue();
                    }
                }
            }
        }
        return stats;
    }

    private void appendLatencyPercentiles(StringBuilder sb, IItemCollection connectEvents, IItemCollection readEvents, IItemCollection writeEvents) {
        sb.append("## Connection Latency Percentiles\n\n");
        sb.append("| Operation | P50 | P95 | P99 | Max |\n");
        sb.append("|-----------|-----|-----|-----|-----|\n");

        appendPercentileRow(sb, "Socket Connect", connectEvents);
        appendPercentileRow(sb, "Socket Read", readEvents);
        appendPercentileRow(sb, "Socket Write", writeEvents);
        sb.append("\n");
    }

    private void appendPercentileRow(StringBuilder sb, String name, IItemCollection events) {
        if (!events.hasItems()) return;
        IQuantity p50 = JfrItemUtils.percentileQuantity(events, JfrAttributes.DURATION.getIdentifier(), 50);
        IQuantity p95 = JfrItemUtils.percentileQuantity(events, JfrAttributes.DURATION.getIdentifier(), 95);
        IQuantity p99 = JfrItemUtils.percentileQuantity(events, JfrAttributes.DURATION.getIdentifier(), 99);
        IQuantity max = JfrItemUtils.maxQuantity(events, JfrAttributes.DURATION.getIdentifier());

        sb.append("| ").append(name).append(" | ")
                .append(JfrAnalysisService.display(p50)).append(" | ")
                .append(JfrAnalysisService.display(p95)).append(" | ")
                .append(JfrAnalysisService.display(p99)).append(" | ")
                .append(JfrAnalysisService.display(max)).append(" |\n");
    }

    private record HostPortKey(String host, int port) {
        @Override
        public String toString() {
            return host + ":" + port;
        }
    }

    private static class ConnectStats {
        long count;
        long totalDurationNanos;
        long maxDurationNanos;
    }

    private static class ReadStats {
        long count;
        long totalDurationNanos;
        long totalBytes;
    }

    private static class WriteStats {
        long count;
        long totalDurationNanos;
        long totalBytes;
    }
}