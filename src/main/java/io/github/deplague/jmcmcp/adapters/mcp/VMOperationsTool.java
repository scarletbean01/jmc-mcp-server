package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.VmOperationsApplicationService;
import io.github.deplague.jmcmcp.domain.model.VmOperationsResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for analyzing VM operations and safepoint events.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class VMOperationsTool implements McpTool {

    private static final String NAME = "vm_operations";

    private final VmOperationsApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Analyze VM operations and safepoint events in a JFR recording. "
                                                + "Reports longest VM operations and total STW time."
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
                                                                "Number of top VM operations to return (default 10)",
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

                        VmOperationsResult result = appService.analyze(filePath, startTimeStr, endTimeStr, topN);
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

    private String formatMarkdown(VmOperationsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# VM Operations Analysis\n\n");

        if (!result.hasOperations()) {
            sb.append("No VM operation events found in the recording.\n");
            return sb.toString();
        }

        sb.append("## Summary\n");
        sb.append(String.format("- **Total VM Ops Duration:** %s%n", result.totalDuration()));
        sb.append(String.format("- **Max VM Op Duration:** %s%n", result.maxDuration()));
        sb.append(String.format("- **Avg VM Op Duration:** %s%n", result.avgDuration()));
        sb.append("\n");

        sb.append("## Longest VM Operations\n");
        sb.append("| Operation | Duration | Caller |\n");
        sb.append("|-----------|----------|--------|\n");
        for (var entry : result.operations()) {
            sb.append(String.format(
                    "| %s | %s | %s |%n",
                    entry.operation(), entry.duration(), entry.caller()
            ));
        }

        return sb.toString();
    }
}
