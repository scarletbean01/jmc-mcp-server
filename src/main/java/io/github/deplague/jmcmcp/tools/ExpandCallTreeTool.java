package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.CallTreeCache;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * MCP tool for expanding a specific node in a cached call tree.
 */
public final class ExpandCallTreeTool {

    private static final Logger LOG = LoggerFactory.getLogger(ExpandCallTreeTool.class);
    private static final String NAME = "smart_expand_node";

    private final CallTreeCache callTreeCache;

    public ExpandCallTreeTool(CallTreeCache callTreeCache) {
        this.callTreeCache = callTreeCache;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Expand a specific node in a cached call tree to view its immediate children.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "tree_id", SchemaUtil.stringProp("Unique tree ID returned by get_call_tree"),
                                        "node_id", SchemaUtil.stringProp("Node ID to expand, e.g., root-0-2-1")
                                ),
                                SchemaUtil.required("tree_id", "node_id")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String treeId = SchemaUtil.getString(request.arguments(), "tree_id");
                        String nodeId = SchemaUtil.getString(request.arguments(), "node_id");

                        CallTreeCache.CachedTree cached = callTreeCache.getTree(treeId);
                        if (cached == null) {
                            return CallToolResult.builder()
                                    .addTextContent("Error: Tree not found or expired. Please call `get_call_tree` again.")
                                    .isError(true)
                                    .build();
                        }

                        Node root = cached.tree().getRoot();
                        Node targetNode = CallTreeCache.findNode(root, nodeId);
                        if (targetNode == null) {
                            return CallToolResult.builder()
                                    .addTextContent("Error: Node `" + nodeId + "` not found in tree.")
                                    .isError(true)
                                    .build();
                        }

                        String result = buildExpansionOutput(nodeId, targetNode, cached.packageFilter(), cached.totalSamples());
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        LOG.warn("Error in expand_node", e);
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String buildExpansionOutput(String parentNodeId, Node parentNode, String packageFilter, double totalSamples) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Expanded Node: `").append(parentNodeId).append("`\n\n");
        sb.append("- **Method:** `").append(CallTreeCache.formatMethodName(parentNode)).append("`\n");
        sb.append("- **Self Samples:** ").append(String.format("%,.0f", parentNode.getWeight())).append("\n");
        sb.append("- **Total Samples:** ").append(String.format("%,.0f", parentNode.getCumulativeWeight())).append("\n");
        sb.append("- **Total %:** ").append(String.format("%.2f%%", totalSamples > 0 ? (parentNode.getCumulativeWeight() / totalSamples) * 100.0 : 0.0)).append("\n\n");

        List<Node> visibleChildren = CallTreeCache.getVisibleChildren(parentNode, packageFilter);
        if (visibleChildren.isEmpty()) {
            sb.append("No further children (leaf node).\n");
        } else {
            sb.append("| Node ID | Method | Self Samples | Total Samples | Self % | Total % | Children? |\n");
            sb.append("|---------|--------|-------------:|--------------:|-------:|--------:|:---------:|\n");

            for (int i = 0; i < visibleChildren.size(); i++) {
                Node child = visibleChildren.get(i);
                String childNodeId = parentNodeId + "-" + i;
                CallTreeTool.appendNodeRow(sb, childNodeId, child, totalSamples);
            }
        }

        sb.append("\n<agent_hint>Continue drilling down with `expand_node` using `tree_id` and a `node_id`, or switch to `hot_methods` for a flattened view of the hottest frames.</agent_hint>\n");
        return sb.toString();
    }
}
