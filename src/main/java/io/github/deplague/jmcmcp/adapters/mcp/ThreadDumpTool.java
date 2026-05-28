package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ThreadDumpApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadDumpResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for extracting periodic thread dumps from JFR recordings.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ThreadDumpTool implements McpTool {

    private static final String NAME = "thread_dumps";

    private final ThreadDumpApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Extract periodic thread dumps from a JFR recording. "
                                                + "Returns the text of thread dumps captured at various points during the recording."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.stringProp("Path to the .jfr recording file"),
                                                        "max_dumps",
                                                        SchemaUtil.intProp(
                                                                "Maximum number of thread dumps to return (default 5)",
                                                                5
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
                        int maxDumps = SchemaUtil.getIntOrDefault(
                                request.arguments(),
                                "max_dumps",
                                5
                        );

                        ThreadDumpResult result = appService.analyze(filePath, maxDumps);
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

    private String formatMarkdown(ThreadDumpResult result) {
        if (!result.hasData()) {
            return "No periodic thread dumps found in this recording. "
                    + "Ensure 'Thread Dump' events were enabled in the recording settings.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Dumps\n\n");

        for (var dump : result.dumps()) {
            sb.append("## Dump at ").append(dump.timestamp()).append("\n\n");
            sb.append("```\n");
            sb.append(dump.content());
            sb.append("\n```\n\n");
        }

        return sb.toString();
    }
}
