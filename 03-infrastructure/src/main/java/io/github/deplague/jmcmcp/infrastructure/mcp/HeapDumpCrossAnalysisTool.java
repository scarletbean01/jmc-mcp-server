package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.HeapDumpCrossAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.CrossAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.UnifiedClassEntry;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class HeapDumpCrossAnalysisTool {

    private final HeapDumpCrossAnalysisApplicationService crossAnalysisService;

    @Blocking
    @Tool(description = "Perform a unified cross-analysis between a JFR recording and a linked heap dump. Correlates JFR OldObjectSample leak signals with heap dump retained sizes to produce severity-ranked leak candidates.")
    public ToolResponse heapDumpCrossAnalysis(
            @ToolArg(name = "recording_path", description = "Absolute or relative path to the .jfr recording file") String recordingPath,
            @ToolArg(name = "heap_dump_path", description = "Absolute or relative path to the .hprof heap dump file") String heapDumpPath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top correlated classes to return (default 50)") Integer topN
    ) {
        try {
            CrossAnalysisResult result = crossAnalysisService.analyze(
                    recordingPath, heapDumpPath, startTime, endTime, topN != null ? topN : 50);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(CrossAnalysisResult result) {
        if (!result.hasData()) {
            return "# JFR ↔ Heap Dump Cross Analysis\n\nNo correlation data available.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("# JFR ↔ Heap Dump Cross Analysis\n\n");
        sb.append("## Summary\n\n");
        sb.append("- **High Severity:** ").append(result.summary().highSeverityCount()).append("\n");
        sb.append("- **Medium Severity:** ").append(result.summary().mediumSeverityCount()).append("\n");
        sb.append("- **Low Severity:** ").append(result.summary().lowSeverityCount()).append("\n");
        sb.append("- **Total Classes Correlated:** ").append(result.summary().totalClasses()).append("\n\n");

        sb.append("## Recommendation\n\n");
        sb.append(result.recommendation()).append("\n\n");

        sb.append("## Unified Class View\n\n");
        sb.append("| Severity | Class | JFR Samples | Heap Instances | Heap Retained |\n");
        sb.append("|----------|-------|------------:|---------------:|--------------:|\n");

        for (UnifiedClassEntry entry : result.classes()) {
            sb.append("| ").append(entry.severity()).append(" | `")
                    .append(entry.className()).append("` | ")
                    .append(String.format("%,d", entry.jfrSampleCount())).append(" | ")
                    .append(String.format("%,d", entry.heapInstanceCount())).append(" | ")
                    .append(String.format("%,d", entry.heapRetainedSize())).append(" |\n");
        }

        sb.append("\n<agent_hint>For HIGH severity classes, inspect allocation sites with `memory_leaks` and "
                + "trace specific object IDs with `heap_dump_reference_graph`.</agent_hint>\n");
        return sb.toString();
    }
}
