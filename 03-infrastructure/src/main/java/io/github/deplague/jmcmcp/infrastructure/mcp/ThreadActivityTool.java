package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.ThreadActivityApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadActivityResult;
import io.github.deplague.jmcmcp.domain.model.ThreadLifecycle;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for thread activity analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ThreadActivityTool {

    private final ThreadActivityApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.THREAD_ACTIVITY,
            title = "Thread Activity",
            description = "Analyze thread lifecycle, creation/destruction rates, and sleep patterns.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse threadActivity(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String filePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                if (topN == null) {
                        topN = 10;
                    }
                    ThreadActivityResult result = appService.analyze(filePath, startTime, endTime, topN != null ? topN : 10);
                    return ToolResponse.success(ToolOutput.limit(formatMarkdown(result)));
    }

    private String formatMarkdown(ThreadActivityResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Activity Analysis\n\n");

        result.threadStats().ifPresent(stats -> {
            sb.append("## Thread Statistics\n");
            sb.append("- **Peak Thread Count:** ").append(stats.peakCount()).append("\n");
            sb.append("- **Active Count (Min/Avg/Max):** ")
                    .append(stats.activeMin()).append(" / ")
                    .append(stats.activeAvg()).append(" / ")
                    .append(stats.activeMax()).append("\n");
            sb.append("- **Max Daemon Count:** ").append(stats.daemonCount()).append("\n");
            sb.append("- **Total Threads Created (lifetime):** ").append(stats.accumulatedCount()).append("\n\n");
        });

        ThreadLifecycle lifecycle = result.threadLifecycle();
        sb.append("## Thread Lifecycle\n");
        sb.append("- **Threads Started:** ").append(lifecycle.startedCount()).append("\n");
        sb.append("- **Threads Ended:** ").append(lifecycle.endedCount()).append("\n");
        sb.append("- **Net Change:** ").append(lifecycle.netChange()).append("\n\n");

        if (!lifecycle.creationSites().isEmpty()) {
            sb.append("### Top Thread Creation Sites\n");
            sb.append("| Count | Creation Call Site (top 5 frames) |\n");
            sb.append("|-------|----------------------------------|\n");
            for (var site : lifecycle.creationSites()) {
                sb.append("| ").append(site.count()).append(" | `")
                        .append(site.trace().replace("\n", "`<br>`")).append("` |\n");
            }
            sb.append("\n");
        }

        if (!result.sleepHotspots().isEmpty()) {
            sb.append("## Thread Sleep Hotspots\n");
            sb.append("| Total Sleep Time | Count | Call Site (top 5 frames) |\n");
            sb.append("|------------------|-------|--------------------------|\n");
            for (var hotspot : result.sleepHotspots()) {
                sb.append("| ").append(hotspot.totalSleepTime()).append(" | ")
                        .append(hotspot.count()).append(" | `")
                        .append(hotspot.trace().replace("\n", "`<br>`")).append("` |\n");
            }
            sb.append("\n");
        }

        if (result.threadStats().isEmpty() && lifecycle.startedCount() == 0 && result.sleepHotspots().isEmpty()) {
            sb.append("No thread activity events found in this recording.\n");
        }

        return sb.toString();
    }
}
