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

/**
 * MCP tool for aggregating all blocking and waiting events in a JFR recording.
 */
public final class BlockingSummaryTool {

    private static final String NAME = "blocking_summary";

    private final JfrAnalysisService service;

    public BlockingSummaryTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Aggregate all blocking events (monitors, parking, sleeping, I/O) per thread.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top threads/reasons to return (default 10)", 10)
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

        Map<String, String> eventToCategory = Map.of(
                "jdk.JavaMonitorEnter", "MONITOR_ENTER",
                "jdk.JavaMonitorWait", "MONITOR_WAIT",
                "jdk.ThreadPark", "PARK",
                "jdk.ThreadSleep", "SLEEP",
                "jdk.SocketRead", "SOCKET_IO",
                "jdk.SocketWrite", "SOCKET_IO",
                "jdk.FileRead", "FILE_IO",
                "jdk.FileWrite", "FILE_IO"
        );

        Map<String, ThreadBlockingStats> threadStatsMap = new HashMap<>();
        Map<String, CategoryStats> categoryStatsMap = new HashMap<>();
        Map<BlockSite, SiteStats> siteStatsMap = new HashMap<>();

        long totalBlockedEvents = 0;
        long totalBlockedNanos = 0;

        for (Map.Entry<String, String> entry : eventToCategory.entrySet()) {
            String eventType = entry.getKey();
            String category = entry.getValue();

            IItemCollection filteredEvents = events.apply(ItemFilters.type(eventType));
            for (IItemIterable iterable : filteredEvents) {
                IMemberAccessor<Object, IItem> threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                
                // Specific accessors for detail
                IMemberAccessor<Object, IItem> monitorClassAccessor = JfrItemUtils.getAccessor(iterable.getType(), "monitorClass");
                IMemberAccessor<String, IItem> filePathAccessor = JfrItemUtils.getAccessor(iterable.getType(), "path");
                IMemberAccessor<String, IItem> hostAccessor = JfrItemUtils.getAccessor(iterable.getType(), "host");

                if (durationAccessor != null) {
                    for (IItem item : iterable) {
                        IQuantity duration = durationAccessor.getMember(item);
                        if (duration == null) continue;

                        long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                        totalBlockedNanos += nanos;
                        totalBlockedEvents++;

                        Object threadObj = threadAccessor != null ? threadAccessor.getMember(item) : null;
                        String threadName = threadObj != null ? threadObj.toString() : "Unknown";

                        ThreadBlockingStats ts = threadStatsMap.computeIfAbsent(threadName, k -> new ThreadBlockingStats(k));
                        ts.add(category, nanos);

                        CategoryStats cs = categoryStatsMap.computeIfAbsent(category, k -> new CategoryStats(k));
                        cs.add(nanos);

                        String detail = "Unknown";
                        if (monitorClassAccessor != null) {
                            Object mc = monitorClassAccessor.getMember(item);
                            if (mc != null) detail = mc.toString();
                        } else if (filePathAccessor != null) {
                            detail = filePathAccessor.getMember(item);
                        } else if (hostAccessor != null) {
                            detail = hostAccessor.getMember(item);
                        } else if (stackAccessor != null) {
                            Object st = stackAccessor.getMember(item);
                            if (st != null) detail = JfrItemUtils.formatStackTrace(st, 1).trim();
                        }
                        
                        BlockSite site = new BlockSite(category, detail);
                        SiteStats ss = siteStatsMap.computeIfAbsent(site, k -> new SiteStats());
                        ss.add(nanos);
                    }
                }
            }
        }

        if (totalBlockedEvents == 0) {
            return "# Blocking Summary\n\nNo blocking events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Blocking Summary\n\n");

        sb.append("## Blocking Overview\n\n");
        sb.append("- **Total Blocked Time:** ").append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(totalBlockedNanos))).append("\n");
        sb.append("- **Total Blocked Events:** ").append(totalBlockedEvents).append("\n\n");

        sb.append("## Per-Thread Blocking Summary\n\n");
        sb.append("| Thread Name | Total Blocked Time | Event Count | Top Category |\n");
        sb.append("|-------------|--------------------|-------------|--------------|\n");

        threadStatsMap.values().stream()
                .sorted((a, b) -> Long.compare(b.totalNanos, a.totalNanos))
                .limit(topN)
                .forEach(ts -> {
                    String topCategory = ts.categoryNanos.entrySet().stream()
                            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                            .map(Map.Entry::getKey)
                            .findFirst().orElse("N/A");
                    sb.append("| ").append(ts.name).append(" | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(ts.totalNanos))).append(" | ")
                            .append(ts.count).append(" | ")
                            .append(topCategory).append(" |\n");
                });

        sb.append("\n## Top Blocking Reasons\n\n");
        sb.append("| Category | Detail | Total Time | Count |\n");
        sb.append("|----------|--------|------------|-------|\n");
        siteStatsMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .limit(topN)
                .forEach(e -> {
                    sb.append("| ").append(e.getKey().category).append(" | `")
                            .append(e.getKey().detail).append("` | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(e.getValue().totalNanos))).append(" | ")
                            .append(e.getValue().count).append(" |\n");
                });

        sb.append("\n## Blocking Time Distribution\n\n");
        sb.append("| Category | Total Time | Event Count | Avg Duration |\n");
        sb.append("|----------|------------|-------------|--------------|\n");
        categoryStatsMap.values().stream()
                .sorted((a, b) -> Long.compare(b.totalNanos, a.totalNanos))
                .forEach(cs -> {
                    sb.append("| ").append(cs.category).append(" | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(cs.totalNanos))).append(" | ")
                            .append(cs.count).append(" | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(cs.totalNanos / cs.count))).append(" |\n");
                });

        return sb.toString();
    }

    private record BlockSite(String category, String detail) {}

    private static class ThreadBlockingStats {
        final String name;
        long totalNanos = 0;
        long count = 0;
        final Map<String, Long> categoryNanos = new HashMap<>();

        ThreadBlockingStats(String name) {
            this.name = name;
        }

        void add(String category, long nanos) {
            totalNanos += nanos;
            count++;
            categoryNanos.merge(category, nanos, Long::sum);
        }
    }

    private static class CategoryStats {
        final String category;
        long totalNanos = 0;
        long count = 0;

        CategoryStats(String category) {
            this.category = category;
        }

        void add(long nanos) {
            totalNanos += nanos;
            count++;
        }
    }

    private static class SiteStats {
        long totalNanos = 0;
        long count = 0;

        void add(long nanos) {
            totalNanos += nanos;
            count++;
        }
    }
}
