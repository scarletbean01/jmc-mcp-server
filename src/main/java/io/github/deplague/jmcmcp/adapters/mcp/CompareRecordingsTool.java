package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.CompareRecordingsApplicationService;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for comprehensive A/B comparison of two JFR recordings.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class CompareRecordingsTool implements McpTool {

    private static final String NAME = "compare_recordings";

    private final CompareRecordingsApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Perform a comprehensive expert-level A/B comparison of two JFR recordings. "
                                                + "Compares CPU, GC (including P95/P99), Memory, I/O, Safepoints, and JVM internals."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "baseline_jfr_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "target_jfr_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "async",
                                                        SchemaUtil.boolProp(
                                                                "Run analysis asynchronously and return a job ID",
                                                                false
                                                        )
                                                ),
                                                SchemaUtil.required(
                                                        "baseline_jfr_path",
                                                        "target_jfr_path"
                                                )
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String baselinePath = SchemaUtil.getString(
                                request.arguments(),
                                "baseline_jfr_path"
                        );
                        String targetPath = SchemaUtil.getString(
                                request.arguments(),
                                "target_jfr_path"
                        );

                        String result = appService.analyze(baselinePath, targetPath);
                        return CallToolResult.builder()
                                .addTextContent(result)
                                .isError(false)
                                .build();
                    } catch (IllegalArgumentException e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
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
}
