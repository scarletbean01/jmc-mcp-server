package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.ExceptionAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.ExceptionAnalysisResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for analyzing Java exceptions.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ExceptionAnalysisTool {

    private final ExceptionAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.EXCEPTION_ANALYSIS,
            title = "Exception Analysis",
            description = "Analyze Java exceptions in a JFR recording. Reports top exceptions by class, message, and throw site.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse exceptionAnalysis(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                ExceptionAnalysisResult result = appService.analyze(
                            jfrFilePath,
                            startTime,
                            endTime,
                            topN != null ? topN : 10
                    );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(ExceptionAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Exception Analysis\n\n");

        if (!result.hasData()) {
            sb.append("No exception or error throw events found in the recording.");
            return sb.toString();
        }

        sb.append("- **Total Exceptions:** ").append(result.totalExceptions()).append("\n");
        sb.append("- **Total Errors:** ").append(result.totalErrors()).append("\n");
        if (result.totalExceptions() > 0) {
            sb.append(String.format(
                    "- **Error to Exception Ratio:** %.4f%n",
                    (double) result.totalErrors() / result.totalExceptions()
            ));
        }
        sb.append("\n");

        sb.append("| Count | Class | Message | Throw Site (top 5 frames) |\n");
        sb.append("|-------|-------|---------|---------------------------|\n");

        for (var entry : result.topExceptions()) {
            sb.append("| ").append(entry.count()).append(" | ");
            sb.append("`").append(entry.className()).append("` | ");
            sb.append("`").append(entry.message().replace("\n", " ")).append("` | ");
            sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`")).append("` |\n");
        }

        return sb.toString();
    }
}
