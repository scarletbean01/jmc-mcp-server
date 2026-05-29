package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.ThreadContentionApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadContentionResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for thread contention analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ThreadContentionTool {

    private final ThreadContentionApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze thread contention in a JFR recording. Identifies top monitor lock contentions and wait locations.")
    public ToolResponse threadContention(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top contention sites to return (default 10)") int topN
    ) {
        try {
            ThreadContentionResult result = appService.analyze(jfrFilePath, startTime, endTime, topN);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(ThreadContentionResult result) {
        if (!result.hasData()) {
            return "# Thread Contention Analysis\n\nNo monitor contention or wait events found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Contention Analysis\n\n");
        sb.append("| Total Duration | Monitor Class | Contention Site (top 5 frames) |\n");
        sb.append("|----------------|---------------|--------------------------------|\n");

        for (var entry : result.topContentions()) {
            sb.append("| ").append(entry.totalDuration()).append(" | ");
            sb.append("`").append(entry.monitorClass()).append("` | ");
            sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`")).append("` |\n");
        }

        sb.append("\n<agent_hint>Lock `").append(result.topLock()).append("` has ")
                .append(result.topDuration()).append(" total contention. Consider `correlate` to see if I/O is performed under this lock, or `request_waterfall` with the contending thread name to trace the full request path.</agent_hint>\n");

        return sb.toString();
    }
}
