package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ThreadPoolAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadPoolAnalysisResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for thread pool analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ThreadPoolAnalysisTool implements McpTool {

    private static final String NAME = "thread_pool_analysis";

    private final ThreadPoolAnalysisApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze thread pool utilization and detect thread pool starvation. " +
                                "Groups threads by name prefix to identify pools, computes active ratios, " +
                                "and detects saturation patterns.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp(
                                                "Number of top pools to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        ThreadPoolAnalysisResult result = appService.analyze(
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

    private String formatMarkdown(ThreadPoolAnalysisResult result) {
        if (!result.hasData()) {
            return "# Thread Pool Analysis\n\nInsufficient thread activity events found for pool analysis.";
        }

        if (result.pools().isEmpty()) {
            return "# Thread Pool Analysis\n\nNo identifiable thread pools found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Pool Analysis\n\n");

        sb.append("## Thread Pool Summary\n\n");
        sb.append("| Pool Prefix | Thread Count | CPU Samples | Blocked Time | Blocked Count | Active Ratio | Status |\n");
        sb.append("|-------------|-------------|-------------|-------------|---------------|-------------|--------|\n");

        for (var pool : result.pools()) {
            sb.append(String.format(
                    "| `%s` | %d | %d | %s | %d | %.1f%% | %s |\n",
                    pool.poolPrefix(),
                    pool.threadCount(),
                    pool.cpuSamples(),
                    SchemaUtil.formatDuration(pool.blockedTimeMs()),
                    pool.blockedCount(),
                    pool.activeRatio(),
                    pool.status()));
        }
        sb.append("\n");

        if (!result.warnings().isEmpty()) {
            sb.append("## ⚠️ Warnings\n\n");
            for (String warning : result.warnings()) {
                sb.append("- ").append(warning).append("\n");
            }
            sb.append("\n");
        }

        if (!result.recommendations().isEmpty()) {
            sb.append("## Recommendations\n\n");
            for (int i = 0; i < result.recommendations().size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, result.recommendations().get(i)));
            }
            sb.append("\n");
        }

        sb.append("## Blocking Breakdown by Type\n\n");
        sb.append("| Pool Prefix | Monitor Enter | Monitor Wait | Thread Park | Thread Sleep |\n");
        sb.append("|-------------|---------------|-------------|-------------|-------------|\n");
        for (var pool : result.pools()) {
            sb.append(String.format(
                    "| `%s` | %d | %d | %d | %d |\n",
                    pool.poolPrefix(),
                    pool.monitorEnterCount(),
                    pool.monitorWaitCount(),
                    pool.parkCount(),
                    pool.sleepCount()));
        }

        return sb.toString();
    }
}
