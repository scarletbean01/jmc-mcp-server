package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.CodeCacheApplicationService;
import io.github.deplague.jmcmcp.domain.model.CodeCacheResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for Code Cache usage and JIT statistics analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class CodeCacheTool {

    private final CodeCacheApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze Code Cache usage and JIT compiler statistics.")
    public ToolResponse codeCache(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime
    ) {
        try {
            CodeCacheResult result = appService.analyze(
                    jfrFilePath,
                    startTime,
                    endTime
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
