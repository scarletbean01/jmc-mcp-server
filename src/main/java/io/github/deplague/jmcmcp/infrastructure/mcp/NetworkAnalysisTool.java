package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.NetworkAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.NetworkAnalysisResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for network socket analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class NetworkAnalysisTool {

    private final NetworkAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze socket connection hotspots in a JFR recording. Reports per-host:port connection latency, read/write throughput, and failure tracking.")
    public ToolResponse networkAnalysis(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top results per section (default 10)") Integer topN
    ) {
        try {
            NetworkAnalysisResult result = appService.analyze(jfrFilePath, startTime, endTime, topN != null ? topN : 10);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(NetworkAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Network Analysis\n\n");

        if (!result.hasData()) {
            sb.append("No socket events found in the recording.\n");
            return sb.toString();
        }

        sb.append("## Connection Summary\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append("| Total Connections | ").append(result.connectCount()).append(" |\n");
        sb.append("| Total Reads | ").append(result.readCount()).append(" |\n");
        sb.append("| Total Writes | ").append(result.writeCount()).append(" |\n");

        result.avgConnectDuration().ifPresent(v ->
                sb.append("| Avg Connect Duration | ").append(v).append(" |\n")
        );
        result.maxConnectDuration().ifPresent(v ->
                sb.append("| Max Connect Duration | ").append(v).append(" |\n")
        );
        result.p95ConnectDuration().ifPresent(v ->
                sb.append("| P95 Connect Duration | ").append(v).append(" |\n")
        );
        sb.append("\n");

        if (!result.topConnections().isEmpty()) {
            sb.append("## Top Hosts by Connection Count\n\n");
            sb.append("| Host:Port | Connections | Avg Duration | Max Duration |\n");
            sb.append("|-----------|-------------|--------------|--------------|\n");
            for (var entry : result.topConnections()) {
                sb.append("| `").append(entry.hostPort()).append("` | ");
                sb.append(entry.count()).append(" | ");
                sb.append(entry.avgDuration()).append(" | ");
                sb.append(entry.maxDuration()).append(" |\n");
            }
            sb.append("\n");
        }

        if (!result.topReads().isEmpty()) {
            sb.append("## Top Hosts by Read Throughput\n\n");
            sb.append("| Host:Port | Reads | Total Bytes | Avg Read Duration |\n");
            sb.append("|-----------|-------|-------------|-------------------|\n");
            for (var entry : result.topReads()) {
                sb.append("| `").append(entry.hostPort()).append("` | ");
                sb.append(entry.count()).append(" | ");
                sb.append(entry.totalBytes()).append(" | ");
                sb.append(entry.avgDuration()).append(" |\n");
            }
            sb.append("\n");
        }

        if (!result.topWrites().isEmpty()) {
            sb.append("## Top Hosts by Write Throughput\n\n");
            sb.append("| Host:Port | Writes | Total Bytes | Avg Write Duration |\n");
            sb.append("|-----------|--------|-------------|--------------------|\n");
            for (var entry : result.topWrites()) {
                sb.append("| `").append(entry.hostPort()).append("` | ");
                sb.append(entry.count()).append(" | ");
                sb.append(entry.totalBytes()).append(" | ");
                sb.append(entry.avgDuration()).append(" |\n");
            }
            sb.append("\n");
        }

        sb.append("## Connection Latency Percentiles\n\n");
        sb.append("| Operation | P50 | P95 | P99 | Max |\n");
        sb.append("|-----------|-----|-----|-----|-----|\n");
        for (var p : result.latencyPercentiles()) {
            sb.append("| ").append(p.operation()).append(" | ");
            sb.append(p.p50()).append(" | ");
            sb.append(p.p95()).append(" | ");
            sb.append(p.p99()).append(" | ");
            sb.append(p.max()).append(" |\n");
        }
        sb.append("\n");

        sb.append(
                "<agent_hint>Slow connections detected. Consider `request_waterfall` with a specific thread name to trace the full request path, or `io_hotspots` for file I/O analysis.</agent_hint>\n"
        );

        return sb.toString();
    }
}
