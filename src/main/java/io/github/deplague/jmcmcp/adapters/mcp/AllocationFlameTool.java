package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.AllocationFlameApplicationService;
import io.github.deplague.jmcmcp.domain.model.AllocationFlameEntry;
import io.github.deplague.jmcmcp.domain.model.AllocationFlameResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP tool adapter for allocation flame graph data.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@ApplicationScoped
public final class AllocationFlameTool implements McpTool {

    private static final String NAME = "allocation_flame";

    private final AllocationFlameApplicationService applicationService;

    @Inject
    public AllocationFlameTool(AllocationFlameApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Provide allocation flame graph data by aggregating object allocations by full stack trace.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "package_prefix", SchemaUtil.stringProp("Optional package prefix to filter stack traces (e.g., 'com.mycompany')"),
                                        "top_n", SchemaUtil.intProp("Number of top call paths (default 20)", 20),
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
                        String packagePrefix = SchemaUtil.getStringOrDefault(request.arguments(), "package_prefix", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 20);
                        SchemaUtil.getBooleanOrDefault(request.arguments(), "async", false);

                        AllocationFlameResult result = applicationService.analyze(
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

    private String formatMarkdown(AllocationFlameResult result) {
        if (result.totalBytes() == 0) {
            return "# Allocation Flame Data\n\nNo allocation events found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Allocation Flame Graph Data\n\n");
        sb.append("- **Total Allocated Bytes:** ")
                .append(result.formattedTotalBytes())
                .append("\n\n");

        sb.append("## Top Allocation Call Paths (Max 10 frames)\n");
        sb.append("| Allocated Bytes | Percentage | Call Path |\n|---|---|---|\n");

        for (AllocationFlameEntry entry : result.entries()) {
            sb.append(String.format("| %s | %.1f%% | `%s` |\n",
                    entry.formattedBytes(),
                    entry.percentage(),
                    entry.callPath().replace("\n", "`<br>`")));
        }

        return sb.toString();
    }
}
