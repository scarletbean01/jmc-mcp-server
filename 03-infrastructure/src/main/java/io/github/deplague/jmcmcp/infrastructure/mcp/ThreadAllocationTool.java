package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.ThreadAllocationApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadAllocationResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for per-thread allocation breakdown.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ThreadAllocationTool {

    private final ThreadAllocationApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.THREAD_ALLOCATION,
            title = "Thread Allocation Analysis",
            description = "Identify which threads are allocating the most memory based on thread allocation statistics.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse threadAllocation(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String filePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                if (topN == null) {
                        topN = 10;
                    }
                    ThreadAllocationResult result = appService.analyze(filePath, startTime, endTime, topN != null ? topN : 10);
                    return ToolResponse.success(ToolOutput.limit(formatMarkdown(result)));
    }

    private String formatMarkdown(ThreadAllocationResult result) {
        if (!result.hasData()) {
            return "# Thread Allocation Analysis\n\nNo thread allocation statistics found in the recording. JFR event 'jdk.ThreadAllocationStatistics' may not be enabled.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Allocation Analysis\n\n");

        sb.append("## Top Allocating Threads\n\n");
        sb.append("| Thread Name | Total Allocated (Estimated) | Allocation Rate |\n");
        sb.append("|-------------|-----------------------------|-----------------|\n");

        for (var entry : result.entries()) {
            sb.append("| ").append(entry.threadName()).append(" | ")
                    .append(entry.totalAllocated()).append(" | ")
                    .append(entry.allocationRate()).append(" |\n");
        }

        if (result.heavyAllocationDetected()) {
            sb.append(
                    "\n<agent_hint>High allocation detected. Use `allocation_hotspots` or `allocation_flame` with an optional `package_prefix` to find the exact classes and call paths responsible for this memory pressure.</agent_hint>\n"
            );
        }

        return sb.toString();
    }
}
