package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.GcAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.GcAnalysisResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * MCP tool adapter for GC analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class GcAnalysisTool implements McpTool {

    private static final String NAME = "gc_analysis";

    private final GcAnalysisApplicationService appService;

    @Override
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

                        GcAnalysisResult result = appService.analyze(filePath, startTimeStr, endTimeStr, statType);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder().addTextContent(markdown).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                })
                .build();
    }

    private String formatMarkdown(GcAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# GC Analysis\n\n");

        result.pauseTimes().ifPresent(pt -> {
            sb.append("## Pause Times\n");
            sb.append(String.format("- **Average Pause:** %s%n", pt.avgPause()));
            sb.append(String.format("- **Maximum Pause:** %s%n", pt.maxPause()));
            sb.append(String.format("- **Total Pause Time:** %s%n", pt.totalPause()));
            sb.append("\n");
        });

        result.frequencies().ifPresent(freq -> {
            sb.append("## GC Frequencies\n");
            if (freq.youngGCs() > 0) sb.append(String.format("- **Young GCs:** %d%n", freq.youngGCs()));
            if (freq.oldGCs() > 0) sb.append(String.format("- **Old GCs:** %d%n", freq.oldGCs()));
            sb.append("\n");
        });

        result.heapSummary().ifPresent(heap -> {
            sb.append("## Heap Summary\n");
            sb.append(String.format("- **Max Heap Used:** %s%n", heap.maxHeapUsed()));
            sb.append(String.format("- **Min Heap Used:** %s%n", heap.minHeapUsed()));
            sb.append(String.format("- **Avg Heap Used:** %s%n", heap.avgHeapUsed()));
            sb.append("\n");
        });

        if (!result.hasData()) {
            sb.append("No garbage collection events found in this recording range.\n");
        } else {
            sb.append("\n<agent_hint>GC analysis complete. Consider `gc_detail` for per-phase pause breakdowns, `gc_recommendations` for JVM tuning advice, or `heap_trends` for memory growth patterns.</agent_hint>\n");
        }

        return sb.toString();
    }
}
