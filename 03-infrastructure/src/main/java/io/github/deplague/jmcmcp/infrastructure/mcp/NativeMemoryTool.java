package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.NativeMemoryApplicationService;
import io.github.deplague.jmcmcp.domain.model.NativeMemoryResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for native memory tracking and library analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class NativeMemoryTool {

    private final NativeMemoryApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.NATIVE_MEMORY,
            title = "Native Memory Tracking",
            description = "Provide a memory footprint overview including native libraries and direct buffer limits.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse nativeMemory(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                NativeMemoryResult result = appService.analyze(
                            jfrFilePath,
                            startTime,
                            endTime
                    );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(NativeMemoryResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Native Memory Analysis\n\n");

        sb.append("## Memory Configuration\n");
        sb.append("- **Loaded Native Libraries:** ").append(result.libraryCount()).append("\n");
        result.maxHeapSize().ifPresent(v ->
                sb.append("- **Max Heap Size Observed:** ").append(v).append("\n")
        );
        sb.append("\n");

        if (!result.memoryProperties().isEmpty()) {
            sb.append("## Memory-Related System Properties\n");
            sb.append("| Key | Value |\n|---|---|\n");
            result.memoryProperties().forEach((k, v) ->
                    sb.append("| `").append(k).append("` | `").append(v).append("` |\n")
            );
            sb.append("\n");
        }

        if (result.libraryCount() > 0) {
            sb.append("## Loaded Native Libraries (Top 50)\n");
            sb.append("| Library Name | Base Path |\n|---|---|\n");
            for (var lib : result.libraries()) {
                sb.append("| `").append(lib.name()).append("` | `").append(lib.path()).append("` |\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
