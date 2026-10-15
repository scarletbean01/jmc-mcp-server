package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.VmOperationsApplicationService;
import io.github.deplague.jmcmcp.domain.model.VmOperationsResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for analyzing VM operations and safepoint events.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class VMOperationsTool {
    private final VmOperationsApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.VM_OPERATIONS,
            title = "VM Operations Analysis",
            description = "Analyze VM operations and safepoint events in a JFR recording. "
            + "Reports longest VM operations and total STW time.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse vmOperationTool(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN

    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                VmOperationsResult result = appService.analyze(jfrFilePath, startTime, endTime, topN != null ? topN : 10);
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(VmOperationsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# VM Operations Analysis\n\n");

        if (!result.hasOperations()) {
            sb.append("No VM operation events found in the recording.\n");
            return sb.toString();
        }

        sb.append("## Summary\n");
        sb.append(String.format("- **Total VM Ops Duration:** %s%n", result.totalDuration()));
        sb.append(String.format("- **Max VM Op Duration:** %s%n", result.maxDuration()));
        sb.append(String.format("- **Avg VM Op Duration:** %s%n", result.avgDuration()));
        sb.append("\n");

        sb.append("## Longest VM Operations\n");
        sb.append("| Operation | Duration | Caller |\n");
        sb.append("|-----------|----------|--------|\n");
        for (var entry : result.operations()) {
            sb.append(String.format(
                    "| %s | %s | %s |%n",
                    entry.operation(), entry.duration(), entry.caller()
            ));
        }

        return sb.toString();
    }
}
