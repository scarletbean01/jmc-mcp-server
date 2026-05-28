package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.LockFlameApplicationService;
import io.github.deplague.jmcmcp.domain.model.LockFlameEntry;
import io.github.deplague.jmcmcp.domain.model.LockFlameResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP tool adapter for lock contention flame graph data.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@ApplicationScoped
public final class LockFlameTool implements McpTool {

    private static final String NAME = "lock_flame";

    private final LockFlameApplicationService applicationService;

    @Inject
    public LockFlameTool(LockFlameApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Provide lock contention flame graph data by aggregating monitor enter/wait by full stack trace.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
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
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 20);
                        SchemaUtil.getBooleanOrDefault(request.arguments(), "async", false);

                        LockFlameResult result = applicationService.analyze(
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

    private String formatMarkdown(LockFlameResult result) {
        if (result.totalNanos() == 0) {
            return "# Lock Flame Data\n\nNo lock/park events found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Lock Flame Graph Data\n\n");
        sb.append("- **Total Lock Duration:** ")
                .append(result.formattedTotalDuration())
                .append("\n\n");

        sb.append("## Top Lock Call Paths (Max 10 frames)\n");
        sb.append("| Lock Duration | Percentage | Call Path |\n|---|---|---|\n");

        for (LockFlameEntry entry : result.entries()) {
            sb.append(String.format("| %s | %.1f%% | `%s` |\n",
                    entry.formattedDuration(),
                    entry.percentage(),
                    entry.callPath().replace("\n", "`<br>`")));
        }

        return sb.toString();
    }
}
