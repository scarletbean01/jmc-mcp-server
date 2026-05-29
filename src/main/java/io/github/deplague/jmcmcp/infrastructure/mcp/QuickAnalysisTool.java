package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.QuickAnalysisApplicationService;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
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
    @Tool(description = "One-click overview dashboard that runs the most impactful analyses in a single call with severity classification. Auto-detects the dominant bottleneck.")
    public ToolResponse smartQuickAnalysis(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "focus", required = false, description = "Focus area: cpu, memory, latency, locks, or auto (default)") String focus
    ) {
        try {
            String result = appService.analyze(
                    jfrFilePath,
                    startTime,
                    endTime,
                    focus != null ? focus : "auto"
            );
            return ToolResponse.success(result);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }
}
