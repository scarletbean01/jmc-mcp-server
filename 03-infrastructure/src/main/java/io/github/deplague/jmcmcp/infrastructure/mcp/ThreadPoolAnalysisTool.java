package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.FormatUtil;
import io.github.deplague.jmcmcp.application.service.ThreadPoolAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadPoolAnalysisResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for thread pool analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ThreadPoolAnalysisTool {

    private final ThreadPoolAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.THREAD_POOL_ANALYSIS,
            title = "Thread Pool Analysis",
            description = "Analyze thread pool utilization and detect thread pool starvation. Groups threads by name prefix to identify pools, computes active ratios, and detects saturation patterns.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse threadPoolAnalysis(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
        ThreadPoolAnalysisResult result = appService.analyze(
                jfrFilePath, startTime, endTime, topN != null ? topN : 10
        );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(ThreadPoolAnalysisResult result) {
        if (!result.hasData()) {
            return "# Thread Pool Analysis\n\nInsufficient thread activity events found for pool analysis.";
        }

        if (result.pools().isEmpty()) {
            return "# Thread Pool Analysis\n\nNo identifiable thread pools found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Pool Analysis\n\n");

        sb.append("## Thread Pool Summary\n\n");
        sb.append("| Pool Prefix | Thread Count | CPU Samples | Blocked Time | Blocked Count | Active Ratio | Status |\n");
        sb.append("|-------------|-------------|-------------|-------------|---------------|-------------|--------|\n");

        for (var pool : result.pools()) {
            sb.append(String.format(
                    "| `%s` | %d | %d | %s | %d | %.1f%% | %s |\n",
                    pool.poolPrefix(),
                    pool.threadCount(),
                    pool.cpuSamples(),
                    FormatUtil.formatDuration(pool.blockedTimeMs()),
                    pool.blockedCount(),
                    pool.activeRatio(),
                    pool.status()));
        }
        sb.append("\n");

        if (!result.warnings().isEmpty()) {
            sb.append("## ⚠️ Warnings\n\n");
            for (String warning : result.warnings()) {
                sb.append("- ").append(warning).append("\n");
            }
            sb.append("\n");
        }

        if (!result.recommendations().isEmpty()) {
            sb.append("## Recommendations\n\n");
            for (int i = 0; i < result.recommendations().size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, result.recommendations().get(i)));
            }
            sb.append("\n");
        }

        sb.append("## Blocking Breakdown by Type\n\n");
        sb.append("| Pool Prefix | Monitor Enter | Monitor Wait | Thread Park | Thread Sleep |\n");
        sb.append("|-------------|---------------|-------------|-------------|-------------|\n");
        for (var pool : result.pools()) {
            sb.append(String.format(
                    "| `%s` | %d | %d | %d | %d |\n",
                    pool.poolPrefix(),
                    pool.monitorEnterCount(),
                    pool.monitorWaitCount(),
                    pool.parkCount(),
                    pool.sleepCount()));
        }

        return sb.toString();
    }
}
