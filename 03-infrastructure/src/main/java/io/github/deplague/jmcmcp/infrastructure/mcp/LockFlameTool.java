package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.LockFlameApplicationService;
import io.github.deplague.jmcmcp.domain.model.LockFlameEntry;
import io.github.deplague.jmcmcp.domain.model.LockFlameResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP tool adapter for lock contention flame graph data.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@HandleToolError
@ApplicationScoped
public final class LockFlameTool {

    private final LockFlameApplicationService applicationService;

    @Inject
    public LockFlameTool(LockFlameApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.LOCK_FLAME,
            title = "Lock Contention Flame Graph",
            description = "Provide lock contention flame graph data by aggregating monitor enter/wait by full stack trace.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse lockFlame(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "20", description = ToolDocs.TOP_N_20) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                LockFlameResult result = applicationService.analyze(
                            jfrFilePath, startTime, endTime, topN != null ? topN : 20);
        
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(LockFlameResult result) {
        if (result.totalNanos() == 0) {
            return "# Lock Flame Data\n\nNo lock/park events found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Lock Flame Graph Data\n\n");
        sb.append("- **Total Lock Duration:** ")
                .append(result.formattedTotalDuration())
                .append("\n\n");

        sb.append("## Top Lock Call Paths (Max 10 frames)\n");
        sb.append("| Lock Duration | Percentage | Call Path |\n|---|---|---|\n");

        for (LockFlameEntry entry : result.entries()) {
            sb.append(String.format("| %s | %.1f%% | `%s` |\n",
                    entry.formattedDuration(),
                    entry.percentage(),
                    entry.callPath().replace("\n", "`<br>`")));
        }

        return sb.toString();
    }
}
