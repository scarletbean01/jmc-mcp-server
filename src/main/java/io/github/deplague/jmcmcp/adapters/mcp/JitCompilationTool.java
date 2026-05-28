package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.JitCompilationApplicationService;
import io.github.deplague.jmcmcp.domain.model.JitCompilationResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for JIT compilation and deoptimization analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class JitCompilationTool implements McpTool {

    private static final String NAME = "jit_compilation";

    private final JitCompilationApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Analyze JIT compilation and deoptimization events in a JFR recording. "
                                                + "Identifies frequent deoptimizations, compilation failures, and longest-running compilations."
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
                                                                "Number of top methods to return (default 10)",
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

                        JitCompilationResult result = appService.analyze(
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

    private String formatMarkdown(JitCompilationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# JIT Compilation & Deoptimization Analysis\n\n");

        if (!result.hasData()) {
            sb.append("No JIT compilation or deoptimization events found.\n");
            return sb.toString();
        }

        if (!result.longestCompilations().isEmpty() || result.totalCompilations().isPresent()) {
            sb.append("## JIT Compilations\n");
            result.totalCompilations().ifPresent(v -> sb.append(String.format("- **Total Compilations:** %s%n", v)));
            result.avgCompilationDuration().ifPresent(v -> sb.append(String.format("- **Average Duration:** %s%n", v)));
            result.maxCompilationDuration().ifPresent(v -> sb.append(String.format("- **Max Duration:** %s%n", v)));
            sb.append("\n");

            if (!result.longestCompilations().isEmpty()) {
                sb.append("### Longest Compilations\n");
                sb.append("| Method | Duration | Level |\n");
                sb.append("|--------|----------|-------|\n");
                for (var entry : result.longestCompilations()) {
                    sb.append(String.format("| `%s` | %s | %s |%n", entry.method(), entry.duration(), entry.level()));
                }
                sb.append("\n");
            }
        }

        if (result.totalDeoptimizations().isPresent() || !result.topDeoptimizedMethods().isEmpty()) {
            sb.append("## Deoptimizations\n");
            result.totalDeoptimizations().ifPresent(v -> sb.append(String.format("- **Total Deoptimizations:** %s%n", v)));
            sb.append("\n");

            if (!result.topDeoptimizedMethods().isEmpty()) {
                sb.append("### Top Deoptimized Methods\n");
                sb.append("| Method | Count |\n");
                sb.append("|--------|-------|\n");
                for (var entry : result.topDeoptimizedMethods()) {
                    sb.append(String.format("| `%s` | %d |%n", entry.method(), entry.count()));
                }
                sb.append("\n");
            }
        }

        if (!result.compilerFailures().isEmpty()) {
            sb.append("## Compiler Failures\n");
            sb.append("| Method | Message |\n");
            sb.append("|--------|---------|\n");
            for (var entry : result.compilerFailures()) {
                sb.append(String.format("| `%s` | %s |%n", entry.method(), entry.message()));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
