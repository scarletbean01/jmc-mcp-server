package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.HeapDumpAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramEntry;
import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class HeapDumpClassHistogramTool {

    private final HeapDumpAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze a heap dump (HPROF) and return a class histogram showing the top classes by retained size, instance count, and shallow size.")
    public ToolResponse heapDumpClassHistogram(
            @ToolArg(name = "heap_dump_path", description = "Absolute or relative path to the .hprof heap dump file") String heapDumpPath,
            @ToolArg(name = "top_n", required = false, description = "Number of top classes to return (default 50)") Integer topN
    ) {
        try {
            HeapDumpClassHistogramResult result = appService.classHistogram(
                    heapDumpPath, topN != null ? topN : 50);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(HeapDumpClassHistogramResult result) {
        if (!result.hasData()) {
            return "# Heap Dump Class Histogram\n\nNo data available.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("# Heap Dump Class Histogram\n\n");
        sb.append("- **Total Instances:** ").append(String.format("%,d", result.totalInstances())).append("\n");
        sb.append("- **Total Bytes:** ").append(String.format("%,d", result.totalBytes())).append("\n\n");

        sb.append("| Rank | Class | Instances | Shallow Size | Retained Size |\n");
        sb.append("|------|-------|----------:|-------------:|--------------:|\n");

        int rank = 1;
        for (HeapDumpClassHistogramEntry entry : result.entries()) {
            sb.append("| ").append(rank++).append(" | `")
                    .append(entry.className()).append("` | ")
                    .append(String.format("%,d", entry.instanceCount())).append(" | ")
                    .append(String.format("%,d", entry.totalShallowSize())).append(" | ")
                    .append(String.format("%,d", entry.totalRetainedSize())).append(" |\n");
        }

        sb.append("\n<agent_hint>Use `heap_dump_dominator_tree` to find the biggest individual objects, "
                + "or `heap_dump_reference_graph` with an objectId to trace GC roots.</agent_hint>\n");
        return sb.toString();
    }
}
