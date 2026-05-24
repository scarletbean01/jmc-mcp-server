package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService.RecordingOverview;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * MCP tool for JFR overview.
 */
public final class JfrOverviewTool {

    private static final String NAME = "jfr_overview";

    private final JfrAnalysisService service;

    public JfrOverviewTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Provide a high-level overview of a JFR recording, " +
                                "including recording duration and event counts.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp()
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);
        RecordingOverview overview = service.getOverview(filePath);

        StringBuilder sb = new StringBuilder();
        sb.append("# JFR Recording Overview\n\n");
        sb.append("**File:** ").append(overview.filePath()).append("\n");
        sb.append("**Duration:** ").append(String.format("%.2f", overview.durationSeconds())).append(" seconds\n\n");

        sb.append("## Event Summary\n");
        long totalEvents = overview.eventCounts().values().stream().mapToLong(Long::longValue).sum();
        sb.append(String.format("- **Total Events (full file):** %d%n", totalEvents));

        if (startTimeStr != null || endTimeStr != null) {
            sb.append(String.format("- **Filtered Events:** %d%n", io.github.deplague.jmcmcp.jfr.JfrItemUtils.count(events)));
        }

        return sb.toString();
    }
}
