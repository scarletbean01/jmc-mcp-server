package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.Aggregators;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.List;

/**
 * MCP tool for analyzing garbage collection events.
 */
public final class GcAnalysisTool {

    private static final String NAME = "gc_analysis";

    private final JfrAnalysisService service;

    public GcAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze garbage collection events in a JFR recording. " +
                                "Returns pause times (avg/max/total), frequencies, and heap summary trends.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "stat_type", SchemaUtil.stringProp(
                                                "Type of GC stats to return: pause_times, frequencies, heap_summary, or all (default)",
                                                List.of("pause_times", "frequencies", "heap_summary", "all"))
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String statType = SchemaUtil.getStringOrDefault(request.arguments(), "stat_type", "all");

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr, statType);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr, String statType) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);
        StringBuilder sb = new StringBuilder();
        sb.append("# GC Analysis\n\n");

        // GC Phase Pause
        var gcPauses = events.apply(ItemFilters.type("jdk.GCPhasePause"));
        if (gcPauses.hasItems() && ("all".equals(statType) || "pause_times".equals(statType))) {
            sb.append("## Pause Times\n");
            IQuantity avgPause = JfrItemUtils.avgQuantity(gcPauses, JfrAttributes.DURATION.getIdentifier());
            IQuantity maxPause = JfrItemUtils.maxQuantity(gcPauses, JfrAttributes.DURATION.getIdentifier());
            IQuantity totalPause = JfrItemUtils.sumQuantity(gcPauses, JfrAttributes.DURATION.getIdentifier());

            sb.append(String.format("- **Average Pause:** %s%n", JfrAnalysisService.display(avgPause)));
            sb.append(String.format("- **Maximum Pause:** %s%n", JfrAnalysisService.display(maxPause)));
            sb.append(String.format("- **Total Pause Time:** %s%n", JfrAnalysisService.display(totalPause)));
            sb.append("\n");
        }

        // GC Frequencies
        var youngGC = events.apply(ItemFilters.type("jdk.YoungGarbageCollection"));
        var oldGC = events.apply(ItemFilters.type("jdk.OldGarbageCollection"));
        if ((youngGC.hasItems() || oldGC.hasItems()) && ("all".equals(statType) || "frequencies".equals(statType))) {
            sb.append("## GC Frequencies\n");
            if (youngGC.hasItems()) {
                sb.append(String.format("- **Young GCs:** %s%n", JfrAnalysisService.display(youngGC.getAggregate(Aggregators.count()))));
            }
            if (oldGC.hasItems()) {
                sb.append(String.format("- **Old GCs:** %s%n", JfrAnalysisService.display(oldGC.getAggregate(Aggregators.count()))));
            }
            sb.append("\n");
        }

        // GC Heap Summary
        if ("all".equals(statType) || "heap_summary".equals(statType)) {
            var heapSummary = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
            if (heapSummary.hasItems()) {
                sb.append("## Heap Summary\n");
                IQuantity maxHeap = JfrItemUtils.maxQuantity(heapSummary, "heapUsed");
                IQuantity minHeap = JfrItemUtils.minQuantity(heapSummary, "heapUsed");
                IQuantity avgHeap = JfrItemUtils.avgQuantity(heapSummary, "heapUsed");
                sb.append(String.format("- **Max Heap Used:** %s%n", JfrAnalysisService.display(maxHeap)));
                sb.append(String.format("- **Min Heap Used:** %s%n", JfrAnalysisService.display(minHeap)));
                sb.append(String.format("- **Avg Heap Used:** %s%n", JfrAnalysisService.display(avgHeap)));
                sb.append("\n");
            }
        }

        if (!gcPauses.hasItems() && !youngGC.hasItems() && !oldGC.hasItems()) {
            sb.append("No garbage collection events found in this recording range.\n");
        }

        return sb.toString();
    }


}
