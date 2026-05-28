package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.SystemPropertiesApplicationService;
import io.github.deplague.jmcmcp.domain.model.SystemPropertiesResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for extracting system properties from a JFR recording.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class SystemPropertiesTool implements McpTool {

    private static final String NAME = "system_properties";

    private final SystemPropertiesApplicationService applicationService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description("List the JVM and OS system properties captured in a JFR recording.")
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "filter",
                                                        SchemaUtil.stringProp(
                                                                "Optional filter for property names (e.g., 'java.vm')"
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
                        String filter = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "filter",
                                null
                        );

                        SystemPropertiesResult result = applicationService.analyze(
                                filePath,
                                filter
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

    private String formatMarkdown(SystemPropertiesResult result) {
        if (!result.hasProperties()) {
            return "# System Properties\n\nNo system properties found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# System Properties\n\n");
        sb.append("| Property | Value |\n");
        sb.append("|----------|-------|\n");

        for (var entry : result.entries()) {
            sb.append(String.format("| %s | %s |%n", entry.key(), entry.value()));
        }

        return sb.toString();
    }
}
