package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.BlockingSummaryApplicationService;
import io.github.deplague.jmcmcp.domain.model.BlockingSummaryResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for blocking event aggregation.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class BlockingSummaryTool implements McpTool {

    private static final String NAME = "blocking_summary";

    private final BlockingSummaryApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description("Aggregate all blocking events (monitors, parking, sleeping, I/O) per thread.")
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp(),
                                                        "top_n",
                                                        SchemaUtil.intProp(
                                                                "Number of top threads/reasons to return (default 10)",
                                                                10
                                                        )
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(
                                request.arguments(),
                                "jfr_file_path"
                        );
                        String startTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "start_time",
                                null
                        );
                        String endTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "end_time",
                                null
                        );
                        int topN = SchemaUtil.getIntOrDefault(
                                request.arguments(),
                                "top_n",
                                10
                        );

                        BlockingSummaryResult result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr,
                                topN
                        );
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

    private String formatMarkdown(BlockingSummaryResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Blocking Summary\n\n");

        if (!result.hasData()) {
            sb.append("No blocking events found in the recording.");
            return sb.toString();
        }

        sb.append("## Blocking Overview\n\n");
        sb.append("- **Total Blocked Time:** ").append(result.totalBlockedTime()).append("\n");
        sb.append("- **Total Blocked Events:** ").append(result.totalBlockedEvents()).append("\n\n");

        if (!result.perThreadBlocking().isEmpty()) {
            sb.append("## Per-Thread Blocking Summary\n\n");
            sb.append("| Thread Name | Total Blocked Time | Event Count | Top Category |\n");
            sb.append("|-------------|--------------------|-------------|--------------|\n");
            for (var entry : result.perThreadBlocking()) {
                sb.append("| ").append(entry.threadName()).append(" | ")
                        .append(entry.totalBlockedTime()).append(" | ")
                        .append(entry.eventCount()).append(" | ")
                        .append(entry.topCategory()).append(" |\n");
            }
            sb.append("\n");
        }

        if (!result.topBlockingReasons().isEmpty()) {
            sb.append("## Top Blocking Reasons\n\n");
            sb.append("| Category | Detail | Total Time | Count |\n");
            sb.append("|----------|--------|------------|-------|\n");
            for (var entry : result.topBlockingReasons()) {
                sb.append("| ").append(entry.category()).append(" | `")
                        .append(entry.detail()).append("` | ")
                        .append(entry.totalTime()).append(" | ")
                        .append(entry.count()).append(" |\n");
            }
            sb.append("\n");
        }

        if (!result.categoryDistribution().isEmpty()) {
            sb.append("## Blocking Time Distribution\n\n");
            sb.append("| Category | Total Time | Event Count | Avg Duration |\n");
            sb.append("|----------|------------|-------------|--------------|\n");
            for (var entry : result.categoryDistribution()) {
                sb.append("| ").append(entry.category()).append(" | ")
                        .append(entry.totalTime()).append(" | ")
                        .append(entry.count()).append(" | ")
                        .append(entry.avgDuration()).append(" |\n");
            }
        }

        if (result.monitorContentionDetected()) {
            sb.append(
                    "\n<agent_hint>Monitor lock contention detected. Use 'thread_contention' or 'lock_analysis' to investigate which threads are holding the locks and causing these blockages.</agent_hint>\n"
            );
        }

        return sb.toString();
    }
}
