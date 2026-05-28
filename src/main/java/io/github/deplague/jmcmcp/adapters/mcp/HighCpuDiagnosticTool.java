package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.HighCpuDiagnosticApplicationService;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for high-CPU diagnostic macro analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class HighCpuDiagnosticTool implements McpTool {

    private static final String NAME = "smart_diagnose_high_cpu";

    private final HighCpuDiagnosticApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Macro tool: Automatically orchestrates system health, thread CPU, and hot methods to diagnose high CPU usage.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "package_prefix", SchemaUtil.stringProp("Optional package prefix to filter stack traces (e.g., 'com.mycompany')"),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String packagePrefix = SchemaUtil.getStringOrDefault(request.arguments(), "package_prefix", null);

                        String result = appService.analyze(filePath, startTimeStr, endTimeStr, packagePrefix);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                })
                .build();
    }
}
