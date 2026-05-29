package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.ExpandCallTreeApplicationService;
import io.github.deplague.jmcmcp.domain.model.ExpandCallTreeChildEntry;
import io.github.deplague.jmcmcp.domain.model.ExpandCallTreeResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for expanding a specific node in a cached call tree.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ExpandCallTreeTool {

    private final ExpandCallTreeApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Expand a specific node in a cached call tree to view its immediate children.")
    public ToolResponse expandCallTree(
            @ToolArg(name = "tree_id", description = "Unique tree ID returned by call_tree") String treeId,
            @ToolArg(name = "node_id", description = "Node ID to expand, e.g., root-0-2-1") String nodeId
    ) {
        try {
            ExpandCallTreeResult result = appService.expand(treeId, nodeId);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(ExpandCallTreeResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Expanded Node: `").append(result.parentNodeId()).append("`\n\n");
        sb.append("- **Method:** `").append(result.parentMethodName()).append("`\n");
        sb.append("- **Self Samples:** ")
                .append(String.format("%,.0f", result.parentSelfSamples())).append("\n");
        sb.append("- **Total Samples:** ")
                .append(String.format("%,.0f", result.parentTotalSamples())).append("\n");
        sb.append("- **Total %:** ")
                .append(String.format("%.2f%%", result.parentTotalPct())).append("\n\n");

        if (result.isLeaf()) {
            sb.append("No further children (leaf node).\n");
        } else {
            sb.append("| Node ID | Method | Self Samples | Total Samples | Self % | Total % | Children? |\n");
            sb.append("|---------|--------|-------------:|--------------:|-------:|--------:|:---------:|\n");

            for (int i = 0; i < result.children().size(); i++) {
                ExpandCallTreeChildEntry child = result.children().get(i);
                String childNodeId = result.parentNodeId() + "-" + i;
                appendNodeRow(sb, childNodeId, child, result.totalSamples());
            }
        }

        sb.append("\n<agent_hint>Continue drilling down with `expand_call_tree` using `tree_id` "
                + "and a `node_id`, or switch to `hot_methods` for a flattened view of the hottest frames.</agent_hint>\n");
        return sb.toString();
    }

    private void appendNodeRow(StringBuilder sb, String nodeId, ExpandCallTreeChildEntry node, double totalSamples) {
        sb.append("| `").append(nodeId).append("` | `")
                .append(node.methodName()).append("` | ")
                .append(String.format("%,.0f", node.selfSamples())).append(" | ")
                .append(String.format("%,.0f", node.totalSamples())).append(" | ")
                .append(String.format("%.2f%%", node.selfPct())).append(" | ")
                .append(String.format("%.2f%%", node.totalPct())).append(" | ")
                .append(node.hasChildren() ? "Yes" : "No").append(" |\n");
    }
}
