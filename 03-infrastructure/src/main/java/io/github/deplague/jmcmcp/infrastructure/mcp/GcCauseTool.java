package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.GcCauseApplicationService;
import io.github.deplague.jmcmcp.domain.model.GcCauseResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for analyzing GC causes.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class GcCauseTool {

    private final GcCauseApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.GC_CAUSE,
            title = "GC Cause Analysis",
            description = "Analyze GC causes to understand what triggers garbage collections.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse gcCause(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                GcCauseResult result = appService.analyze(
                            jfrFilePath,
                            startTime,
                            endTime
                    );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(GcCauseResult result) {
        if (!result.hasData()) {
            return "# GC Cause Analysis\n\nNo young or old garbage collection events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# GC Cause Analysis\n\n");

        sb.append("## GC Cause Distribution (Overall)\n\n");
        sb.append("| Cause | Count | Total Pause | Avg Pause |\n");
        sb.append("|-------|-------|-------------|-----------|\n");
        for (var entry : result.overall()) {
            sb.append(String.format(
                    "| %s | %d | %s | %s |%n",
                    entry.cause(), entry.count(), entry.totalPause(), entry.avgPause()
            ));
        }

        if (!result.youngGen().isEmpty()) {
            sb.append("\n## Young Generation GC Causes\n\n");
            sb.append("| Cause | Count | Total Pause |\n");
            sb.append("|-------|-------|-------------|\n");
            for (var entry : result.youngGen()) {
                sb.append(String.format(
                        "| %s | %d | %s |%n",
                        entry.cause(), entry.count(), entry.totalPause()
                ));
            }
        }

        if (!result.oldGen().isEmpty()) {
            sb.append("\n## Old Generation GC Causes\n\n");
            sb.append("| Cause | Count | Total Pause |\n");
            sb.append("|-------|-------|-------------|\n");
            for (var entry : result.oldGen()) {
                sb.append(String.format(
                        "| %s | %d | %s |%n",
                        entry.cause(), entry.count(), entry.totalPause()
                ));
            }
        }

        return sb.toString();
    }
}
