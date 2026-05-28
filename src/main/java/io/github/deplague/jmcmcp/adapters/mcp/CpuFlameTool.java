package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.CpuFlameApplicationService;
import io.github.deplague.jmcmcp.domain.model.CallPathEntry;
import io.github.deplague.jmcmcp.domain.model.CpuFlameMethodEntry;
import io.github.deplague.jmcmcp.domain.model.CpuFlameResult;
import io.github.deplague.jmcmcp.domain.model.StateDistributionEntry;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP tool adapter for CPU flame graph data and execution sample profiling.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@ApplicationScoped
public final class CpuFlameTool implements McpTool {

    private static final String NAME = "cpu_flame";

    private final CpuFlameApplicationService applicationService;

    @Inject
    public CpuFlameTool(CpuFlameApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Provide CPU flame graph data including thread states and hottest call paths.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top call paths and methods (default 20)", 20),
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

                        CpuFlameResult result = applicationService.analyze(
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

    private String formatMarkdown(CpuFlameResult result) {
        if (result.totalSamples() == 0) {
            return "# CPU Flame Data\n\nNo execution samples found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# CPU Flame Graph Data\n\n");
        sb.append("- **Total Samples:** ").append(result.totalSamples()).append("\n\n");

        sb.append("## CPU State Distribution\n");
        sb.append("| State | Samples | Percentage |\n|---|---|---|\n");
        for (StateDistributionEntry entry : result.stateDistribution()) {
            sb.append(String.format("| `%s` | %d | %.1f%% |\n",
                    entry.state(), entry.samples(), entry.percentage()));
        }
        sb.append("\n");

        sb.append("## Top CPU Call Paths (Max 10 frames)\n");
        sb.append("| Samples | Percentage | Call Path |\n|---|---|---|\n");
        for (CallPathEntry entry : result.callPaths()) {
            sb.append(String.format("| %d | %.1f%% | `%s` |\n",
                    entry.samples(),
                    entry.percentage(),
                    entry.callPath().replace("\n", "`<br>`")));
        }
        sb.append("\n");

        sb.append("## Hottest Methods (Self Time)\n");
        sb.append("| Method | Samples | Percentage |\n|---|---|---|\n");
        for (CpuFlameMethodEntry entry : result.hotMethods()) {
            sb.append(String.format("| `%s` | %d | %.1f%% |\n",
                    entry.methodName(), entry.samples(), entry.percentage()));
        }

        return sb.toString();
    }
}
