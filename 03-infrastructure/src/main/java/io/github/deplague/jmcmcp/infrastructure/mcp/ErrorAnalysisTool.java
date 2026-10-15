package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.ErrorAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.ErrorAnalysisResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for error analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ErrorAnalysisTool {

    private final ErrorAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.ERROR_ANALYSIS,
            title = "Error Analysis",
            description = "Analyze Java errors (OutOfMemoryError, StackOverflowError, InternalError, etc.) in a JFR recording. Reports top errors by class, message, and throw site with severity classification.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse errorAnalysis(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                ErrorAnalysisResult result = appService.analyze(jfrFilePath, startTime, endTime, topN != null ? topN : 10);
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(ErrorAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Error Analysis\n\n");

        if (!result.hasData()) {
            sb.append("No Java error throw events found in the recording.");
            return sb.toString();
        }

        sb.append("## Error Summary\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append("| Total Errors | ").append(result.totalErrors()).append(" |\n");
        sb.append("| Total Exceptions | ").append(result.totalExceptions()).append(" |\n");
        if (result.totalExceptions() > 0) {
            sb.append(String.format("| Error-to-Exception Ratio | %.2f%% |%n", (result.totalErrors() * 100.0) / result.totalExceptions()));
        }
        sb.append("\n");

        sb.append("## Top Errors by Class\n\n");
        sb.append("| Count | Error Class | Message | Throw Site (top 5 frames) | Severity |\n");
        sb.append("|------|-------------|---------|---------------------------|----------|\n");

        for (var entry : result.topErrors()) {
            sb.append("| ").append(entry.count()).append(" | ");
            sb.append("`").append(entry.className()).append("` | ");
            sb.append("`").append(entry.message().replace("\n", " ")).append("` | ");
            sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`")).append("` | ");
            sb.append(entry.severity()).append(" |\n");
        }

        return sb.toString();
    }
}
