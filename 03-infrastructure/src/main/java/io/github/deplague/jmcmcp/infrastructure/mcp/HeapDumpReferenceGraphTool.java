package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.HeapDumpAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.ReferenceLink;
import io.github.deplague.jmcmcp.domain.model.ReferencePathResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class HeapDumpReferenceGraphTool {

    private final HeapDumpAnalysisApplicationService appService;

    @Blocking
    @Tool(description = "Trace paths from a specific object to GC roots in a heap dump. Essential for diagnosing why an object is not being garbage collected.")

    public ToolResponse heapDumpReferenceGraph(
            @ToolArg(name = "heap_dump_path", description = "Absolute or relative path to the .hprof heap dump file") String heapDumpPath,
            @ToolArg(name = "object_id", description = "The object ID (instanceId) to trace from") long objectId,
            @ToolArg(name = "max_paths", required = false, description = "Maximum number of paths to GC roots to return (default 5)") Integer maxPaths
    ) {
        try {
            ReferencePathResult result = appService.referenceGraph(
                    heapDumpPath, objectId, maxPaths != null ? maxPaths : 5);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(ReferencePathResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Reference Graph to GC Roots\n\n");
        sb.append("- **Target Class:** `").append(result.targetClassName()).append("`\n");
        sb.append("- **Object ID:** `").append(result.targetObjectId()).append("`\n");
        sb.append("- **Retained Size:** ").append(String.format("%,d", result.targetRetainedSize())).append(" bytes\n\n");

        if (result.pathsToGcRoots().isEmpty()) {
            sb.append("No paths to GC roots found. The object may already be unreachable or the heap dump may not include GC root information.\n");
            return sb.toString();
        }

        int pathNum = 1;
        for (ReferenceLink path : result.pathsToGcRoots()) {
            sb.append("## Path ").append(pathNum++).append("\n\n");
            appendLink(sb, path, 0);
            sb.append("\n");
        }

        sb.append("<agent_hint>Look for unexpected references or static fields holding objects longer than intended. "
                + "Common leak patterns include static collections, listeners not being deregistered, or thread-local accumulations.</agent_hint>\n");
        return sb.toString();
    }

    private void appendLink(StringBuilder sb, ReferenceLink link, int depth) {
        String indent = "  ".repeat(depth);
        sb.append(indent).append("- **").append(link.referenceType()).append("**");
        if (!link.fieldName().isBlank()) {
            sb.append(" (`").append(link.fieldName()).append("`)");
        }
        sb.append(": `").append(link.className()).append("` #").append(link.objectId());
        sb.append(" — retained ").append(String.format("%,d", link.retainedSize())).append(" bytes\n");
        for (ReferenceLink child : link.children()) {
            appendLink(sb, child, depth + 1);
        }
    }
}
