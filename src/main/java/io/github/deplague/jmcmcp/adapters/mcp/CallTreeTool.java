package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.CallTreeApplicationService;
import io.github.deplague.jmcmcp.domain.model.CallTreeNodeEntry;
import io.github.deplague.jmcmcp.domain.model.CallTreeResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public final class CallTreeTool implements McpTool {
    private static final String NAME = "call_tree";

    private final CallTreeApplicationService applicationService;

    @Inject
    public CallTreeTool(CallTreeApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Initialize an interactive call tree from a JFR recording. Returns root-level nodes with a treeId for subsequent expansion. Supports subsystem filtering (cpu, socket, file, lock) and package filtering.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "subsystem", SchemaUtil.stringProp("Subsystem to isolate: cpu, socket, file, lock", List.of("cpu", "socket", "file", "lock")),
                                        "package_filter", SchemaUtil.stringProp("Optional package prefix to filter call tree (e.g., 'com.mycompany')"),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp()
                                ),
                                SchemaUtil.required("jfr_file_path", "subsystem")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String subsystem = SchemaUtil.getString(request.arguments(), "subsystem");
                        String packageFilter = SchemaUtil.getStringOrDefault(request.arguments(), "package_filter", null);
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);

                        CallTreeResult result = applicationService.analyze(filePath, subsystem, packageFilter, startTimeStr, endTimeStr);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder().addTextContent(markdown).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
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
