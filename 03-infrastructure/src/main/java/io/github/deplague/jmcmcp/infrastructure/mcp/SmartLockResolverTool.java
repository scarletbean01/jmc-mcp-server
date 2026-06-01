package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.SmartLockResolverApplicationService;
import io.github.deplague.jmcmcp.domain.model.LockHolderIssue;
import io.github.deplague.jmcmcp.domain.model.SmartLockResolverResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for smart lock resolver analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class SmartLockResolverTool {

    private final SmartLockResolverApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Smart tool that identifies lock holder threads causing contention, analyzes what the holder is doing while blocking others, and suggests remedies.")
    public ToolResponse smartLockResolver(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top lock issues to return (default 5)") Integer topN
    ) {
        try {
            SmartLockResolverResult result = appService.analyze(jfrFilePath, startTime, endTime, topN != null ? topN : 5);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(SmartLockResolverResult result) {
        if (!result.hasMonitorEnters()) {
            return "# Smart Lock Resolver\n\nNo monitor contention events (jdk.JavaMonitorEnter) found in the recording.\n";
        }

        if (result.totalDistinctPatterns() == 0) {
            return "# Smart Lock Resolver\n\nNo lock holder data could be extracted from the recording.\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Smart Lock Resolver\n\n");
        sb.append("Found ").append(result.totalDistinctPatterns()).append(" distinct lock holder patterns.\n\n");

        for (int i = 0; i < result.topIssues().size(); i++) {
            LockHolderIssue issue = result.topIssues().get(i);
            sb.append("## Issue #").append(i + 1).append("\n\n");
            sb.append("- **Monitor:** `").append(issue.monitorClass()).append("`\n");
            sb.append("- **Holder Thread:** `").append(issue.holderName()).append("`\n");
            sb.append("- **Threads Blocked:** ").append(issue.blockedThreadCount()).append("\n");
            sb.append("- **Total Block Events:** ").append(issue.blockedCount()).append("\n");
            sb.append("- **Total Blocked Duration:** ").append(issue.totalBlockedDuration()).append("\n");

            if (issue.holderActivity() != null) {
                sb.append("- **Holder Activity:** ").append(issue.holderActivity().description()).append("\n");
                if (issue.holderActivity().topFrame() != null) {
                    sb.append("- **Holder Top Frame:** `").append(issue.holderActivity().topFrame()).append("`\n");
                }
                if (issue.holderActivity().hasIo()) {
                    sb.append("- **⚠️ Holder is performing I/O while holding the lock**\n");
                }
                if (issue.holderActivity().hasSql()) {
                    sb.append("- **⚠️ Holder is performing SQL while holding the lock**\n");
                }
            }

            if (!issue.topBlockedTraces().isEmpty()) {
                sb.append("\n**Top Blocked Call Sites:**\n\n");
                for (var trace : issue.topBlockedTraces()) {
                    sb.append("- `").append(trace.stackTrace().replace("\n", "`<br>`")).append("` (")
                            .append(trace.count()).append(" times)\n");
                }
            }
            sb.append("\n");
        }

        // Build agent hint from the worst offender
        LockHolderIssue worst = result.topIssues().get(0);

        StringBuilder hint = new StringBuilder();
        hint.append("Lock contention resolved: Thread `").append(worst.holderName()).append("` is holding `")
                .append(worst.monitorClass()).append("` and blocking ").append(worst.blockedThreadCount())
                .append(" other threads. ");
        if (worst.holderActivity() != null && worst.holderActivity().hasIo()) {
            hint.append("The holder is performing I/O while holding the lock — a critical anti-pattern. ");
            hint.append("Consider reducing lock scope or using `ReentrantReadWriteLock`.");
        } else if (worst.holderActivity() != null && worst.holderActivity().hasSql()) {
            hint.append("The holder is performing SQL while holding the lock. ");
            hint.append("Consider moving the SQL outside the synchronized block.");
        } else {
            hint.append("Consider using `ReentrantLock` with timeout or `StampedLock` to reduce contention.");
        }
        hint.append(" Use `smart_correlate` to see if this lock is associated with I/O hotspots.");

        sb.append("<agent_hint>").append(hint).append("</agent_hint>\n");

        return sb.toString();
    }
}
