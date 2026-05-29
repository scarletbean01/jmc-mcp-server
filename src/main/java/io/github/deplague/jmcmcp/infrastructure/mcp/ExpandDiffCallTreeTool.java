package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.ExpandDiffCallTreeApplicationService;
import io.github.deplague.jmcmcp.domain.model.DiffCallTreeNodeEntry;
import io.github.deplague.jmcmcp.domain.model.ExpandDiffCallTreeResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for expanding a specific node in a cached diff call tree.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ExpandDiffCallTreeTool {

    private final ExpandDiffCallTreeApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Expand a specific node in a cached diff call tree to view its children.")
    public ToolResponse expandDiffCallTree(
            @ToolArg(name = "tree_id", description = "Unique tree ID returned by diff_call_tree") String treeId,
            @ToolArg(name = "node_id", description = "Node ID to expand, e.g., root-0-2-1") String nodeId
    ) {
        try {
            ExpandDiffCallTreeResult result = appService.expand(treeId, nodeId);

            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(ExpandDiffCallTreeResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Expanded Diff Node: `").append(result.parentNodeId()).append("`\n\n");
        sb.append("- **Method:** `").append(result.parentMethodName()).append("`\n");
        sb.append("- **Baseline Samples:** ").append(String.format("%,.0f", result.parentBaselineCumulative())).append("\n");
        sb.append("- **Target Samples:** ").append(String.format("%,.0f", result.parentTargetCumulative())).append("\n");
        sb.append("- **Delta:** ").append(String.format("%,.0f", result.parentDelta())).append("\n");
        sb.append("- **% Change:** ").append(formatPercentageChange(result.parentPercentageChange())).append("\n");
        sb.append("- **Change Type:** `").append(result.parentChangeType()).append("`\n\n");

        if (result.children().isEmpty()) {
            sb.append("No further children (leaf node).\n");
        } else {
            sb.append("| Node ID | Method | Baseline | Target | Δ | Baseline % | Target % | Change |\n");
            sb.append("|---------|--------|---------:|-------:|--:|-----------:|---------:|:------:|\n");

            for (int i = 0; i < result.children().size(); i++) {
                DiffCallTreeNodeEntry child = result.children().get(i);
                String childNodeId = result.parentNodeId() + "-" + i;
                DiffCallTreeTool.appendDiffNodeRow(sb, childNodeId, child,
                        result.baselineTotal(), result.targetTotal());
            }
        }

        sb.append("\n<agent_hint>Continue drilling down with `expand_diff_call_tree`, or use `diff_stack_traces` for a flattened comparison.</agent_hint>\n");
        return sb.toString();
    }

    private static String formatPercentageChange(double pctChange) {
        if (Double.isInfinite(pctChange) || Double.isNaN(pctChange)) {
            return "New";
        }
        return String.format("%.2f%%", pctChange);
    }
}
