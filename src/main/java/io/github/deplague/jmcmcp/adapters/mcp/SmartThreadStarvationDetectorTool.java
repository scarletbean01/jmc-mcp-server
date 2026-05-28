package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.SmartThreadStarvationDetectorApplicationService;
import io.github.deplague.jmcmcp.domain.model.BlockedPoolEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadStarvationResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
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
@ApplicationScoped
public final class SmartThreadStarvationDetectorTool implements McpTool {

    private static final String NAME = "smart_thread_starvation_detector";

    private final SmartThreadStarvationDetectorApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Smart tool that detects thread starvation patterns: connection pool exhaustion, " +
                                "CPU starvation, or virtual thread pinning by correlating CPU load, thread states, " +
                                "and blocking events.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top starvation issues (default 5)", 5),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 5);

                        ThreadStarvationResult result = appService.analyze(
                                filePath, startTimeStr, endTimeStr, topN);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
                                .build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
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
