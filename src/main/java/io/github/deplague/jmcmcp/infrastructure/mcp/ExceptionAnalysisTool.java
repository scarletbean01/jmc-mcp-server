package io.github.deplague.jmcmcp.infrastructure.mcp;

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
    @Tool(description = "Analyze Java exceptions in a JFR recording. Reports top exceptions by class, message, and throw site.")
    public ToolResponse exceptionAnalysis(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top exceptions to return (default 10)") Integer topN
    ) {
        try {
            ExceptionAnalysisResult result = appService.analyze(
                    jfrFilePath,
                    startTime,
                    endTime,
                    topN != null ? topN : 10
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
