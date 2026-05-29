package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.HighCpuDiagnosticApplicationService;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for high-CPU diagnostic macro analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class HighCpuDiagnosticTool {

    private final HighCpuDiagnosticApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Macro tool: Automatically orchestrates system health, thread CPU, and hot methods to diagnose high CPU usage.")
    public ToolResponse smartDiagnoseHighCpu(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "package_prefix", required = false, description = "Optional package prefix to filter stack traces (e.g., 'com.mycompany')") String packagePrefix
    ) {
        try {
            String result = appService.analyze(jfrFilePath, startTime, endTime, packagePrefix);
            return ToolResponse.success(result);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }
}
