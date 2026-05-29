package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.AllocationHotspotsApplicationService;
import io.github.deplague.jmcmcp.domain.model.AllocationHotspotEntry;
import io.github.deplague.jmcmcp.domain.model.AllocationHotspotsResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP tool adapter for memory allocation hotspot analysis.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@HandleToolError
@ApplicationScoped
public final class AllocationHotspotsTool {

    private final AllocationHotspotsApplicationService applicationService;

    @Inject
    public AllocationHotspotsTool(AllocationHotspotsApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RunOnVirtualThread
    @Tool(description = "Find memory allocation hotspots and allocation sites in a JFR recording. " +
            "Reports top allocating classes and their call paths.")
    public ToolResponse allocationHotspots(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "package_prefix", required = false, description = "Optional package prefix to filter stack traces (e.g., 'com.mycompany')") String packagePrefix,
            @ToolArg(name = "top_n", required = false, description = "Number of top allocation sites to return (default 10)") Integer topN
    ) {
        try {
            AllocationHotspotsResult result = applicationService.analyze(
                    jfrFilePath, startTime, endTime, packagePrefix, topN != null ? topN : 10);

            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(AllocationHotspotsResult result) {
        if (!result.hasData()) {
            return "# Allocation Hotspots\n\nNo allocation events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Allocation Hotspots\n\n");
        sb.append("| Total Allocated | Class | Allocation Site (top 5 frames) |\n");
        sb.append("|-----------------|-------|--------------------------------|\n");

        for (AllocationHotspotEntry entry : result.entries()) {
            sb.append("| ").append(entry.formattedBytes()).append(" | ");
            sb.append("`").append(entry.className()).append("` | ");
            sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`")).append("` |\n");
        }

        return sb.toString();
    }
}
