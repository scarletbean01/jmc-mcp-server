package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.AllocationFlameApplicationService;
import io.github.deplague.jmcmcp.domain.model.AllocationFlameEntry;
import io.github.deplague.jmcmcp.domain.model.AllocationFlameResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP tool adapter for allocation flame graph data.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@HandleToolError
@ApplicationScoped
public final class AllocationFlameTool {

    private final AllocationFlameApplicationService applicationService;

    @Inject
    public AllocationFlameTool(AllocationFlameApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.ALLOCATION_FLAME,
            title = "Allocation Flame Graph",
            description = "Provide allocation flame graph data by aggregating object allocations by full stack trace.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse allocationFlame(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "package_prefix", required = false, description = ToolDocs.PACKAGE_PREFIX) String packagePrefix,
            @ToolArg(name = "top_n", required = false, defaultValue = "20", description = ToolDocs.TOP_N_20) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                AllocationFlameResult result = applicationService.analyze(
                            jfrFilePath, startTime, endTime, packagePrefix, topN != null ? topN : 20);
        
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(AllocationFlameResult result) {
        if (result.totalBytes() == 0) {
            return "# Allocation Flame Data\n\nNo allocation events found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Allocation Flame Graph Data\n\n");
        sb.append("- **Total Allocated Bytes:** ")
                .append(result.formattedTotalBytes())
                .append("\n\n");

        sb.append("## Top Allocation Call Paths (Max 10 frames)\n");
        sb.append("| Allocated Bytes | Percentage | Call Path |\n|---|---|---|\n");

        for (AllocationFlameEntry entry : result.entries()) {
            sb.append(String.format("| %s | %.1f%% | `%s` |\n",
                    entry.formattedBytes(),
                    entry.percentage(),
                    entry.callPath().replace("\n", "`<br>`")));
        }

        return sb.toString();
    }
}
