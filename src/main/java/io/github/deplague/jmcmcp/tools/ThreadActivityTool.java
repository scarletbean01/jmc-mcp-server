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
 * MCP tool for analyzing thread lifecycle, creation rates, and sleep hotspots.
 */
public final class ThreadActivityTool {

    private static final String NAME = "thread_activity";

    private final JfrAnalysisService service;

    public ThreadActivityTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze thread lifecycle, creation/destruction rates, and sleep patterns.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
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

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Activity Analysis\n\n");

        // 1. Thread Statistics
        IItemCollection threadStats = events.apply(ItemFilters.type("jdk.JavaThreadStatistics"));
        if (threadStats.hasItems()) {
            sb.append("## Thread Statistics\n");
            IQuantity peak = JfrItemUtils.maxQuantity(threadStats, "peakCount");
            IQuantity maxActive = JfrItemUtils.maxQuantity(threadStats, "activeCount");
            IQuantity minActive = JfrItemUtils.minQuantity(threadStats, "activeCount");
            IQuantity avgActive = JfrItemUtils.avgQuantity(threadStats, "activeCount");
            IQuantity daemon = JfrItemUtils.maxQuantity(threadStats, "daemonCount");
            IQuantity total = JfrItemUtils.maxQuantity(threadStats, "accumulatedCount");

            sb.append("- **Peak Thread Count:** ").append(JfrAnalysisService.display(peak)).append("\n");
            sb.append("- **Active Count (Min/Avg/Max):** ")
                    .append(JfrAnalysisService.display(minActive)).append(" / ")
                    .append(JfrAnalysisService.display(avgActive)).append(" / ")
                    .append(JfrAnalysisService.display(maxActive)).append("\n");
            sb.append("- **Max Daemon Count:** ").append(JfrAnalysisService.display(daemon)).append("\n");
            sb.append("- **Total Threads Created (lifetime):** ").append(JfrAnalysisService.display(total)).append("\n\n");
        }

        // 2. Thread Creation Rate
        IItemCollection starts = events.apply(ItemFilters.type("jdk.ThreadStart"));
        IItemCollection ends = events.apply(ItemFilters.type("jdk.ThreadEnd"));
        long startedCount = JfrItemUtils.count(starts);
        long endedCount = JfrItemUtils.count(ends);

        sb.append("## Thread Lifecycle\n");
        sb.append("- **Threads Started:** ").append(startedCount).append("\n");
        sb.append("- **Threads Ended:** ").append(endedCount).append("\n");
        sb.append("- **Net Change:** ").append(startedCount - endedCount).append("\n\n");

        if (startedCount > 0) {
            sb.append("### Top Thread Creation Sites\n");
            sb.append("| Count | Creation Call Site (top 5 frames) |\n");
            sb.append("|-------|----------------------------------|\n");
            Map<String, Long> startSites = new HashMap<>();
            for (IItemIterable iterable : starts) {
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                if (stackAccessor != null) {
                    for (IItem item : iterable) {
                        Object stack = stackAccessor.getMember(item);
                        String trace = JfrItemUtils.formatStackTrace(stack, 5);
                        startSites.merge(trace, 1L, Long::sum);
                    }
                }
            }
            startSites.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(topN)
                    .forEach(e -> sb.append("| ").append(e.getValue()).append(" | `").append(e.getKey().replace("\n", "`<br>`")).append("` |\n"));
            sb.append("\n");
        }

        // 3. Thread Sleep Hotspots
        IItemCollection sleeps = events.apply(ItemFilters.type("jdk.ThreadSleep"));
        if (sleeps.hasItems()) {
            sb.append("## Thread Sleep Hotspots\n");
            sb.append("| Total Sleep Time | Count | Call Site (top 5 frames) |\n");
            sb.append("|------------------|-------|--------------------------|\n");

            Map<String, SleepStats> sleepMap = new HashMap<>();
            for (IItemIterable iterable : sleeps) {
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                if (durationAccessor != null && stackAccessor != null) {
                    for (IItem item : iterable) {
                        IQuantity duration = durationAccessor.getMember(item);
                        Object stack = stackAccessor.getMember(item);
                        if (duration != null && stack != null) {
                            String trace = JfrItemUtils.formatStackTrace(stack, 5);
                            SleepStats stats = sleepMap.computeIfAbsent(trace, k -> new SleepStats());
                            stats.count++;
                            stats.totalNanos += duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                        }
                    }
                }
            }

            sleepMap.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                    .limit(topN)
                    .forEach(e -> {
                        SleepStats s = e.getValue();
                        sb.append("| ").append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.totalNanos))).append(" | ")
                                .append(s.count).append(" | ")
                                .append("`").append(e.getKey().replace("\n", "`<br>`")).append("` |\n");
                    });
            sb.append("\n");
        }

        return sb.toString();
    }

    private static class SleepStats {
        long count;
        long totalNanos;
    }
}
