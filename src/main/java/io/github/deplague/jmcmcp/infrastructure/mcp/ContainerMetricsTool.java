package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.ContainerMetricsApplicationService;
import io.github.deplague.jmcmcp.domain.model.ContainerMetricsResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for container resource limits and usage analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ContainerMetricsTool {

    private final ContainerMetricsApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze container resource limits and usage (Docker/Kubernetes).")
    public ToolResponse containerMetrics(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime
    ) {
        try {
            ContainerMetricsResult result = appService.analyze(
                    jfrFilePath,
                    startTime,
                    endTime
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(ContainerMetricsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Container Metrics Analysis\n\n");

        if (!result.hasData()) {
            sb.append(
                    "No container configuration or usage events found. The JVM is likely not running in a container, or container tracking is disabled."
            );
            return sb.toString();
        }

        result.config().ifPresent(cfg -> {
            sb.append("## Container Configuration\n");
            if (cfg.cpuShares() != null) sb.append("- **CPU Shares:** ").append(cfg.cpuShares()).append("\n");
            if (cfg.cpuPeriod() != null) sb.append("- **CPU Period:** ").append(cfg.cpuPeriod()).append("\n");
            if (cfg.cpuQuota() != null) sb.append("- **CPU Quota:** ").append(cfg.cpuQuota()).append("\n");
            if (cfg.memoryLimit() != null) sb.append("- **Memory Limit:** ").append(cfg.memoryLimit()).append("\n");
            if (cfg.swapLimit() != null) sb.append("- **Swap Limit:** ").append(cfg.swapLimit()).append("\n");
            if (cfg.memorySoftLimit() != null) sb.append("- **Memory Soft Limit:** ").append(cfg.memorySoftLimit()).append("\n");
            sb.append("\n");
        });

        result.cpuUsage().ifPresent(cpu -> {
            sb.append("## Container CPU Usage\n");
            cpu.avgCpuTime().ifPresent(v -> sb.append("- **Avg CPU Time:** ").append(v).append("\n"));
            cpu.maxCpuTime().ifPresent(v -> sb.append("- **Max CPU Time:** ").append(v).append("\n\n"));
        });

        result.memoryUsage().ifPresent(mem -> {
            sb.append("## Container Memory Usage\n");
            mem.avgMemoryUsage().ifPresent(v -> sb.append("- **Avg Memory Usage:** ").append(v).append("\n"));
            mem.maxMemoryUsage().ifPresent(v -> sb.append("- **Max Memory Usage:** ").append(v).append("\n"));
            mem.avgSwapUsage().ifPresent(v -> sb.append("- **Avg Swap Usage:** ").append(v).append("\n"));
            mem.maxSwapUsage().ifPresent(v -> sb.append("- **Max Swap Usage:** ").append(v).append("\n\n"));
        });

        return sb.toString();
    }
}
