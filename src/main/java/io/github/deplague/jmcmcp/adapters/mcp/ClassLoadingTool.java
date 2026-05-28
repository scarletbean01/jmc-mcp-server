package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ClassLoadingApplicationService;
import io.github.deplague.jmcmcp.domain.model.ClassLoadingResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for analyzing class loading events and statistics.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ClassLoadingTool implements McpTool {

    private static final String NAME = "class_loading";

    private final ClassLoadingApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Analyze class loading events and statistics in a JFR recording. "
                                                + "Identifies longest-loading classes and metaspace pressure."
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
                                                        "top_n",
                                                        SchemaUtil.intProp(
                                                                "Number of top longest loading classes to return (default 10)",
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

                        ClassLoadingResult result = appService.analyze(
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

    private String formatMarkdown(ClassLoadingResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Class Loading Analysis\n\n");

        if (!result.hasAnyData()) {
            sb.append("No class loading events found in the recording.\n");
            return sb.toString();
        }

        if (result.hasClassLoadEvents()) {
            sb.append("## Longest Class Loads\n");
            sb.append("| Class | Duration | Initiating Loader |\n");
            sb.append("|-------|----------|-------------------|\n");
            for (var entry : result.longestLoads()) {
                sb.append(String.format(
                        "| `%s` | %s | %s |%n",
                        entry.className(), entry.duration(), entry.loader()
                ));
            }
            sb.append("\n");
        }

        if (result.hasStatsEvents()) {
            sb.append("## Class Loading Statistics\n");
            result.stats().maxLoadedCount().ifPresent(v ->
                    sb.append(String.format("- **Max Loaded Class Count:** %s%n", v))
            );
            result.stats().maxUnloadedCount().ifPresent(v ->
                    sb.append(String.format("- **Max Unloaded Class Count:** %s%n", v))
            );
            sb.append("\n");
        }

        return sb.toString();
    }
}
