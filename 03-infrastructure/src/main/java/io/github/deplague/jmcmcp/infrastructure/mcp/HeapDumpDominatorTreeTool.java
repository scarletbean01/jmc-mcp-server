package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.HeapDumpAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.DominatorTreeResult;
import io.github.deplague.jmcmcp.domain.model.HeapObjectEntry;
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
public final class HeapDumpDominatorTreeTool {

    private final HeapDumpAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze a heap dump (HPROF) and return the dominator tree — the biggest objects by retained size. These are the objects that would free the most memory if collected.")
    public ToolResponse heapDumpDominatorTree(
            @ToolArg(name = "heap_dump_path", description = "Absolute or relative path to the .hprof heap dump file") String heapDumpPath,
            @ToolArg(name = "top_n", required = false, description = "Number of top dominators to return (default 50)") Integer topN
    ) {
        try {
            DominatorTreeResult result = appService.dominatorTree(
                    heapDumpPath, topN != null ? topN : 50);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(DominatorTreeResult result) {
        if (!result.hasData()) {
            return "# Heap Dump Dominator Tree\n\nNo data available.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("# Heap Dump Dominator Tree\n\n");
        sb.append("- **Total Objects:** ").append(String.format("%,d", result.totalObjects())).append("\n");
        sb.append("- **Total Classes:** ").append(String.format("%,d", result.totalClasses())).append("\n");
        sb.append("- **Total Heap Bytes:** ").append(String.format("%,d", result.totalHeapBytes())).append("\n\n");

        sb.append("| Object ID | Class | Shallow Size | Retained Size | Has Children |\n");
        sb.append("|-----------|-------|-------------:|--------------:|:------------:|\n");

        for (HeapObjectEntry entry : result.topDominators()) {
            sb.append("| `").append(entry.objectId()).append("` | `")
                    .append(entry.className()).append("` | ")
                    .append(String.format("%,d", entry.shallowSize())).append(" | ")
                    .append(String.format("%,d", entry.retainedSize())).append(" | ")
                    .append(entry.hasChildren() ? "Yes" : "No").append(" |\n");
        }

        sb.append("\n<agent_hint>Use `heap_dump_reference_graph` with an objectId from above to trace "
                + "reference paths to GC roots and understand why an object is retained.</agent_hint>\n");
        return sb.toString();
    }
}
