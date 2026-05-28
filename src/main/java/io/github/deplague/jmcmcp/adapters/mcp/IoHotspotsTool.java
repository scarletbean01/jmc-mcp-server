package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.IoHotspotsApplicationService;
import io.github.deplague.jmcmcp.domain.model.IoHotspotsResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * MCP tool adapter for I/O hotspots analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class IoHotspotsTool implements McpTool {

    private static final String NAME = "io_hotspots";

    private final IoHotspotsApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Identify slow and frequent I/O operations by path/host with call-site breakdowns.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "io_type", SchemaUtil.stringProp(
                                                "I/O type filter",
                                                List.of("file", "socket", "all")
                                        ),
                                        "top_n", SchemaUtil.intProp(
                                                "Number of top results (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String ioType = SchemaUtil.getStringOrDefault(request.arguments(), "io_type", "all");
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        IoHotspotsResult result = appService.analyze(filePath, startTimeStr, endTimeStr, ioType, topN);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder().addTextContent(markdown).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                })
                .build();
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
