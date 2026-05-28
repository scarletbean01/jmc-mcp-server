package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.HotMethodEntry;
import io.github.deplague.jmcmcp.domain.model.HotMethodsResult;
import io.github.deplague.jmcmcp.domain.model.SystemHealthResult;
import io.github.deplague.jmcmcp.domain.model.ThreadCpuResult;
import io.github.deplague.jmcmcp.domain.service.HighCpuDiagnosticService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * Application service that orchestrates high-CPU diagnostic analysis.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class HighCpuDiagnosticApplicationService {

    private final JfrProvider jfrProvider;
    private final SystemHealthApplicationService systemHealthApplicationService;
    private final ThreadCpuApplicationService threadCpuApplicationService;
    private final HotMethodsApplicationService hotMethodsApplicationService;
    private final HighCpuDiagnosticService highCpuDiagnosticService;

    public String analyze(String filePath, String startTime, String endTime, String packagePrefix) throws IOException {
        jfrProvider.loadRecording(filePath);

        String healthMarkdown;
        try {
            healthMarkdown = formatSystemHealth(
                    systemHealthApplicationService.analyze(filePath, startTime, endTime));
        } catch (Exception e) {
            healthMarkdown = "Failed to gather system health: " + e.getMessage() + "\n";
        }

        String threadMarkdown;
        try {
            threadMarkdown = formatThreadCpu(
                    threadCpuApplicationService.analyze(filePath, startTime, endTime, packagePrefix, 5));
        } catch (Exception e) {
            threadMarkdown = "Failed to gather thread CPU stats: " + e.getMessage() + "\n";
        }

        String hotMethodsMarkdown;
        try {
            hotMethodsMarkdown = formatHotMethods(
                    hotMethodsApplicationService.analyze(filePath, startTime, endTime, null, packagePrefix, 10));
        } catch (Exception e) {
            hotMethodsMarkdown = "Failed to gather hot methods: " + e.getMessage() + "\n";
        }

        return highCpuDiagnosticService.formatReport(healthMarkdown, threadMarkdown, hotMethodsMarkdown);
    }

    private String formatSystemHealth(SystemHealthResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# System Health Analysis\n\n");

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

        if (result.highCpuDetected()) {
            sb.append("\n<agent_hint>High CPU load detected. Use 'thread_cpu' or 'hot_methods' to identify the culprit. Or use the new 'diagnose_high_cpu' macro tool for an automated analysis.</agent_hint>\n");
        }

        return sb.toString();
    }

    private String formatThreadCpu(ThreadCpuResult result) {
        if (result.totalSamples() == 0) {
            return "# Thread CPU Analysis\n\nNo execution samples found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread CPU Analysis\n\n");
        sb.append("Total samples: ").append(result.totalSamples()).append("\n\n");

        sb.append("## Per-Thread CPU Summary\n\n");
        sb.append("| Thread Name | Samples | CPU % | Primary States |\n");
        sb.append("|-------------|---------|-------|----------------|\n");
        for (var thread : result.threads()) {
            String states = thread.stateCounts().entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(2)
                    .map(e -> String.format("%s (%d)", e.getKey(), e.getValue()))
                    .collect(Collectors.joining(", "));
            sb.append(String.format("| %s | %d | %.2f%% | %s |%n",
                    thread.threadName(), thread.samples(), thread.cpuPercent(), states));
        }

        sb.append("\n## Top Methods per Thread\n\n");
        for (var thread : result.threads()) {
            sb.append("### ").append(thread.threadName()).append("\n\n");
            sb.append("| Samples | Method (Top Frame) |\n");
            sb.append("|---------|--------------------|\n");
            for (var method : thread.topMethods()) {
                sb.append(String.format("| %d | `%s` |%n", method.samples(), method.method()));
            }
            sb.append("\n");
        }

        sb.append("## Thread State Distribution\n\n");
        sb.append("| State | Samples | Percentage |\n");
        sb.append("|-------|---------|------------|\n");
        for (var state : result.stateDistribution()) {
            sb.append(String.format("| %s | %d | %.2f%% |%n", state.state(), state.samples(), state.percent()));
        }

        return sb.toString();
    }

    private String formatHotMethods(HotMethodsResult result) {
        if (!result.hasResults()) {
            return "# Hot Methods & Call Paths\n\nNo execution samples found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Hot Methods & Call Paths\n\n");
        sb.append("| Samples | Stack Trace (top 5 frames) |\n");
        sb.append("|---------|----------------------------|\n");

        for (HotMethodEntry entry : result.entries()) {
            sb.append("| ").append(entry.sampleCount()).append(" | ");
            sb.append("`")
                    .append(entry.stackTrace().replace("\n", "`<br>`"))
                    .append("` |\n");
        }

        String topMethod = result.topMethod() != null
                ? result.topMethod()
                : "unknown";
        sb.append("\n<agent_hint>Top hot method is `")
                .append(topMethod)
                .append("`. Consider `thread_cpu` to see which threads consume the most CPU, `stack_trace_search` with `class_pattern` to find all events involving this class, or `correlate` to see if this method is associated with lock contention or I/O.</agent_hint>\n");

        return sb.toString();
    }
}
