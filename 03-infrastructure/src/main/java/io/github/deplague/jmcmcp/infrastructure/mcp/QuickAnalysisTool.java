package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.QuickAnalysisApplicationService;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for quick analysis dashboard.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class QuickAnalysisTool {

    private final QuickAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.QUICK_ANALYSIS,
            title = "Quick Diagnostic Analysis",
            description = "One-click overview dashboard that runs the most impactful analyses in a single call with severity classification. Auto-detects the dominant bottleneck.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse smartQuickAnalysis(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "focus", required = false, description = "Focus area: cpu, memory, latency, locks, or auto (default)") String focus
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                String result = appService.analyze(
                            jfrFilePath,
                            startTime,
                            endTime,
                            focus != null ? focus : "auto"
                    );
                    return ToolResponse.success(ToolOutput.limit(result));
    }
}
