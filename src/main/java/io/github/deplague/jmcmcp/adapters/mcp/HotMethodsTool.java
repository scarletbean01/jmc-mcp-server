package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.HotMethodsApplicationService;
import io.github.deplague.jmcmcp.domain.model.HotMethodEntry;
import io.github.deplague.jmcmcp.domain.model.HotMethodsResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.Map;

/**
 * MCP tool adapter for identifying hot methods from execution samples.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@ApplicationScoped
public final class HotMethodsTool implements McpTool {

    private static final String NAME = "hot_methods";

    private final HotMethodsApplicationService applicationService;

    @Inject
    public HotMethodsTool(HotMethodsApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Identify hot methods and call paths in a JFR recording based on execution samples."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp(),
                                                        "thread_name",
                                                        SchemaUtil.stringProp(
                                                                "Optional thread name to filter execution samples by"
                                                        ),
                                                        "package_prefix",
                                                        SchemaUtil.stringProp(
                                                                "Optional package prefix to filter stack traces (e.g., 'com.mycompany')"
                                                        ),
                                                        "top_n",
                                                        SchemaUtil.intProp(
                                                                "Number of top hot methods to return (default 10)",
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
                        String threadName = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "thread_name",
                                null
                        );
                        String packagePrefix = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "package_prefix",
                                null
                        );
                        int topN = SchemaUtil.getIntOrDefault(
                                request.arguments(),
                                "top_n",
                                10
                        );

                        HotMethodsResult result = applicationService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr,
                                threadName,
                                packagePrefix,
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

    private String formatMarkdown(HotMethodsResult result) {
        if (!result.hasResults()) {
            return "# Hot Methods\n\nNo execution samples found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Hot Methods & Call Paths\n\n");
        sb.append("| Samples | Stack Trace (top 5 frames) |\n");
        sb.append("|---------|----------------------------|\n");

        for (HotMethodEntry entry : result.entries()) {
            sb.append("| ").append(entry.sampleCount()).append(" | ");
            sb.append("`")
                    .append(entry.stackTrace().replace("\n", "`<br>`"))
                    .append("` |\n");
        }

        String topMethod = result.topMethod() != null
                ? result.topMethod()
                : "unknown";
        sb.append("\n<agent_hint>Top hot method is `")
                .append(topMethod)
                .append(
                        "`. Consider `thread_cpu` to see which threads consume the most CPU, `stack_trace_search` with `class_pattern` to find all events involving this class, or `correlate` to see if this method is associated with lock contention or I/O.</agent_hint>\n"
                );

        return sb.toString();
    }
}
