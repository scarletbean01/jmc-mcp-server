package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.HealthCheckApplicationService;
import io.github.deplague.jmcmcp.domain.model.HealthCheckReport;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for server health check.
 * Delegates data gathering to the application layer and formats results as Markdown.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class HealthCheckTool implements McpTool {

    private static final String NAME = "health_check";

    private final HealthCheckApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Return the server's health status, cache statistics, JVM metrics, and async job queue state."
                                )
                                .inputSchema(SchemaUtil.objectSchema(Map.of()))
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        HealthCheckReport report = appService.check();
                        String markdown = formatMarkdown(report);
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

    private String formatMarkdown(HealthCheckReport report) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Server Health Check\n\n");

        sb.append("## Status\n\n");
        sb.append("- **Overall:** `").append(report.status()).append("`\n");
        sb.append("- **Uptime:** ").append(report.uptime()).append("\n");
        sb.append("- **Server Start:** ").append(report.serverStart()).append("\n");
        sb.append("- **JVM:** ")
                .append(report.jvmName())
                .append(" ")
                .append(report.javaVersion())
                .append("\n");
        sb.append("\n");

        sb.append("## JVM Memory\n\n");
        sb.append("| Region | Used | Committed | Max | Utilization |\n");
        sb.append("|--------|------|-----------|-----|-------------|\n");
        sb.append(
                String.format(
                        "| Heap | %s | %s | %s | %.1f%% |\n",
                        SchemaUtil.formatBytes(report.jvmMemory().heapUsed()),
                        SchemaUtil.formatBytes(report.jvmMemory().heapCommitted()),
                        report.jvmMemory().heapMax() > 0
                                ? SchemaUtil.formatBytes(report.jvmMemory().heapMax())
                                : "unlimited",
                        report.heapUsedPct()
                )
        );
        sb.append(
                String.format(
                        "| Non-Heap | %s | %s | %s | — |\n",
                        SchemaUtil.formatBytes(report.jvmMemory().nonHeapUsed()),
                        SchemaUtil.formatBytes(report.jvmMemory().nonHeapCommitted()),
                        report.jvmMemory().nonHeapMax() > 0
                                ? SchemaUtil.formatBytes(report.jvmMemory().nonHeapMax())
                                : "unlimited"
                )
        );
        sb.append(
                String.format(
                        "| Total Available | — | — | %s | — |\n",
                        SchemaUtil.formatBytes(report.jvmMemory().totalMaxMemory())
                )
        );
        sb.append(
                String.format(
                        "| Free (within committed) | %s | — | — | — |\n",
                        SchemaUtil.formatBytes(report.jvmMemory().freeMemory())
                )
        );
        sb.append("\n");

        sb.append("## JVM Threads\n\n");
        sb.append("- **Active threads:** ")
                .append(report.jvmThreads().threadCount())
                .append("\n");
        sb.append("- **Peak threads:** ")
                .append(report.jvmThreads().peakThreadCount())
                .append("\n");
        sb.append("- **Daemon threads:** ")
                .append(report.jvmThreads().daemonThreadCount())
                .append("\n");
        sb.append("\n");

        sb.append("## Recording Cache\n\n");
        sb.append("- **Cached recordings:** ")
                .append(report.recordingCache().size())
                .append("\n");
        sb.append("- **Cache hits:** ")
                .append(report.recordingCache().hitCount())
                .append("\n");
        sb.append("- **Cache misses:** ")
                .append(report.recordingCache().missCount())
                .append("\n");
        sb.append("- **Evictions:** ")
                .append(report.recordingCache().evictionCount())
                .append("\n");
        sb.append("- **Total cached bytes:** ")
                .append(SchemaUtil.formatBytes(report.recordingCache().totalCachedBytes()))
                .append("\n");
        sb.append("\n");

        sb.append("## Async Job Queue\n\n");
        sb.append("- **Active jobs:** ")
                .append(report.asyncJobQueue().activeJobs())
                .append("\n");
        sb.append("- **Pending jobs:** ")
                .append(report.asyncJobQueue().pendingJobs())
                .append("\n");
        sb.append("- **Completed jobs:** ")
                .append(report.asyncJobQueue().completedJobs())
                .append("\n");
        sb.append("- **Failed jobs:** ")
                .append(report.asyncJobQueue().failedJobs())
                .append("\n");
        sb.append("- **Total tracked jobs:** ")
                .append(report.asyncJobQueue().totalJobs())
                .append("\n");
        sb.append("- **Recommended poll interval:** ")
                .append(report.asyncJobQueue().recommendedPollSeconds())
                .append("s\n");
        sb.append("\n");

        sb.append(
                "<agent_hint>Server health check complete. Use `jfr_overview` to analyze a recording, or `quick_analysis` for a full diagnostic dashboard.</agent_hint>\n"
        );

        return sb.toString();
    }
}
