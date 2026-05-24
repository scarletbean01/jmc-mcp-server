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
import java.util.Map;

/**
 * MCP tool for analyzing safepoint events and STW pauses.
 */
public final class SafepointAnalysisTool {

    private static final String NAME = "safepoint_analysis";

    private final JfrAnalysisService service;

    public SafepointAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze safepoint events and stop-the-world pauses outside of GC.")
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
        sb.append("# Safepoint & STW Pause Analysis\n\n");

        IItemCollection safepoints = events.apply(ItemFilters.type("jdk.SafepointBegin"));
        
        if (!safepoints.hasItems()) {
            sb.append("No safepoint events found in the recording.\n");
            return sb.toString();
        }

        // Summary
        long count = JfrItemUtils.count(safepoints);
        IQuantity totalTime = JfrItemUtils.sumQuantity(safepoints, JfrAttributes.DURATION.getIdentifier());
        IQuantity avgTime = JfrItemUtils.avgQuantity(safepoints, JfrAttributes.DURATION.getIdentifier());
        IQuantity maxTime = JfrItemUtils.maxQuantity(safepoints, JfrAttributes.DURATION.getIdentifier());
        IQuantity p95Time = JfrItemUtils.percentileQuantity(safepoints, JfrAttributes.DURATION.getIdentifier(), 95);

        sb.append("## Safepoint Summary\n");
        sb.append("- **Total Safepoints:** ").append(count).append("\n");
        sb.append("- **Total STW Time:** ").append(JfrAnalysisService.display(totalTime)).append("\n");
        sb.append("- **Average Duration:** ").append(JfrAnalysisService.display(avgTime)).append("\n");
        sb.append("- **Max Duration:** ").append(JfrAnalysisService.display(maxTime)).append("\n");
        sb.append("- **P95 Duration:** ").append(JfrAnalysisService.display(p95Time)).append("\n\n");

        // Cause Distribution
        sb.append("## Safepoint Cause Distribution\n");
        sb.append("| Cause | Count | Total Duration | Avg Duration | Max Duration |\n");
        sb.append("|-------|-------|----------------|--------------|--------------|\n");

        Map<String, CauseStats> causeMap = new HashMap<>();
        for (IItemIterable iterable : safepoints) {
            IMemberAccessor<String, IItem> opAccessor = JfrItemUtils.getAccessor(iterable.getType(), "operation");
            if (opAccessor == null) opAccessor = JfrItemUtils.getAccessor(iterable.getType(), "name");

            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());

            if (opAccessor != null && durationAccessor != null) {
                for (IItem item : iterable) {
                    String op = opAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (op != null && duration != null) {
                        CauseStats stats = causeMap.computeIfAbsent(op, k -> new CauseStats());
                        stats.count++;
                        long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                        stats.totalNanos += nanos;
                        if (nanos > stats.maxNanos) stats.maxNanos = nanos;
                    }
                }
            }
        }

        causeMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .forEach(entry -> {
                    CauseStats s = entry.getValue();
                    sb.append("| ").append(entry.getKey()).append(" | ")
                            .append(s.count).append(" | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.totalNanos))).append(" | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.totalNanos / s.count))).append(" | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.maxNanos))).append(" |\n");
                });
        sb.append("\n");

        // Top-N Longest Safepoints
        sb.append("## Top Longest Safepoints\n");
        sb.append("| Duration | Cause | Start Time |\n");
        sb.append("|----------|-------|------------|\n");

        java.util.List<IItem> sortedSafepoints = new java.util.ArrayList<>();
        for (IItemIterable iterable : safepoints) {
            for (IItem item : iterable) {
                sortedSafepoints.add(item);
            }
        }
        sortedSafepoints.stream()
                .sorted((a, b) -> {
                    IQuantity da = JfrItemUtils.getQuantity(a, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                    IQuantity db = JfrItemUtils.getQuantity(b, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                    if (da == null) return (db == null) ? 0 : 1;
                    if (db == null) return -1;
                    return db.compareTo(da);
                })
                .limit(topN)
                .forEach(item -> {
                    IQuantity duration = JfrItemUtils.getQuantity(item, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                    Object op = JfrItemUtils.getMember(item, "operation").orElse(JfrItemUtils.getMember(item, "name").orElse("Unknown"));
                    IQuantity start = JfrItemUtils.getQuantity(item, JfrAttributes.START_TIME.getIdentifier()).orElse(null);
                    sb.append("| ").append(JfrAnalysisService.display(duration)).append(" | ")
                            .append(op).append(" | ")
                            .append(JfrAnalysisService.display(start)).append(" |\n");
                });
        sb.append("\n");

        // VM Operations Summary
        IItemCollection vmOps = events.apply(ItemFilters.type("jdk.ExecuteVMOperation"));
        if (vmOps.hasItems()) {
            sb.append("## VM Operations Summary\n");
            long vmoCount = JfrItemUtils.count(vmOps);
            IQuantity vmoAvg = JfrItemUtils.avgQuantity(vmOps, JfrAttributes.DURATION.getIdentifier());
            IQuantity vmoMax = JfrItemUtils.maxQuantity(vmOps, JfrAttributes.DURATION.getIdentifier());
            sb.append("- **Total VM Operations:** ").append(vmoCount).append("\n");
            sb.append("- **Average Duration:** ").append(JfrAnalysisService.display(vmoAvg)).append("\n");
            sb.append("- **Max Duration:** ").append(JfrAnalysisService.display(vmoMax)).append("\n\n");
        }

        // TTSP
        IItemCollection ttsp = events.apply(ItemFilters.type("jdk.SafepointStateSynchronization"));
        if (ttsp.hasItems()) {
            sb.append("## TTSP (Time-To-Safepoint)\n");
            IQuantity avgTtsp = JfrItemUtils.avgQuantity(ttsp, JfrAttributes.DURATION.getIdentifier());
            IQuantity maxTtsp = JfrItemUtils.maxQuantity(ttsp, JfrAttributes.DURATION.getIdentifier());
            IQuantity p95Ttsp = JfrItemUtils.percentileQuantity(ttsp, JfrAttributes.DURATION.getIdentifier(), 95);
            sb.append("- **Average TTSP:** ").append(JfrAnalysisService.display(avgTtsp)).append("\n");
            sb.append("- **Max TTSP:** ").append(JfrAnalysisService.display(maxTtsp)).append("\n");
            sb.append("- **P95 TTSP:** ").append(JfrAnalysisService.display(p95Ttsp)).append("\n\n");
        }

        return sb.toString();
    }

    private static class CauseStats {
        long count;
        long totalNanos;
        long maxNanos;
    }
}
