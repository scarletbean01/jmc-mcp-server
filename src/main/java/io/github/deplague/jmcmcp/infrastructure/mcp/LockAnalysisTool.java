package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.LockAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.LockAnalysisResult;
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
 * MCP tool adapter for advanced lock analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class LockAnalysisTool {

    private final LockAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze ThreadPark and Biased Lock Revocation events for advanced lock contention.")
    public ToolResponse lockAnalysis(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top sites to return (default 10)") Integer topN
    ) {
        try {
            LockAnalysisResult result = appService.analyze(jfrFilePath, startTime, endTime, topN != null ? topN : 10);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(LockAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Advanced Lock Analysis\n\n");

        if (result.threadParkSummary().isPresent()) {
            var park = result.threadParkSummary().get();
            sb.append("## Thread Park Summary (LockSupport.park)\n");
            sb.append("- **Total Park Events:** ").append(park.count()).append("\n");
            sb.append("- **Avg Park Duration:** ").append(park.avgDuration()).append("\n");
            sb.append("- **Max Park Duration:** ").append(park.maxDuration()).append("\n\n");

            if (!park.topSites().isEmpty()) {
                sb.append("### Top Park Sites\n");
                sb.append("| Stack Trace | Count | Avg Duration | Max Duration |\n");
                sb.append("|---|---|---|---|\n");
                for (var site : park.topSites()) {
                    sb.append(String.format("| `%s` | %d | %s | %s |%n",
                            site.stackTrace().replace("\n", "`<br>`"),
                            site.count(),
                            site.avgDuration(),
                            site.maxDuration()));
                }
                sb.append("\n");
            }
        } else {
            sb.append("No Thread Park events found.\n\n");
        }

        if (result.biasedLockSummary().isPresent()) {
            var biased = result.biasedLockSummary().get();
            sb.append("## Biased Lock Revocations\n");
            sb.append("- **Single Revocations:** ").append(biased.singleRevocations()).append("\n");
            sb.append("- **Class/Bulk Revocations:** ").append(biased.classRevocations()).append("\n");
            sb.append("- **Self Revocations:** ").append(biased.selfRevocations()).append("\n\n");

            if (!biased.topClasses().isEmpty()) {
                sb.append("### Revoked Lock Classes\n");
                sb.append("| Lock Class | Revocation Count |\n");
                sb.append("|---|---|\n");
                for (var entry : biased.topClasses()) {
                    sb.append(String.format("| `%s` | %d |%n", entry.lockClass(), entry.count()));
                }
            }
        } else {
            sb.append("No Biased Lock Revocation events found.\n");
        }

        sb.append("\n<agent_hint>Lock contention detected. Consider `correlate` to see if I/O is performed under contended locks (a critical anti-pattern), or `deadlock_detection` to check for deadlock cycles.</agent_hint>\n");

        return sb.toString();
    }
}
