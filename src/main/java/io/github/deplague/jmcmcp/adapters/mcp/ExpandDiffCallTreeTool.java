package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ExpandDiffCallTreeApplicationService;
import io.github.deplague.jmcmcp.domain.model.DiffCallTreeNodeEntry;
import io.github.deplague.jmcmcp.domain.model.ExpandDiffCallTreeResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
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
@ApplicationScoped
public final class ExpandDiffCallTreeTool implements McpTool {

    private static final String NAME = "expand_diff_call_tree";

    private final ExpandDiffCallTreeApplicationService appService;

    @Override
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

                        ExpandDiffCallTreeResult result = appService.expand(treeId, nodeId);

                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
                                .build();
                    } catch (IllegalArgumentException e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    } catch (Exception e) {
                        log.warn("Error in expand_diff_call_tree", e);
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
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
