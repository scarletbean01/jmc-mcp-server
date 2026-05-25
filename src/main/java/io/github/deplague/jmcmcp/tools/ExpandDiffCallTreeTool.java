package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.CallTreeCache;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * MCP tool for expanding a specific node in a cached diff call tree.
 */
public final class ExpandDiffCallTreeTool {

    private static final Logger LOG = LoggerFactory.getLogger(ExpandDiffCallTreeTool.class);
    private static final String NAME = "smart_expand_diff_node";

    private final CallTreeCache callTreeCache;

    public ExpandDiffCallTreeTool(CallTreeCache callTreeCache) {
        this.callTreeCache = callTreeCache;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Expand a specific node in a cached diff call tree to view its children.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "tree_id", SchemaUtil.stringProp("Unique tree ID returned by get_diff_tree"),
                                        "node_id", SchemaUtil.stringProp("Node ID to expand, e.g., root-0-2-1")
                                ),
                                SchemaUtil.required("tree_id", "node_id")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String treeId = SchemaUtil.getString(request.arguments(), "tree_id");
                        String nodeId = SchemaUtil.getString(request.arguments(), "node_id");

                        CallTreeCache.CachedDiffTree cached = callTreeCache.getDiffTree(treeId);
                        if (cached == null) {
                            return CallToolResult.builder()
                                    .addTextContent("Error: Diff tree not found or expired. Please call `get_diff_tree` again.")
                                    .isError(true)
                                    .build();
                        }

                        CallTreeCache.DiffTreeNode targetNode = CallTreeCache.findDiffNode(cached.root(), nodeId);
                        if (targetNode == null) {
                            return CallToolResult.builder()
                                    .addTextContent("Error: Node `" + nodeId + "` not found in diff tree.")
                                    .isError(true)
                                    .build();
                        }

                        String result = buildExpansionOutput(nodeId, targetNode, cached.packageFilter(),
                                cached.baselineTotalSamples(), cached.targetTotalSamples());
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        LOG.warn("Error in expand_diff_node", e);
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String buildExpansionOutput(String parentNodeId, CallTreeCache.DiffTreeNode parentNode,
                                        String packageFilter, double baselineTotal, double targetTotal) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Expanded Diff Node: `").append(parentNodeId).append("`\n\n");
        sb.append("- **Method:** `").append(CallTreeCache.formatMethodName(parentNode)).append("`\n");
        sb.append("- **Baseline Samples:** ").append(String.format("%,.0f", parentNode.baselineCumulative())).append("\n");
        sb.append("- **Target Samples:** ").append(String.format("%,.0f", parentNode.targetCumulative())).append("\n");
        sb.append("- **Delta:** ").append(String.format("%,.0f", parentNode.delta())).append("\n");
        sb.append("- **% Change:** ").append(formatPercentageChange(parentNode.percentageChange())).append("\n");
        sb.append("- **Change Type:** `").append(parentNode.changeType()).append("`\n\n");

        List<CallTreeCache.DiffTreeNode> visibleChildren = CallTreeCache.getVisibleDiffChildren(parentNode, packageFilter);
        if (visibleChildren.isEmpty()) {
            sb.append("No further children (leaf node).\n");
        } else {
            sb.append("| Node ID | Method | Baseline | Target | Δ | Baseline % | Target % | Change |\n");
            sb.append("|---------|--------|---------:|-------:|--:|-----------:|---------:|:------:|\n");

            for (int i = 0; i < visibleChildren.size(); i++) {
                CallTreeCache.DiffTreeNode child = visibleChildren.get(i);
                String childNodeId = parentNodeId + "-" + i;
                DiffCallTreeTool.appendDiffNodeRow(sb, childNodeId, child, baselineTotal, targetTotal);
            }
        }

        sb.append("\n<agent_hint>Continue drilling down with `expand_diff_node`, or use `diff_stack_traces` for a flattened comparison.</agent_hint>\n");
        return sb.toString();
    }

    private static String formatPercentageChange(double pctChange) {
        if (Double.isInfinite(pctChange) || Double.isNaN(pctChange)) {
            return "New";
        }
        return String.format("%.2f%%", pctChange);
    }
}
