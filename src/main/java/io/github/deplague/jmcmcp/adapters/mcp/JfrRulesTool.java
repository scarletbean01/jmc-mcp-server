package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.JfrRulesApplicationService;
import io.github.deplague.jmcmcp.domain.model.JfrRulesResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for JMC rules engine analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class JfrRulesTool implements McpTool {

    private static final String NAME = "jfr_rules";

    private final JfrRulesApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Automatically detects performance issues using JMC's built-in rules engine."
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
                                                        "min_severity",
                                                        SchemaUtil.stringProp(
                                                                "Minimum rule severity threshold to include (OK, INFO, WARNING, IGNORE). Default is WARNING.",
                                                                java.util.List.of("OK", "INFO", "WARNING", "IGNORE")
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
                        String minSevStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "min_severity",
                                "WARNING"
                        );

                        JfrRulesResult result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr,
                                minSevStr
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

    private String formatMarkdown(JfrRulesResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# JMC Automated Bottleneck Detection\n\n");
        sb.append(String.format(
                "Rules with severity >= %s are shown.\n\n",
                result.minSeverity()
        ));

        if (!result.hasData()) {
            sb.append("No issues found exceeding the threshold.\n");
            return sb.toString();
        }

        for (var entry : result.rules()) {
            sb.append(String.format("### %s (%s)%n", entry.name(), entry.severity()));
            sb.append("**Summary:** ").append(entry.summary()).append("\n\n");
            if (entry.explanation() != null) {
                sb.append(entry.explanation()).append("\n\n");
            }
        }

        return sb.toString();
    }
}
