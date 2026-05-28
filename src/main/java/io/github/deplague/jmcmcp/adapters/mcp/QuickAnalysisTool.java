package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.QuickAnalysisApplicationService;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for quick analysis dashboard.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class QuickAnalysisTool implements McpTool {

    private static final String NAME = "smart_quick_analysis";

    private final QuickAnalysisApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "One-click overview dashboard that runs the most impactful analyses in a single call "
                                                + "with severity classification. Auto-detects the dominant bottleneck."
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
                                                        "focus",
                                                        SchemaUtil.stringProp(
                                                                "Focus area: cpu, memory, latency, locks, or auto (default)",
                                                                List.of(
                                                                        "cpu",
                                                                        "memory",
                                                                        "latency",
                                                                        "locks",
                                                                        "auto"
                                                                )
                                                        ),
                                                        "async",
                                                        SchemaUtil.boolProp(
                                                                "Run analysis asynchronously and return a job ID",
                                                                false
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
                        String focus = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "focus",
                                "auto"
                        );

                        String result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr,
                                focus
                        );
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
