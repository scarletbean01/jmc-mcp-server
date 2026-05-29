package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.SystemHealthApplicationService;
import io.github.deplague.jmcmcp.domain.model.SystemHealthResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for system health analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class SystemHealthTool {

    private final SystemHealthApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze system health metrics from a JFR recording, including CPU load, physical memory usage, and swap usage.")
    public ToolResponse systemHealth(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime
    ) {
        try {
            SystemHealthResult result = appService.analyze(jfrFilePath, startTime, endTime);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(SystemHealthResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# System Health Analysis\n\n");

        boolean highCpuDetected = result.highCpuDetected();

        result.cpuLoad().ifPresent(cpu -> {
            sb.append("## CPU Load\n");
            if (cpu.avgMachineTotal() != null) sb.append(String.format("- **Avg Machine Total:** %s%n", cpu.avgMachineTotal()));
            if (cpu.maxMachineTotal() != null) sb.append(String.format("- **Max Machine Total:** %s%n", cpu.maxMachineTotal()));
            if (cpu.avgJvmUser() != null) sb.append(String.format("- **Avg JVM User:** %s%n", cpu.avgJvmUser()));
            if (cpu.avgJvmSystem() != null) sb.append(String.format("- **Avg JVM System:** %s%n", cpu.avgJvmSystem()));
            sb.append("\n");
        });

        result.physicalMemory().ifPresent(mem -> {
            sb.append("## Physical Memory\n");
            sb.append(String.format("- **Total Physical Memory:** %s%n", mem.totalSize()));
            sb.append(String.format("- **Min Used:** %s%n", mem.minUsed()));
            sb.append(String.format("- **Max Used:** %s%n", mem.maxUsed()));
            sb.append(String.format("- **Avg Used:** %s%n", mem.avgUsed()));
            sb.append("\n");
        });

        result.cpuInfo().ifPresent(info -> {
            sb.append("## CPU Information\n");
            if (info.cpu() != null) sb.append(String.format("- **CPU:** %s%n", info.cpu()));
            if (info.cores() > 0) sb.append(String.format("- **Cores:** %d%n", info.cores()));
            if (info.sockets() > 0) sb.append(String.format("- **Sockets:** %d%n", info.sockets()));
            sb.append("\n");
        });

        result.containerConfig().ifPresent(config -> {
            sb.append("## Container Configuration\n");
            sb.append("- **CPU Shares:** ").append(config.cpuShares()).append("\n");
            sb.append("- **CPU Period:** ").append(config.cpuPeriod()).append("\n");
            sb.append("- **CPU Quota:** ").append(config.cpuQuota()).append("\n");
            sb.append("- **Memory Limit:** ").append(config.memoryLimit()).append("\n");
            sb.append("- **Swap Limit:** ").append(config.swapLimit()).append("\n");
            sb.append("\n");
        });

        if (!result.hasData()) {
            sb.append("No system health events found in this recording.\n");
        }

        if (highCpuDetected) {
            sb.append("\n<agent_hint>High CPU load detected. Use 'thread_cpu' or 'hot_methods' to identify the culprit. Or use the new 'diagnose_high_cpu' macro tool for an automated analysis.</agent_hint>\n");
        }

        return sb.toString();
    }
}
