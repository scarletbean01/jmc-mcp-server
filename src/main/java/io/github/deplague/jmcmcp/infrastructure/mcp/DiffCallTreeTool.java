package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.DiffCallTreeApplicationService;
import io.github.deplague.jmcmcp.domain.model.DiffCallTreeNodeEntry;
import io.github.deplague.jmcmcp.domain.model.DiffCallTreeResult;
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
 * MCP tool adapter for initializing an interactive diff call tree between two JFR recordings.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class DiffCallTreeTool {

    private final DiffCallTreeApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Initialize an interactive diff call tree comparing two JFR recordings. Returns root-level diff nodes with a treeId for subsequent expansion. Shows added, removed, and changed call paths.")
    public ToolResponse diffCallTree(
            @ToolArg(name = "baseline_jfr_path", description = "Absolute or relative path to the baseline .jfr recording file") String baselineJfrPath,
            @ToolArg(name = "target_jfr_path", description = "Absolute or relative path to the target .jfr recording file") String targetJfrPath,
            @ToolArg(name = "subsystem", description = "Subsystem to isolate: cpu, socket, file, lock") String subsystem,
            @ToolArg(name = "package_filter", required = false, description = "Optional package prefix to filter call tree (e.g., 'com.mycompany')") String packageFilter
    ) {
        try {
            DiffCallTreeResult result = appService.analyze(
                    baselineJfrPath, targetJfrPath, subsystem, packageFilter);

            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(DiffCallTreeResult result) {
        if (!result.hasNodes()) {
            return "# Diff Call Tree\n\nNo events found for subsystem `"
                    + result.subsystem() + "` in either recording.\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Diff Call Tree\n\n");
        sb.append("- **Tree ID:** `").append(result.treeId()).append("`\n");
        sb.append("- **Subsystem:** `").append(result.subsystem()).append("`\n");
        if (result.packageFilter() != null && !result.packageFilter().isBlank()) {
            sb.append("- **Package Filter:** `").append(result.packageFilter()).append("`\n");
        }
        sb.append("- **Baseline Samples:** ").append(String.format("%,.0f", result.baselineTotal())).append("\n");
        sb.append("- **Target Samples:** ").append(String.format("%,.0f", result.targetTotal())).append("\n\n");

        if (result.nodes().isEmpty()) {
            sb.append("No diff nodes match the current filter criteria.\n");
        } else {
            sb.append("| Node ID | Method | Baseline | Target | Δ | Baseline % | Target % | Change |\n");
            sb.append("|---------|--------|---------:|-------:|--:|-----------:|---------:|:------:|\n");

            for (int i = 0; i < result.nodes().size(); i++) {
                DiffCallTreeNodeEntry child = result.nodes().get(i);
                String nodeId = "root-" + i;
                appendDiffNodeRow(sb, nodeId, child, result.baselineTotal(), result.targetTotal());
            }
        }

        sb.append("\n<agent_hint>Use `expand_diff_node` with `tree_id=`").append(result.treeId())
                .append("` and a `node_id` to drill down. Consider `diff_stack_traces` "
                + "for a flattened method-level comparison or `compare_recordings` for metric-level analysis.</agent_hint>\n");

        return sb.toString();
    }

    public static void appendDiffNodeRow(StringBuilder sb, String nodeId, DiffCallTreeNodeEntry node,
                                         double baselineTotal, double targetTotal) {
        sb.append("| `").append(nodeId).append("` | `")
                .append(node.methodName()).append("` | ")
                .append(String.format("%,.0f", node.baselineCumulative())).append(" | ")
                .append(String.format("%,.0f", node.targetCumulative())).append(" | ")
                .append(String.format("%,.0f", node.delta())).append(" | ")
                .append(String.format("%.2f%%", node.baselinePct())).append(" | ")
                .append(String.format("%.2f%%", node.targetPct())).append(" | ")
                .append(node.changeType()).append(node.hasChildren() ? " (+)" : "").append(" |\n");
    }
}
