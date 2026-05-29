package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.GcAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.GcAnalysisResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for GC analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class GcAnalysisTool {

    private final GcAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze garbage collection events in a JFR recording. Returns pause times (avg/max/total), frequencies, and heap summary trends.")
    public ToolResponse gcAnalysis(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "stat_type", required = false, description = "Type of GC stats to return: pause_times, frequencies, heap_summary, or all (default)") String statType
    ) {
        try {
            GcAnalysisResult result = appService.analyze(jfrFilePath, startTime, endTime, statType != null ? statType : "all");
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
