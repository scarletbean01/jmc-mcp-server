package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ThreadContentionApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadContentionResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for thread contention analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ThreadContentionTool implements McpTool {

    private static final String NAME = "thread_contention";

    private final ThreadContentionApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze thread contention in a JFR recording. " +
                                "Identifies top monitor lock contentions and wait locations.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top contention sites to return (default 10)", 10)
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

                        ThreadContentionResult result = appService.analyze(filePath, startTimeStr, endTimeStr, topN);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder().addTextContent(markdown).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                })
                .build();
    }

    private String formatMarkdown(ThreadContentionResult result) {
        if (!result.hasData()) {
            return "# Thread Contention Analysis\n\nNo monitor contention or wait events found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Contention Analysis\n\n");
        sb.append("| Total Duration | Monitor Class | Contention Site (top 5 frames) |\n");
        sb.append("|----------------|---------------|--------------------------------|\n");

        for (var entry : result.topContentions()) {
            sb.append("| ").append(entry.totalDuration()).append(" | ");
            sb.append("`").append(entry.monitorClass()).append("` | ");
            sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`")).append("` |\n");
        }

        sb.append("\n<agent_hint>Lock `").append(result.topLock()).append("` has ")
                .append(result.topDuration()).append(" total contention. Consider `correlate` to see if I/O is performed under this lock, or `request_waterfall` with the contending thread name to trace the full request path.</agent_hint>\n");

        return sb.toString();
    }
}
