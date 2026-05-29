package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.SmartThreadStarvationDetectorApplicationService;
import io.github.deplague.jmcmcp.domain.model.BlockedPoolEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadStarvationResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.unit.UnitLookup;

/**
 * MCP tool adapter for smart thread starvation detection.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class SmartThreadStarvationDetectorTool {

    private final SmartThreadStarvationDetectorApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Smart tool that detects thread starvation patterns: connection pool exhaustion, CPU starvation, or virtual thread pinning by correlating CPU load, thread states, and blocking events.")
    public ToolResponse smartThreadStarvationDetector(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top starvation issues (default 5)") Integer topN
    ) {
        try {
            ThreadStarvationResult result = appService.analyze(
                    jfrFilePath, startTime, endTime, topN != null ? topN : 5);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(ThreadStarvationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Smart Thread Starvation Detector\n\n");

        sb.append("## Overview\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        if (result.cpuLoad().sampleCount() > 0) {
            sb.append(String.format("| Avg JVM User CPU | %.1f%% |%n", result.cpuLoad().avgJvmUser() * 100));
            sb.append(String.format("| Avg Machine CPU | %.1f%% |%n", result.cpuLoad().avgMachineTotal() * 100));
            sb.append(String.format("| CPU Efficiency (JVM/Machine) | %.1f%% |%n", result.cpuLoad().efficiency() * 100));
        }
        sb.append("| Active Threads (samples) | ").append(result.activeThreadCount()).append(" |\n");
        sb.append("| Blocked Thread Events | ").append(result.totalBlockedEvents()).append(" |\n");
        if (result.threadDump().totalThreads() > 0) {
            sb.append("| Threads in BLOCKED state (dumps) | ").append(result.threadDump().blockedCount()).append(" |\n");
            sb.append("| Threads in WAITING state (dumps) | ").append(result.threadDump().waitingCount()).append(" |\n");
        }
        sb.append("\n");

        sb.append("## Primary Diagnosis: ").append(result.primaryDiagnosis()).append("\n\n");

        if (!result.findings().isEmpty()) {
            sb.append("### Supporting Evidence\n\n");
            for (String finding : result.findings()) {
                sb.append("- ").append(finding).append("\n");
            }
            sb.append("\n");
        }

        if (!result.topBlockedPools().isEmpty()) {
            sb.append("## Top Blocked Thread Pools\n\n");
            sb.append("| Pool / Thread | Block Events | Total Block Time | Avg Block Time |\n");
            sb.append("|---------------|-------------:|-----------------:|---------------:|\n");

            for (BlockedPoolEntry entry : result.topBlockedPools()) {
                long avgNanos = entry.blockCount() > 0 ? entry.totalBlockedNanos() / entry.blockCount() : 0;
                sb.append(String.format("| `%s` | %d | %s | %s |%n",
                        entry.poolName(),
                        entry.blockCount(),
                        UnitLookup.NANOSECOND.quantity(entry.totalBlockedNanos())
                                .displayUsing(IDisplayable.AUTO),
                        UnitLookup.NANOSECOND.quantity(avgNanos)
                                .displayUsing(IDisplayable.AUTO)));
            }
            sb.append("\n");
        }

        if (result.connectionPool().poolDetected()) {
            sb.append("## Connection Pool Analysis\n\n");
            sb.append("- **Pool Detected:** `").append(result.connectionPool().poolName()).append("`\n");
            sb.append("- **Threads Waiting on Pool:** ").append(result.connectionPool().threadsWaiting()).append("\n");
            sb.append("- **Pool Block Events:** ").append(result.connectionPool().blockEvents()).append("\n");
            sb.append("- **Confidence:** ").append(String.format("%.0f%%", result.connectionPool().confidence() * 100)).append("\n\n");
        }

        sb.append("<agent_hint>").append(result.agentHint()).append("</agent_hint>\n");

        return sb.toString();
    }
}
