package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.CompareRecordingsApplicationService;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for comprehensive A/B comparison of two JFR recordings.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class CompareRecordingsTool {

    private final CompareRecordingsApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Perform a comprehensive expert-level A/B comparison of two JFR recordings. Compares CPU, GC (including P95/P99), Memory, I/O, Safepoints, and JVM internals.")
    public ToolResponse compareRecordings(
            @ToolArg(name = "baseline_jfr_path", description = "Absolute or relative path to the baseline .jfr recording file") String baselineJfrPath,
            @ToolArg(name = "target_jfr_path", description = "Absolute or relative path to the target .jfr recording file") String targetJfrPath
    ) {
        try {
            String result = appService.analyze(baselineJfrPath, targetJfrPath);
            return ToolResponse.success(result);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }
}
