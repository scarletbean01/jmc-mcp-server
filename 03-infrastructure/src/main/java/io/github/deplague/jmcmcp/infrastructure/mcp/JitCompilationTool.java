package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.JitCompilationApplicationService;
import io.github.deplague.jmcmcp.domain.model.JitCompilationResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for JIT compilation and deoptimization analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class JitCompilationTool {

    private final JitCompilationApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.JIT_COMPILATION,
            title = "JIT Compilation Analysis",
            description = "Analyze JIT compilation and deoptimization events in a JFR recording. Identifies frequent deoptimizations, compilation failures, and longest-running compilations.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse jitCompilation(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                JitCompilationResult result = appService.analyze(
                            jfrFilePath,
                            startTime,
                            endTime,
                            topN != null ? topN : 10
                    );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
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
