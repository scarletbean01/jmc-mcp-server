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
 * MCP tool for analyzing garbage collection causes.
 */
public final class GcCauseTool {

    private static final String NAME = "gc_cause";

    private final JfrAnalysisService service;

    public GcCauseTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze GC causes to understand what triggers garbage collections.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp()
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        Map<String, CauseStats> overallStats = new HashMap<>();
        Map<String, CauseStats> youngStats = new HashMap<>();
        Map<String, CauseStats> oldStats = new HashMap<>();

        processGcEvents(events, "jdk.YoungGarbageCollection", youngStats, overallStats);
        processGcEvents(events, "jdk.OldGarbageCollection", oldStats, overallStats);

        if (overallStats.isEmpty()) {
            return "# GC Cause Analysis\n\nNo young or old garbage collection events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# GC Cause Analysis\n\n");

        sb.append("## GC Cause Distribution (Overall)\n\n");
        sb.append("| Cause | Count | Total Pause | Avg Pause |\n");
        sb.append("|-------|-------|-------------|-----------|\n");
        
        overallStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .forEach(e -> {
                    CauseStats s = e.getValue();
                    sb.append("| ").append(e.getKey()).append(" | ")
                            .append(s.count).append(" | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.totalNanos))).append(" | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.totalNanos / s.count))).append(" |\n");
                });

        if (!youngStats.isEmpty()) {
            sb.append("\n## Young Generation GC Causes\n\n");
            sb.append("| Cause | Count | Total Pause |\n");
            sb.append("|-------|-------|-------------|\n");
            youngStats.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                    .forEach(e -> sb.append("| ").append(e.getKey()).append(" | ")
                            .append(e.getValue().count).append(" | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(e.getValue().totalNanos))).append(" |\n"));
        }

        if (!oldStats.isEmpty()) {
            sb.append("\n## Old Generation GC Causes\n\n");
            sb.append("| Cause | Count | Total Pause |\n");
            sb.append("|-------|-------|-------------|\n");
            oldStats.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                    .forEach(e -> sb.append("| ").append(e.getKey()).append(" | ")
                            .append(e.getValue().count).append(" | ")
                            .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(e.getValue().totalNanos))).append(" |\n"));
        }

        return sb.toString();
    }

    private void processGcEvents(IItemCollection events, String typeId, Map<String, CauseStats> genStats, Map<String, CauseStats> overallStats) {
        IItemCollection gcEvents = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : gcEvents) {
            IMemberAccessor<String, IItem> causeAccessor = JfrItemUtils.getAccessor(iterable.getType(), "cause");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());

            if (causeAccessor != null && durationAccessor != null) {
                for (IItem item : iterable) {
                    String cause = causeAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (cause != null && duration != null) {
                        long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                        genStats.computeIfAbsent(cause, k -> new CauseStats()).add(nanos);
                        overallStats.computeIfAbsent(cause, k -> new CauseStats()).add(nanos);
                    }
                }
            }
        }
    }

    private static class CauseStats {
        long count = 0;
        long totalNanos = 0;

        void add(long nanos) {
            count++;
            totalNanos += nanos;
        }
    }
}
