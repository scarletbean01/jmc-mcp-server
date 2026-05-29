package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.CallTreeApplicationService;
import io.github.deplague.jmcmcp.domain.model.CallTreeNodeEntry;
import io.github.deplague.jmcmcp.domain.model.CallTreeResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@HandleToolError
@ApplicationScoped
public final class CallTreeTool {

    private final CallTreeApplicationService applicationService;

    @Inject
    public CallTreeTool(CallTreeApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RunOnVirtualThread
    @Tool(description = "Initialize an interactive call tree from a JFR recording. Returns root-level nodes with a treeId for subsequent expansion. Supports subsystem filtering (cpu, socket, file, lock) and package filtering.")
    public ToolResponse callTree(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "subsystem", description = "Subsystem to isolate: cpu, socket, file, lock") String subsystem,
            @ToolArg(name = "package_filter", required = false, description = "Optional package prefix to filter call tree (e.g., 'com.mycompany')") String packageFilter,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime
    ) {
        try {
            CallTreeResult result = applicationService.analyze(jfrFilePath, subsystem, packageFilter, startTime, endTime);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(CallTreeResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Call Tree\n\n");

        if (!result.hasResults()) {
            sb.append("No events found for subsystem `").append(result.subsystem()).append("` in the recording.\n");
            return sb.toString();
        }

        sb.append("- **Tree ID:** `").append(result.treeId()).append("`\n");
        sb.append("- **Subsystem:** `").append(result.subsystem()).append("`\n");
        if (result.packageFilter() != null && !result.packageFilter().isBlank()) {
            sb.append("- **Package Filter:** `").append(result.packageFilter()).append("`\n");
        }
        sb.append("- **Total Samples:** ").append(String.format("%,.0f", result.totalSamples())).append("\n\n");

        if (result.nodes().isEmpty()) {
            sb.append("No nodes match the current filter criteria.\n");
        } else {
            sb.append("| Node ID | Method | Self Samples | Total Samples | Self % | Total % | Children? |\n");
            sb.append("|---------|--------|-------------:|--------------:|-------:|--------:|:---------:|\n");

            for (int i = 0; i < result.nodes().size(); i++) {
                CallTreeNodeEntry node = result.nodes().get(i);
                String nodeId = "root-" + i;
                appendNodeRow(sb, nodeId, node, result.totalSamples());
            }
        }

        sb.append("\n<agent_hint>Use `expand_call_tree` with `tree_id=`").append(result.treeId()).append("` and a `nodeId` to drill down into the call tree. Consider `hot_methods` for a flattened hotspot view or `stack_trace_search` to find specific classes.</agent_hint>\n");

        return sb.toString();
    }

    private void appendNodeRow(StringBuilder sb, String nodeId, CallTreeNodeEntry node, double totalSamples) {
        double selfPct = totalSamples > 0 ? (node.selfSamples() / totalSamples) * 100.0 : 0.0;
        double totalPct = totalSamples > 0 ? (node.totalSamples() / totalSamples) * 100.0 : 0.0;

        sb.append("| `").append(nodeId).append("` | `")
                .append(node.methodName()).append("` | ")
                .append(String.format("%,.0f", node.selfSamples())).append(" | ")
                .append(String.format("%,.0f", node.totalSamples())).append(" | ")
                .append(String.format("%.2f%%", selfPct)).append(" | ")
                .append(String.format("%.2f%%", totalPct)).append(" | ")
                .append(node.hasChildren() ? "Yes" : "No").append(" |\n");
    }
}
