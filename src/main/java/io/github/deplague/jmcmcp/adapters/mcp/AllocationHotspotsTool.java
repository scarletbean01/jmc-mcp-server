package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.AllocationHotspotsApplicationService;
import io.github.deplague.jmcmcp.domain.model.AllocationHotspotEntry;
import io.github.deplague.jmcmcp.domain.model.AllocationHotspotsResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP tool adapter for memory allocation hotspot analysis.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@ApplicationScoped
public final class AllocationHotspotsTool implements McpTool {

    private static final String NAME = "allocation_hotspots";

    private final AllocationHotspotsApplicationService applicationService;

    @Inject
    public AllocationHotspotsTool(AllocationHotspotsApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Find memory allocation hotspots and allocation sites in a JFR recording. " +
                                "Reports top allocating classes and their call paths.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "package_prefix", SchemaUtil.stringProp("Optional package prefix to filter stack traces (e.g., 'com.mycompany')"),
                                        "top_n", SchemaUtil.intProp("Number of top allocation sites to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String packagePrefix = SchemaUtil.getStringOrDefault(request.arguments(), "package_prefix", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        AllocationHotspotsResult result = applicationService.analyze(
                                filePath, startTimeStr, endTimeStr, packagePrefix, topN);

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

    private String formatMarkdown(AllocationHotspotsResult result) {
        if (!result.hasData()) {
            return "# Allocation Hotspots\n\nNo allocation events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Allocation Hotspots\n\n");
        sb.append("| Total Allocated | Class | Allocation Site (top 5 frames) |\n");
        sb.append("|-----------------|-------|--------------------------------|\n");

        for (AllocationHotspotEntry entry : result.entries()) {
            sb.append("| ").append(entry.formattedBytes()).append(" | ");
            sb.append("`").append(entry.className()).append("` | ");
            sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`")).append("` |\n");
        }

        return sb.toString();
    }
}
