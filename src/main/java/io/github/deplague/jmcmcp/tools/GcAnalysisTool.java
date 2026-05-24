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
import java.util.Map;

/**
 * MCP tool for GC (Garbage Collection) analysis.
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
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "stat_type", SchemaUtil.stringProp(
                                                "Type of GC stats to return: pause_times, frequencies, heap_summary, or all (default)",
                                                List.of("pause_times", "frequencies", "heap_summary", "all"))
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        String statType = getStringOrDefault(request.arguments(), "stat_type", "all");
                        String result = analyze(filePath, statType);
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

    private String analyze(String filePath, String statType) throws IOException {
        IItemCollection events = service.loadRecording(filePath);
        StringBuilder sb = new StringBuilder();
        sb.append("# GC Analysis\n\n");

        // GC Phase Pause
        var gcPauses = events.apply(ItemFilters.type("jdk.GCPhasePause"));
        if (gcPauses.hasItems()) {
            sb.append("## GC Phase Pauses\n");
            IQuantity count = gcPauses.getAggregate(Aggregators.count());
            IQuantity avg = gcPauses.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity max = gcPauses.getAggregate(Aggregators.max(JfrAttributes.DURATION));
            IQuantity total = gcPauses.getAggregate(Aggregators.sum(JfrAttributes.DURATION));

            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Average Pause:** %s%n", JfrAnalysisService.display(avg)));
            sb.append(String.format("- **Max Pause:** %s%n", JfrAnalysisService.display(max)));
            sb.append(String.format("- **Total Pause Time:** %s%n", JfrAnalysisService.display(total)));
            sb.append("\n");
        }

        // Young GC
        var youngGC = events.apply(ItemFilters.type("jdk.YoungGarbageCollection"));
        if (youngGC.hasItems()) {
            sb.append("## Young GC\n");
            IQuantity count = youngGC.getAggregate(Aggregators.count());
            IQuantity avg = youngGC.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity max = youngGC.getAggregate(Aggregators.max(JfrAttributes.DURATION));
            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Average Duration:** %s%n", JfrAnalysisService.display(avg)));
            sb.append(String.format("- **Max Duration:** %s%n", JfrAnalysisService.display(max)));
            sb.append("\n");
        }

        // Old GC
        var oldGC = events.apply(ItemFilters.type("jdk.OldGarbageCollection"));
        if (oldGC.hasItems()) {
            sb.append("## Old GC (Full GC)\n");
            IQuantity count = oldGC.getAggregate(Aggregators.count());
            IQuantity avg = oldGC.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity max = oldGC.getAggregate(Aggregators.max(JfrAttributes.DURATION));
            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Average Duration:** %s%n", JfrAnalysisService.display(avg)));
            sb.append(String.format("- **Max Duration:** %s%n", JfrAnalysisService.display(max)));
            sb.append("\n");
        }

        // GC Heap Summary
        if ("all".equals(statType) || "heap_summary".equals(statType)) {
            var heapSummary = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
            if (heapSummary.hasItems()) {
                sb.append("## Heap Summary\n");
                double maxHeap = JfrItemUtils.maxQuantity(heapSummary, "heapUsed");
                double minHeap = JfrItemUtils.minQuantity(heapSummary, "heapUsed");
                double avgHeap = JfrItemUtils.avgQuantity(heapSummary, "heapUsed");
                sb.append(String.format("- **Max Heap Used:** %.2f%n", maxHeap));
                sb.append(String.format("- **Min Heap Used:** %.2f%n", minHeap));
                sb.append(String.format("- **Avg Heap Used:** %.2f%n", avgHeap));
                sb.append("\n");
            }
        }

        if (!gcPauses.hasItems() && !youngGC.hasItems() && !oldGC.hasItems()) {
            sb.append("No GC events found in this recording.\n");
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static String getString(Map<String, Object> args, String key) {
        Object val = args.get(key);
        if (val == null) {
            throw new IllegalArgumentException("Missing required argument: " + key);
        }
        return val.toString();
    }

    private static String getStringOrDefault(Map<String, Object> args, String key, String defaultValue) {
        Object val = args.get(key);
        return val != null ? val.toString() : defaultValue;
    }
}
