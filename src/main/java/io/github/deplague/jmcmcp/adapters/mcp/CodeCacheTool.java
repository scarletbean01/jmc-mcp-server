package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.CodeCacheApplicationService;
import io.github.deplague.jmcmcp.domain.model.CodeCacheResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for Code Cache usage and JIT statistics analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class CodeCacheTool implements McpTool {

    private static final String NAME = "code_cache";

    private final CodeCacheApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description("Analyze Code Cache usage and JIT compiler statistics.")
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

                        CodeCacheResult result = appService.analyze(
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

    private String formatMarkdown(CodeCacheResult result) {
        if (!result.hasAnyData()) {
            return "# Code Cache & JIT Analysis\n\nNo code cache or compiler statistics found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Code Cache & JIT Analysis\n\n");

        if (result.hasCodeCacheEvents()) {
            sb.append("## Code Cache Segment Usage\n\n");
            sb.append("| Segment | Entry Count | Method Count | Capacity | Unallocated | Utilization |\n");
            sb.append("|---------|-------------|--------------|----------|-------------|-------------|\n");
            for (var seg : result.segments()) {
                String utilStr = String.format("%.2f%%", seg.utilizationPercent());
                if (seg.utilizationPercent() > 90) {
                    utilStr = "**" + utilStr + "** (Warning)";
                }
                sb.append("| ")
                        .append(seg.name()).append(" | ")
                        .append(seg.entryCount()).append(" | ")
                        .append(seg.methodCount()).append(" | ")
                        .append(seg.reservedCapacity()).append(" | ")
                        .append(seg.unallocatedCapacity()).append(" | ")
                        .append(utilStr).append(" |\n");
            }
            sb.append("\n");
        }

        if (result.hasCompilerEvents()) {
            sb.append("## Compilation Statistics\n\n");
            var stats = result.compilerStats();
            stats.totalCompilations().ifPresent(v ->
                    sb.append("- **Total Compilations:** ").append(v).append("\n")
            );
            stats.peakCompilationTime().ifPresent(v ->
                    sb.append("- **Peak Compilation Time:** ").append(v).append("\n")
            );
            stats.totalCompilationTime().ifPresent(v ->
                    sb.append("- **Total Compilation Time:** ").append(v).append("\n")
            );
            stats.averageCompilationTimeMs().ifPresent(v ->
                    sb.append(String.format("- **Average Compilation Time:** %.2f ms%n", v))
            );
            sb.append("\n");
        }

        return sb.toString();
    }
}
