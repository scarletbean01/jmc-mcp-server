package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.VirtualThreadsApplicationService;
import io.github.deplague.jmcmcp.domain.model.VirtualThreadsResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for virtual thread analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class VirtualThreadsTool {

    private static final String NAME = "virtual_threads";

    private final VirtualThreadsApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze virtual thread pinning sites and execution failures (Java 21+).")
    public ToolResponse virtualThreadTool(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format (e.g., 2023-10-27T10:00:00Z)") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format (e.g., 2023-10-27T10:05:00Z)") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top VM operations to return (default 10)") int topN
    ) {
        try {
            VirtualThreadsResult result = appService.analyze(
                    jfrFilePath, startTime, endTime, topN
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(VirtualThreadsResult result) {
        if (!result.hasData()) {
            return "# Virtual Thread Analysis\n\nNo virtual thread pinning or failure events found in the recording. Virtual threads may not be in use, or JFR events are not enabled.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Virtual Thread Analysis\n\n");

        if (result.pinnedCount() > 0) {
            sb.append("## Pinning Summary\n\n");
            sb.append("- **Total Pinned Events:** ").append(result.pinnedCount()).append("\n\n");

            sb.append("### Top Pinning Sites\n\n");
            sb.append("| Stack Trace (top 5 frames) | Count | Percentage |\n");
            sb.append("|----------------------------|-------|------------|\n");
            for (var site : result.pinningSites()) {
                sb.append("| `")
                        .append(site.stackTrace().replace("\n", "`<br>`"))
                        .append("` | ")
                        .append(site.count())
                        .append(" | ")
                        .append(String.format("%.2f%%", site.percentage()))
                        .append(" |\n");
            }
            sb.append("\n");
        }

        if (result.submitFailedCount() > 0) {
            sb.append("## Submission Failures (Carrier Pool Exhaustion)\n\n");
            sb.append("| Exception | Count |\n");
            sb.append("|-----------|-------|\n");
            for (var failure : result.submitFailures()) {
                sb.append("| ").append(failure.exception()).append(" | ").append(failure.count()).append(" |\n");
            }
            sb.append("\n");
        }

        if (result.sleepFailedCount() > 0) {
            sb.append("## Sleep Failures\n\n");
            sb.append("| Exception | Count |\n");
            sb.append("|-----------|-------|\n");
            for (var failure : result.sleepFailures()) {
                sb.append("| ").append(failure.exception()).append(" | ").append(failure.count()).append(" |\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
