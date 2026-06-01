package io.github.deplague.jmcmcp.infrastructure.mcp;

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
    @Tool(description = "Provide allocation flame graph data by aggregating object allocations by full stack trace.")
    public ToolResponse allocationFlame(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "package_prefix", required = false, description = "Optional package prefix to filter stack traces (e.g., 'com.mycompany')") String packagePrefix,
            @ToolArg(name = "top_n", required = false, description = "Number of top call paths (default 20)") Integer topN
    ) {
        try {
            AllocationFlameResult result = applicationService.analyze(
                    jfrFilePath, startTime, endTime, packagePrefix, topN != null ? topN : 20);

            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
