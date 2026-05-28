package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ThreadActivityApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadActivityResult;
import io.github.deplague.jmcmcp.domain.model.ThreadLifecycle;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for thread activity analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ThreadActivityTool implements McpTool {

    private static final String NAME = "thread_activity";

    private final ThreadActivityApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze thread lifecycle, creation/destruction rates, and sleep patterns.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top results (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        ThreadActivityResult result = appService.analyze(filePath, startTimeStr, endTimeStr, topN);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder().addTextContent(markdown).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                })
                .build();
    }

    private String formatMarkdown(ThreadActivityResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Activity Analysis\n\n");

        result.threadStats().ifPresent(stats -> {
            sb.append("## Thread Statistics\n");
            sb.append("- **Peak Thread Count:** ").append(stats.peakCount()).append("\n");
            sb.append("- **Active Count (Min/Avg/Max):** ")
                    .append(stats.activeMin()).append(" / ")
                    .append(stats.activeAvg()).append(" / ")
                    .append(stats.activeMax()).append("\n");
            sb.append("- **Max Daemon Count:** ").append(stats.daemonCount()).append("\n");
            sb.append("- **Total Threads Created (lifetime):** ").append(stats.accumulatedCount()).append("\n\n");
        });

        ThreadLifecycle lifecycle = result.threadLifecycle();
        sb.append("## Thread Lifecycle\n");
        sb.append("- **Threads Started:** ").append(lifecycle.startedCount()).append("\n");
        sb.append("- **Threads Ended:** ").append(lifecycle.endedCount()).append("\n");
        sb.append("- **Net Change:** ").append(lifecycle.netChange()).append("\n\n");

        if (!lifecycle.creationSites().isEmpty()) {
            sb.append("### Top Thread Creation Sites\n");
            sb.append("| Count | Creation Call Site (top 5 frames) |\n");
            sb.append("|-------|----------------------------------|\n");
            for (var site : lifecycle.creationSites()) {
                sb.append("| ").append(site.count()).append(" | `")
                        .append(site.trace().replace("\n", "`<br>`")).append("` |\n");
            }
            sb.append("\n");
        }

        if (!result.sleepHotspots().isEmpty()) {
            sb.append("## Thread Sleep Hotspots\n");
            sb.append("| Total Sleep Time | Count | Call Site (top 5 frames) |\n");
            sb.append("|------------------|-------|--------------------------|\n");
            for (var hotspot : result.sleepHotspots()) {
                sb.append("| ").append(hotspot.totalSleepTime()).append(" | ")
                        .append(hotspot.count()).append(" | `")
                        .append(hotspot.trace().replace("\n", "`<br>`")).append("` |\n");
            }
            sb.append("\n");
        }

        if (result.threadStats().isEmpty() && lifecycle.startedCount() == 0 && result.sleepHotspots().isEmpty()) {
            sb.append("No thread activity events found in this recording.\n");
        }

        return sb.toString();
    }
}
