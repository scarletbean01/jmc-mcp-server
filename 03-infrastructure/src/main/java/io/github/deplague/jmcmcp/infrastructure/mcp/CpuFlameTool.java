package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.CpuFlameApplicationService;
import io.github.deplague.jmcmcp.domain.model.CallPathEntry;
import io.github.deplague.jmcmcp.domain.model.CpuFlameMethodEntry;
import io.github.deplague.jmcmcp.domain.model.CpuFlameResult;
import io.github.deplague.jmcmcp.domain.model.StateDistributionEntry;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP tool adapter for CPU flame graph data and execution sample profiling.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@HandleToolError
@ApplicationScoped
public final class CpuFlameTool {

    private final CpuFlameApplicationService applicationService;

    @Inject
    public CpuFlameTool(CpuFlameApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.CPU_FLAME,
            title = "CPU Flame Graph",
            description = "Provide CPU flame graph data including thread states and hottest call paths.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse cpuFlame(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "20", description = ToolDocs.TOP_N_20) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                CpuFlameResult result = applicationService.analyze(
                            jfrFilePath, startTime, endTime, topN != null ? topN : 20);
        
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(CpuFlameResult result) {
        if (result.totalSamples() == 0) {
            return "# CPU Flame Data\n\nNo execution samples found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# CPU Flame Graph Data\n\n");
        sb.append("- **Total Samples:** ").append(result.totalSamples()).append("\n\n");

        sb.append("## CPU State Distribution\n");
        sb.append("| State | Samples | Percentage |\n|---|---|---|\n");
        for (StateDistributionEntry entry : result.stateDistribution()) {
            sb.append(String.format("| `%s` | %d | %.1f%% |\n",
                    entry.state(), entry.samples(), entry.percentage()));
        }
        sb.append("\n");

        sb.append("## Top CPU Call Paths (Max 10 frames)\n");
        sb.append("| Samples | Percentage | Call Path |\n|---|---|---|\n");
        for (CallPathEntry entry : result.callPaths()) {
            sb.append(String.format("| %d | %.1f%% | `%s` |\n",
                    entry.samples(),
                    entry.percentage(),
                    entry.callPath().replace("\n", "`<br>`")));
        }
        sb.append("\n");

        sb.append("## Hottest Methods (Self Time)\n");
        sb.append("| Method | Samples | Percentage |\n|---|---|---|\n");
        for (CpuFlameMethodEntry entry : result.hotMethods()) {
            sb.append(String.format("| `%s` | %d | %.1f%% |\n",
                    entry.methodName(), entry.samples(), entry.percentage()));
        }

        return sb.toString();
    }
}
