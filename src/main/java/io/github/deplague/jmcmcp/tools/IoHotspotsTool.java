package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP tool for identifying I/O latency hotspots (file and socket).
 */
public final class IoHotspotsTool {

    private static final String NAME = "io_hotspots";

    private final JfrAnalysisService service;

    public IoHotspotsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Identify slow and frequent I/O operations by path/host with call-site breakdowns.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "io_type", SchemaUtil.stringProp("I/O type filter", List.of("file", "socket", "all")),
                                        "top_n", SchemaUtil.intProp("Number of top results (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String ioType = SchemaUtil.getStringOrDefault(request.arguments(), "io_type", "all");
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr, ioType, topN);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr, String ioType, int topN) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        StringBuilder sb = new StringBuilder();
        sb.append("# I/O Hotspots Analysis\n\n");

        boolean showFile = "all".equals(ioType) || "file".equals(ioType);
        boolean showSocket = "all".equals(ioType) || "socket".equals(ioType);

        if (showFile) {
            appendIoSection(sb, "File", events, List.of("jdk.FileRead", "jdk.FileWrite"), "path", "bytesRead", "bytesWritten", topN);
        }

        if (showSocket) {
            appendIoSection(sb, "Socket", events, List.of("jdk.SocketRead", "jdk.SocketWrite"), "host", "bytesRead", "bytesWritten", topN);
        }

        appendPercentiles(sb, events);

        return sb.toString();
    }

    private void appendIoSection(StringBuilder sb, String title, IItemCollection events, List<String> types, String targetAttr, String readAttr, String writeAttr, int topN) {
        sb.append("## ").append(title).append(" I/O Hotspots\n\n");
        Map<IoKey, IoStats> statsMap = new HashMap<>();

        for (String typeId : types) {
            IItemCollection typeEvents = events.apply(ItemFilters.type(typeId));
            boolean isRead = typeId.contains("Read");
            String bytesAttr = isRead ? readAttr : writeAttr;

            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<Object, IItem> targetAccessor = JfrItemUtils.getAccessor(iterable.getType(), targetAttr);
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
                IMemberAccessor<IQuantity, IItem> bytesAccessor = JfrItemUtils.getAccessor(iterable.getType(), bytesAttr);
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

                if (targetAccessor != null && durationAccessor != null) {
                    for (IItem item : iterable) {
                        Object targetObj = targetAccessor.getMember(item);
                        if (targetObj == null) continue;

                        String target = targetObj.toString();
                        if (title.equals("Socket")) {
                            Object port = JfrItemUtils.getMember(item, "port").orElse("");
                            target = target + ":" + port;
                        }

                        IQuantity duration = durationAccessor.getMember(item);
                        IQuantity bytes = bytesAccessor != null ? bytesAccessor.getMember(item) : null;
                        Object stackObj = stackAccessor != null ? stackAccessor.getMember(item) : null;
                        String trace = JfrItemUtils.formatStackTrace(stackObj, 5);

                        IoKey key = new IoKey(target, trace);
                        IoStats stats = statsMap.computeIfAbsent(key, k -> new IoStats());
                        stats.count++;
                        if (duration != null) {
                            stats.totalDurationNanos += duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                            if (duration.clampedLongValueIn(UnitLookup.NANOSECOND) > stats.maxDurationNanos) {
                                stats.maxDurationNanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                            }
                        }
                        if (bytes != null) {
                            stats.totalBytes += bytes.clampedLongValueIn(UnitLookup.BYTE);
                        }
                    }
                }
            }
        }

        if (statsMap.isEmpty()) {
            sb.append("No ").append(title.toLowerCase()).append(" I/O events found.\n\n");
            return;
        }

        sb.append("| Duration (Max) | Count | Bytes | Target | Call Site (top 5 frames) |\n");
        sb.append("|----------------|-------|-------|--------|--------------------------|\n");

        statsMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().maxDurationNanos, a.getValue().maxDurationNanos))
                .limit(topN)
                .forEach(entry -> {
                    IoStats s = entry.getValue();
                    sb.append("| ").append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.maxDurationNanos))).append(" | ")
                            .append(s.count).append(" | ")
                            .append(SchemaUtil.formatBytes(s.totalBytes)).append(" | ")
                            .append("`").append(entry.getKey().target).append("` | ")
                            .append("`").append(entry.getKey().stackTrace.replace("\n", "`<br>`")).append("` |\n");
                });
        sb.append("\n");
    }

    private void appendPercentiles(StringBuilder sb, IItemCollection events) {
        sb.append("## I/O Latency Percentiles\n\n");
        sb.append("| Operation | P50 | P95 | P99 | Max |\n");
        sb.append("|-----------|-----|-----|-----|-----|\n");

        appendPercentileRow(sb, "File Read", events, "jdk.FileRead");
        appendPercentileRow(sb, "File Write", events, "jdk.FileWrite");
        appendPercentileRow(sb, "Socket Read", events, "jdk.SocketRead");
        appendPercentileRow(sb, "Socket Write", events, "jdk.SocketWrite");
        sb.append("\n");
    }

    private void appendPercentileRow(StringBuilder sb, String name, IItemCollection events, String typeId) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        if (!filtered.hasItems()) return;

        IQuantity p50 = JfrItemUtils.percentileQuantity(filtered, JfrAttributes.DURATION.getIdentifier(), 50);
        IQuantity p95 = JfrItemUtils.percentileQuantity(filtered, JfrAttributes.DURATION.getIdentifier(), 95);
        IQuantity p99 = JfrItemUtils.percentileQuantity(filtered, JfrAttributes.DURATION.getIdentifier(), 99);
        IQuantity max = JfrItemUtils.maxQuantity(filtered, JfrAttributes.DURATION.getIdentifier());

        sb.append("| ").append(name).append(" | ")
                .append(JfrAnalysisService.display(p50)).append(" | ")
                .append(JfrAnalysisService.display(p95)).append(" | ")
                .append(JfrAnalysisService.display(p99)).append(" | ")
                .append(JfrAnalysisService.display(max)).append(" |\n");
    }

    private record IoKey(String target, String stackTrace) {}
    private static class IoStats {
        long count;
        long totalDurationNanos;
        long maxDurationNanos;
        long totalBytes;
    }
}
