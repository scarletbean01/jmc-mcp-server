package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.CallTreeCache;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.flightrecorder.stacktrace.FrameSeparator;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.Node;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.StacktraceTreeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * MCP tool for initializing an interactive call tree from a JFR recording.
 *
 * <p>Builds a {@link StacktraceTreeModel}, caches it under a unique tree ID,
 * and returns the root-level nodes with sample counts and percentages.</p>
 */
public final class CallTreeTool {

    private static final Logger LOG = LoggerFactory.getLogger(CallTreeTool.class);
    private static final String NAME = "get_call_tree";

    private final JfrAnalysisService service;
    private final CallTreeCache callTreeCache;

    public CallTreeTool(JfrAnalysisService service) {
        this(service, new CallTreeCache());
    }

    public CallTreeTool(JfrAnalysisService service, CallTreeCache callTreeCache) {
        this.service = service;
        this.callTreeCache = callTreeCache;
    }

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

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, subsystem, packageFilter, startTimeStr, endTimeStr);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        LOG.warn("Error in get_call_tree", e);
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    public String analyze(String filePath, String subsystem, String packageFilter,
                          String startTimeStr, String endTimeStr) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);
        IItemCollection filtered = filterBySubsystem(events, subsystem);

        if (!filtered.hasItems()) {
            return "# Call Tree\n\nNo events found for subsystem `" + subsystem + "` in the recording.\n";
        }

        StacktraceTreeModel tree = new StacktraceTreeModel(
                filtered,
                new FrameSeparator(FrameSeparator.FrameCategorization.METHOD, false),
                false
        );

        String treeId = callTreeCache.cacheTree(tree, filePath, subsystem, packageFilter);
        Node root = tree.getRoot();
        double totalSamples = CallTreeCache.computeTotalSamples(root);

        StringBuilder sb = new StringBuilder();
        sb.append("# Call Tree\n\n");
        sb.append("- **Tree ID:** `").append(treeId).append("`\n");
        sb.append("- **Subsystem:** `").append(subsystem).append("`\n");
        if (packageFilter != null && !packageFilter.isBlank()) {
            sb.append("- **Package Filter:** `").append(packageFilter).append("`\n");
        }
        sb.append("- **Total Samples:** ").append(String.format("%,.0f", totalSamples)).append("\n\n");

        List<Node> visibleChildren = CallTreeCache.getVisibleChildren(root, packageFilter);
        if (visibleChildren.isEmpty()) {
            sb.append("No nodes match the current filter criteria.\n");
        } else {
            sb.append("| Node ID | Method | Self Samples | Total Samples | Self % | Total % | Children? |\n");
            sb.append("|---------|--------|-------------:|--------------:|-------:|--------:|:---------:|\n");

            for (int i = 0; i < visibleChildren.size(); i++) {
                Node child = visibleChildren.get(i);
                String nodeId = "root-" + i;
                appendNodeRow(sb, nodeId, child, totalSamples);
            }
        }

        sb.append("\n<agent_hint>Use `expand_node` with `treeId=`").append(treeId).append("` and a `nodeId` to drill down into the call tree. Consider `hot_methods` for a flattened hotspot view or `stack_trace_search` to find specific classes.</agent_hint>\n");

        return sb.toString();
    }

    static void appendNodeRow(StringBuilder sb, String nodeId, Node node, double totalSamples) {
        String methodName = CallTreeCache.formatMethodName(node);
        double self = node.getWeight();
        double cumulative = node.getCumulativeWeight();
        double selfPct = totalSamples > 0 ? (self / totalSamples) * 100.0 : 0.0;
        double totalPct = totalSamples > 0 ? (cumulative / totalSamples) * 100.0 : 0.0;
        boolean hasChildren = !CallTreeCache.getVisibleChildren(node, null).isEmpty();

        sb.append("| `").append(nodeId).append("` | `")
                .append(methodName).append("` | ")
                .append(String.format("%,.0f", self)).append(" | ")
                .append(String.format("%,.0f", cumulative)).append(" | ")
                .append(String.format("%.2f%%", selfPct)).append(" | ")
                .append(String.format("%.2f%%", totalPct)).append(" | ")
                .append(hasChildren ? "Yes" : "No").append(" |\n");
    }

    static IItemCollection filterBySubsystem(IItemCollection events, String subsystem) {
        return switch (subsystem.toLowerCase()) {
            case "cpu" -> events.apply(ItemFilters.type("jdk.ExecutionSample"));
            case "socket" -> events.apply(ItemFilters.or(
                    ItemFilters.type("jdk.SocketRead"),
                    ItemFilters.type("jdk.SocketWrite")
            ));
            case "file" -> events.apply(ItemFilters.or(
                    ItemFilters.type("jdk.FileRead"),
                    ItemFilters.type("jdk.FileWrite")
            ));
            case "lock" -> events.apply(ItemFilters.type("jdk.JavaMonitorEnter"));
            default -> events;
        };
    }

    public CallTreeCache getCallTreeCache() {
        return callTreeCache;
    }
}
