package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.JfrOverviewApplicationService;
import io.github.deplague.jmcmcp.domain.model.JfrOverviewResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for JFR recording overview.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class JfrOverviewTool implements McpTool {

    private static final String NAME = "jfr_overview";

    private final JfrOverviewApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Provide a high-level overview of a JFR recording, "
                                                + "including recording duration and event counts."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                                        "start_time", SchemaUtil.startTimeProp(),
                                                        "end_time", SchemaUtil.endTimeProp()
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

                        JfrOverviewResult result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr
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

    private String formatMarkdown(JfrOverviewResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# JFR Recording Overview\n\n");
        sb.append("**File:** ").append(result.filePath()).append("\n");
        sb.append("**Duration:** ")
                .append(String.format("%.2f", result.durationSeconds()))
                .append(" seconds\n\n");

        sb.append("## Event Summary\n");
        sb.append(String.format(
                "- **Total Events (full file):** %d%n",
                result.totalEvents()
        ));

        if (result.filteredEvents() != null) {
            sb.append(String.format(
                    "- **Filtered Events:** %d%n",
                    result.filteredEvents()
            ));
        }

        return sb.toString();
    }
}
