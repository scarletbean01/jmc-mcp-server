package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.BlockingSummaryApplicationService;
import io.github.deplague.jmcmcp.domain.model.BlockingSummaryResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for blocking event aggregation.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class BlockingSummaryTool {

    private final BlockingSummaryApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.BLOCKING_SUMMARY,
            title = "Blocking Summary",
            description = "Aggregate all blocking events (monitors, parking, sleeping, I/O) per thread.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse blockingSummary(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                BlockingSummaryResult result = appService.analyze(
                            jfrFilePath,
                            startTime,
                            endTime,
                            topN != null ? topN : 10
                    );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(BlockingSummaryResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Blocking Summary\n\n");

        if (!result.hasData()) {
            sb.append("No blocking events found in the recording.");
            return sb.toString();
        }

        sb.append("## Blocking Overview\n\n");
        sb.append("- **Total Blocked Time:** ").append(result.totalBlockedTime()).append("\n");
        sb.append("- **Total Blocked Events:** ").append(result.totalBlockedEvents()).append("\n\n");

        if (!result.perThreadBlocking().isEmpty()) {
            sb.append("## Per-Thread Blocking Summary\n\n");
            sb.append("| Thread Name | Total Blocked Time | Event Count | Top Category |\n");
            sb.append("|-------------|--------------------|-------------|--------------|\n");
            for (var entry : result.perThreadBlocking()) {
                sb.append("| ").append(entry.threadName()).append(" | ")
                        .append(entry.totalBlockedTime()).append(" | ")
                        .append(entry.eventCount()).append(" | ")
                        .append(entry.topCategory()).append(" |\n");
            }
            sb.append("\n");
        }

        if (!result.topBlockingReasons().isEmpty()) {
            sb.append("## Top Blocking Reasons\n\n");
            sb.append("| Category | Detail | Total Time | Count |\n");
            sb.append("|----------|--------|------------|-------|\n");
            for (var entry : result.topBlockingReasons()) {
                sb.append("| ").append(entry.category()).append(" | `")
                        .append(entry.detail()).append("` | ")
                        .append(entry.totalTime()).append(" | ")
                        .append(entry.count()).append(" |\n");
            }
            sb.append("\n");
        }

        if (!result.categoryDistribution().isEmpty()) {
            sb.append("## Blocking Time Distribution\n\n");
            sb.append("| Category | Total Time | Event Count | Avg Duration |\n");
            sb.append("|----------|------------|-------------|--------------|\n");
            for (var entry : result.categoryDistribution()) {
                sb.append("| ").append(entry.category()).append(" | ")
                        .append(entry.totalTime()).append(" | ")
                        .append(entry.count()).append(" | ")
                        .append(entry.avgDuration()).append(" |\n");
            }
        }

        if (result.monitorContentionDetected()) {
            sb.append(
                    "\n<agent_hint>Monitor lock contention detected. Use `thread_contention` or `lock_analysis` to investigate which threads are holding the locks and causing these blockages.</agent_hint>\n"
            );
        }

        return sb.toString();
    }
}
