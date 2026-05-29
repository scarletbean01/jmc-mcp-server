package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.IoHotspotsApplicationService;
import io.github.deplague.jmcmcp.domain.model.IoHotspotsResult;
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
 * MCP tool adapter for I/O hotspots analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class IoHotspotsTool {

    private final IoHotspotsApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Identify slow and frequent I/O operations by path/host with call-site breakdowns.")
    public ToolResponse ioHotspots(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "io_type", required = false, description = "I/O type filter: file, socket, or all (default)") String ioType,
            @ToolArg(name = "top_n", required = false, description = "Number of top results (default 10)") Integer topN
    ) {
        try {
            IoHotspotsResult result = appService.analyze(jfrFilePath, startTime, endTime, ioType != null ? ioType : "all", topN != null ? topN : 10);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(IoHotspotsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# I/O Hotspots Analysis\n\n");

        if (!result.fileEndpoints().isEmpty()) {
            sb.append("## File I/O Hotspots\n\n");
            sb.append("| Duration (Max) | Count | Bytes | Target | Call Site (top 5 frames) |\n");
            sb.append("|----------------|-------|-------|--------|--------------------------|\n");
            for (var entry : result.fileEndpoints()) {
                sb.append("| ").append(entry.maxDuration()).append(" | ");
                sb.append(entry.count()).append(" | ");
                sb.append(entry.totalBytes()).append(" | ");
                sb.append("`").append(entry.target()).append("` | ");
                sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`")).append("` |\n");
            }
            sb.append("\n");
        } else if (result.hasFileData()) {
            sb.append("## File I/O Hotspots\n\nNo file I/O events found.\n\n");
        }

        if (!result.socketEndpoints().isEmpty()) {
            sb.append("## Socket I/O Hotspots\n\n");
            sb.append("| Duration (Max) | Count | Bytes | Target | Call Site (top 5 frames) |\n");
            sb.append("|----------------|-------|-------|--------|--------------------------|\n");
            for (var entry : result.socketEndpoints()) {
                sb.append("| ").append(entry.maxDuration()).append(" | ");
                sb.append(entry.count()).append(" | ");
                sb.append(entry.totalBytes()).append(" | ");
                sb.append("`").append(entry.target()).append("` | ");
                sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`")).append("` |\n");
            }
            sb.append("\n");
        } else if (result.hasSocketData()) {
            sb.append("## Socket I/O Hotspots\n\nNo socket I/O events found.\n\n");
        }

        sb.append("## I/O Latency Percentiles\n\n");
        sb.append("| Operation | P50 | P95 | P99 | Max |\n");
        sb.append("|-----------|-----|-----|-----|-----|\n");
        for (var p : result.percentiles()) {
            sb.append("| ").append(p.operation()).append(" | ");
            sb.append(p.p50()).append(" | ");
            sb.append(p.p95()).append(" | ");
            sb.append(p.p99()).append(" | ");
            sb.append(p.max()).append(" |\n");
        }
        sb.append("\n");

        sb.append("<agent_hint>Top I/O hotspot identified. Consider `correlate` to see which hot methods and locks are associated with this endpoint, or `network_analysis` for connection-level details.</agent_hint>\n");

        return sb.toString();
    }
}
