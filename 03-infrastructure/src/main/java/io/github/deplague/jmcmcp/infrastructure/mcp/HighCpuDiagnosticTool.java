package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.HighCpuDiagnosticApplicationService;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
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
    @Tool(
            name = ToolNames.HIGH_CPU_DIAGNOSTIC,
            title = "High CPU Diagnostic",
            description = "Macro tool: Automatically orchestrates system health, thread CPU, and hot methods to diagnose high CPU usage.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse smartDiagnoseHighCpu(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "package_prefix", required = false, description = ToolDocs.PACKAGE_PREFIX) String packagePrefix
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                String result = appService.analyze(jfrFilePath, startTime, endTime, packagePrefix);
                    return ToolResponse.success(ToolOutput.limit(result));
    }
}
