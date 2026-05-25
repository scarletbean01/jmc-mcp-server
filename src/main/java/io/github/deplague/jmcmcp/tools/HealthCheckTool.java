package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * MCP tool that returns the server's health status, cache statistics,
 * JVM metrics, and async job queue state.
 */
public final class HealthCheckTool {

    private static final String NAME = "health_check";

    private final JfrRecordingCache recordingCache;
    private final AsyncJobService asyncJobService;
    private final Instant startedAt;

    public HealthCheckTool(JfrRecordingCache recordingCache, AsyncJobService asyncJobService) {
        this.recordingCache = recordingCache;
        this.asyncJobService = asyncJobService;
        this.startedAt = Instant.now();
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Return the server's health status, cache statistics, JVM metrics, and async job queue state.")
                        .inputSchema(SchemaUtil.objectSchema(Map.of()))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String report = buildReport();
                        return CallToolResult.builder()
                                .addTextContent(report)
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

    private String buildReport() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryMXBean.getNonHeapMemoryUsage();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        Runtime runtime = Runtime.getRuntime();

        long uptimeMs = runtimeMXBean.getUptime();
        Duration uptime = Duration.ofMillis(uptimeMs);

        double heapUsedPct = heapUsage.getMax() > 0
                ? (heapUsage.getUsed() * 100.0 / heapUsage.getMax())
                : 0.0;

        StringBuilder sb = new StringBuilder();
        sb.append("# Server Health Check\n\n");

        // Overall status
        String status = determineStatus(heapUsedPct);
        sb.append("## Status\n\n");
        sb.append("- **Overall:** `").append(status).append("`\n");
        sb.append("- **Uptime:** ").append(formatDuration(uptime)).append("\n");
        sb.append("- **Server Start:** ").append(startedAt).append("\n");
        sb.append("- **JVM:** ").append(System.getProperty("java.vm.name"))
          .append(" ").append(System.getProperty("java.version")).append("\n");
        sb.append("\n");

        // JVM Memory
        sb.append("## JVM Memory\n\n");
        sb.append("| Region | Used | Committed | Max | Utilization |\n");
        sb.append("|--------|------|-----------|-----|-------------|\n");
        sb.append(String.format("| Heap | %s | %s | %s | %.1f%% |\n",
                formatBytes(heapUsage.getUsed()),
                formatBytes(heapUsage.getCommitted()),
                heapUsage.getMax() > 0 ? formatBytes(heapUsage.getMax()) : "unlimited",
                heapUsedPct));
        sb.append(String.format("| Non-Heap | %s | %s | %s | — |\n",
                formatBytes(nonHeapUsage.getUsed()),
                formatBytes(nonHeapUsage.getCommitted()),
                nonHeapUsage.getMax() > 0 ? formatBytes(nonHeapUsage.getMax()) : "unlimited"));
        sb.append(String.format("| Total Available | — | — | %s | — |\n",
                formatBytes(runtime.maxMemory())));
        sb.append(String.format("| Free (within committed) | %s | — | — | — |\n",
                formatBytes(runtime.freeMemory())));
        sb.append("\n");

        // Threads
        sb.append("## JVM Threads\n\n");
        sb.append("- **Active threads:** ").append(ManagementFactory.getThreadMXBean().getThreadCount()).append("\n");
        sb.append("- **Peak threads:** ").append(ManagementFactory.getThreadMXBean().getPeakThreadCount()).append("\n");
        sb.append("- **Daemon threads:** ").append(ManagementFactory.getThreadMXBean().getDaemonThreadCount()).append("\n");
        sb.append("\n");

        // Recording Cache
        sb.append("## Recording Cache\n\n");
        sb.append("- **Cached recordings:** ").append(recordingCache.size()).append("\n");
        sb.append("- **Cache hits:** ").append(recordingCache.getHitCount()).append("\n");
        sb.append("- **Cache misses:** ").append(recordingCache.getMissCount()).append("\n");
        sb.append("- **Evictions:** ").append(recordingCache.getEvictionCount()).append("\n");
        sb.append("- **Total cached bytes:** ").append(formatBytes(recordingCache.getTotalCachedBytes())).append("\n");
        sb.append("\n");

        // Async Jobs
        sb.append("## Async Job Queue\n\n");
        sb.append("- **Active jobs:** ").append(asyncJobService.activeJobs()).append("\n");
        sb.append("- **Completed jobs:** ").append(asyncJobService.completedJobs()).append("\n");
        sb.append("- **Failed jobs:** ").append(asyncJobService.failedJobs()).append("\n");
        sb.append("- **Total tracked jobs:** ").append(asyncJobService.totalJobs()).append("\n");
        sb.append("\n");

        sb.append("<agent_hint>Server health check complete. Use `jfr_overview` to analyze a recording, or `quick_analysis` for a full diagnostic dashboard.</agent_hint>\n");

        return sb.toString();
    }

    private String determineStatus(double heapUsedPct) {
        if (heapUsedPct > 95) {
            return "CRITICAL — heap usage > 95%";
        }
        if (heapUsedPct > 85) {
            return "DEGRADED — heap usage > 85%";
        }
        return "HEALTHY";
    }

    private static String formatDuration(Duration d) {
        long days = d.toDays();
        long hours = d.toHoursPart();
        long minutes = d.toMinutesPart();
        long seconds = d.toSecondsPart();
        if (days > 0) {
            return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
        }
        if (hours > 0) {
            return String.format("%dh %02dm %02ds", hours, minutes, seconds);
        }
        if (minutes > 0) {
            return String.format("%dm %02ds", minutes, seconds);
        }
        return String.format("%ds", seconds);
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
