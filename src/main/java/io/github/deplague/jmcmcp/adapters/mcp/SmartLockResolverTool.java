package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.SmartLockResolverApplicationService;
import io.github.deplague.jmcmcp.domain.model.LockHolderIssue;
import io.github.deplague.jmcmcp.domain.model.SmartLockResolverResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for smart lock resolver analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class SmartLockResolverTool implements McpTool {

    private static final String NAME = "smart_lock_resolver";

    private final SmartLockResolverApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Smart tool that identifies lock holder threads causing contention, " +
                                "analyzes what the holder is doing while blocking others, and suggests remedies.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top lock issues to return (default 5)", 5)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 5);

                        SmartLockResolverResult result = appService.analyze(filePath, startTimeStr, endTimeStr, topN);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder().addTextContent(markdown).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                })
                .build();
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
