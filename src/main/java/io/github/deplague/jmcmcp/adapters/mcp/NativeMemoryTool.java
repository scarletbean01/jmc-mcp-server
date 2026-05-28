package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.NativeMemoryApplicationService;
import io.github.deplague.jmcmcp.domain.model.NativeMemoryResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for native memory tracking and library analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class NativeMemoryTool implements McpTool {

    private static final String NAME = "native_memory";

    private final NativeMemoryApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Provide a memory footprint overview including native libraries and direct buffer limits."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp()
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

                        NativeMemoryResult result = appService.analyze(
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
