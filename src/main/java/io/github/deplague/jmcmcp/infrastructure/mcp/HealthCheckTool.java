package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.FormatUtil;
import io.github.deplague.jmcmcp.application.service.HealthCheckApplicationService;
import io.github.deplague.jmcmcp.domain.model.HealthCheckReport;
import io.github.deplague.jmcmcp.infrastructure.mcp.util.MarkdownBuffer;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for server health check.
 * Delegates data gathering to the application layer and formats results as Markdown.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class HealthCheckTool {

    private final HealthCheckApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Return the server's health status, cache statistics, JVM metrics, and async job queue state.")
    public ToolResponse healthCheck() {
        try {
            HealthCheckReport report = appService.check();
            String markdown = formatMarkdown(report);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(HealthCheckReport report) {
        MarkdownBuffer sb = MarkdownBuffer.get();
        sb.line("# Server Health Check").nl();

        sb.line("## Status").nl();
        sb.append("- **Overall:** `").append(report.status()).append("`\n");
        sb.append("- **Uptime:** ").append(report.uptime()).append('\n');
        sb.append("- **Server Start:** ").append(report.serverStart()).append('\n');
        sb.append("- **JVM:** ")
                .append(report.jvmName())
                .append(" ")
                .append(report.javaVersion())
                .append('\n');
        sb.nl();

        sb.line("## JVM Memory").nl();
        sb.line("| Region | Used | Committed | Max | Utilization |");
        sb.line("|--------|------|-----------|-----|-------------|");
        sb.appendf(
                "| Heap | %s | %s | %s | %.1f%% |\n",
                FormatUtil.formatBytes(report.jvmMemory().heapUsed()),
                FormatUtil.formatBytes(report.jvmMemory().heapCommitted()),
                report.jvmMemory().heapMax() > 0
                        ? FormatUtil.formatBytes(report.jvmMemory().heapMax())
                        : "unlimited",
                report.heapUsedPct()
        );
        sb.appendf(
                "| Non-Heap | %s | %s | %s | — |\n",
                FormatUtil.formatBytes(report.jvmMemory().nonHeapUsed()),
                FormatUtil.formatBytes(report.jvmMemory().nonHeapCommitted()),
                report.jvmMemory().nonHeapMax() > 0
                        ? FormatUtil.formatBytes(report.jvmMemory().nonHeapMax())
                        : "unlimited"
        );
        sb.appendf(
                "| Total Available | — | — | %s | — |\n",
                FormatUtil.formatBytes(report.jvmMemory().totalMaxMemory())
        );
        sb.appendf(
                "| Free (within committed) | %s | — | — | — |\n",
                FormatUtil.formatBytes(report.jvmMemory().freeMemory())
        );
        sb.nl();

        sb.line("## JVM Threads").nl();
        sb.append("- **Active threads:** ")
                .append(report.jvmThreads().threadCount())
                .append('\n');
        sb.append("- **Peak threads:** ")
                .append(report.jvmThreads().peakThreadCount())
                .append('\n');
        sb.append("- **Daemon threads:** ")
                .append(report.jvmThreads().daemonThreadCount())
                .append('\n');
        sb.nl();

        sb.line("## Recording Cache").nl();
        sb.append("- **Cached recordings:** ")
                .append(report.recordingCache().size())
                .append('\n');
        sb.append("- **Cache hits:** ")
                .append(report.recordingCache().hitCount())
                .append('\n');
        sb.append("- **Cache misses:** ")
                .append(report.recordingCache().missCount())
                .append('\n');
        sb.append("- **Evictions:** ")
                .append(report.recordingCache().evictionCount())
                .append('\n');
        sb.append("- **Total cached bytes:** ")
                .append(FormatUtil.formatBytes(report.recordingCache().totalCachedBytes()))
                .append('\n');
        sb.nl();

        sb.append(
                "<agent_hint>Server health check complete. Use `jfr_overview` to analyze a recording, or `quick_analysis` for a full diagnostic dashboard.</agent_hint>\n"
        );

        return sb.toStringAndReset();
    }
}
