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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP tool for initializing an interactive diff call tree between two JFR recordings.
 *
 * <p>Compares baseline and target recordings, builds a merged diff tree, caches it,
 * and returns root-level diff nodes showing added, removed, and changed call paths.</p>
 */
public final class DiffCallTreeTool {

    private static final Logger LOG = LoggerFactory.getLogger(DiffCallTreeTool.class);
    private static final String NAME = "get_diff_tree";

    private final JfrAnalysisService service;
    private final CallTreeCache callTreeCache;

    public DiffCallTreeTool(JfrAnalysisService service) {
        this(service, new CallTreeCache());
    }

    public DiffCallTreeTool(JfrAnalysisService service, CallTreeCache callTreeCache) {
        this.service = service;
        this.callTreeCache = callTreeCache;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Initialize an interactive diff call tree comparing two JFR recordings. " +
                                "Returns root-level diff nodes with a treeId for subsequent expansion. " +
                                "Shows added, removed, and changed call paths.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "baseline_jfr_path", SchemaUtil.stringProp("Path to baseline JFR recording"),
                                        "target_jfr_path", SchemaUtil.stringProp("Path to target JFR recording"),
                                        "subsystem", SchemaUtil.stringProp("Subsystem to isolate: cpu, socket, file, lock", List.of("cpu", "socket", "file", "lock")),
                                        "package_filter", SchemaUtil.stringProp("Optional package prefix to filter call tree (e.g., 'com.mycompany')"),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("baseline_jfr_path", "target_jfr_path", "subsystem")
                        ))
                        .build())
                .callHandler((exchange, request) -> service.execute(NAME, request.arguments(), () -> {
                    String baselinePath = SchemaUtil.getString(request.arguments(), "baseline_jfr_path");
                    String targetPath = SchemaUtil.getString(request.arguments(), "target_jfr_path");
                    String subsystem = SchemaUtil.getString(request.arguments(), "subsystem");
                    String packageFilter = SchemaUtil.getStringOrDefault(request.arguments(), "package_filter", null);
                    return analyze(baselinePath, targetPath, subsystem, packageFilter);
                }))
                .build();
    }

    public String analyze(String baselinePath, String targetPath, String subsystem, String packageFilter) throws IOException {
        IItemCollection baselineEvents = service.loadRecording(baselinePath);
        IItemCollection targetEvents = service.loadRecording(targetPath);

        IItemCollection baselineFiltered = CallTreeTool.filterBySubsystem(baselineEvents, subsystem);
        IItemCollection targetFiltered = CallTreeTool.filterBySubsystem(targetEvents, subsystem);

        if (!baselineFiltered.hasItems() && !targetFiltered.hasItems()) {
            return "# Diff Call Tree\n\nNo events found for subsystem `" + subsystem + "` in either recording.\n";
        }

        StacktraceTreeModel baselineTree = baselineFiltered.hasItems()
                ? new StacktraceTreeModel(baselineFiltered, new FrameSeparator(FrameSeparator.FrameCategorization.METHOD, false), false)
                : null;
        StacktraceTreeModel targetTree = targetFiltered.hasItems()
                ? new StacktraceTreeModel(targetFiltered, new FrameSeparator(FrameSeparator.FrameCategorization.METHOD, false), false)
                : null;

        Node baselineRoot = baselineTree != null ? baselineTree.getRoot() : null;
        Node targetRoot = targetTree != null ? targetTree.getRoot() : null;

        double baselineTotal = baselineRoot != null ? CallTreeCache.computeTotalSamples(baselineRoot) : 0;
        double targetTotal = targetRoot != null ? CallTreeCache.computeTotalSamples(targetRoot) : 0;

        CallTreeCache.DiffTreeNode diffRoot = buildDiffTree(baselineRoot, targetRoot, "root");

        String treeId = callTreeCache.cacheDiffTree(diffRoot, baselinePath, targetPath, subsystem, packageFilter, baselineTotal, targetTotal);

        StringBuilder sb = new StringBuilder();
        sb.append("# Diff Call Tree\n\n");
        sb.append("- **Tree ID:** `").append(treeId).append("`\n");
        sb.append("- **Subsystem:** `").append(subsystem).append("`\n");
        if (packageFilter != null && !packageFilter.isBlank()) {
            sb.append("- **Package Filter:** `").append(packageFilter).append("`\n");
        }
        sb.append("- **Baseline Samples:** ").append(String.format("%,.0f", baselineTotal)).append("\n");
        sb.append("- **Target Samples:** ").append(String.format("%,.0f", targetTotal)).append("\n\n");

        List<CallTreeCache.DiffTreeNode> visibleChildren = CallTreeCache.getVisibleDiffChildren(diffRoot, packageFilter);
        if (visibleChildren.isEmpty()) {
            sb.append("No diff nodes match the current filter criteria.\n");
        } else {
            sb.append("| Node ID | Method | Baseline | Target | Δ | Baseline % | Target % | Change |\n");
            sb.append("|---------|--------|---------:|-------:|--:|-----------:|---------:|:------:|\n");

            for (int i = 0; i < visibleChildren.size(); i++) {
                CallTreeCache.DiffTreeNode child = visibleChildren.get(i);
                String nodeId = "root-" + i;
                appendDiffNodeRow(sb, nodeId, child, baselineTotal, targetTotal);
            }
        }

        sb.append("\n<agent_hint>Use `expand_diff_node` with `tree_id=`").append(treeId)
                .append("` and a `node_id` to drill down. Consider `diff_stack_traces` for a flattened method-level comparison or `compare_recordings` for metric-level analysis.</agent_hint>\n");

        return sb.toString();
    }

    static void appendDiffNodeRow(StringBuilder sb, String nodeId, CallTreeCache.DiffTreeNode node,
                                  double baselineTotal, double targetTotal) {
        String methodName = CallTreeCache.formatMethodName(node);
        double baselinePct = baselineTotal > 0 ? (node.baselineCumulative() / baselineTotal) * 100.0 : 0.0;
        double targetPct = targetTotal > 0 ? (node.targetCumulative() / targetTotal) * 100.0 : 0.0;
        boolean hasChildren = !CallTreeCache.getVisibleDiffChildren(node, null).isEmpty();

        sb.append("| `").append(nodeId).append("` | `")
                .append(methodName).append("` | ")
                .append(String.format("%,.0f", node.baselineCumulative())).append(" | ")
                .append(String.format("%,.0f", node.targetCumulative())).append(" | ")
                .append(String.format("%,.0f", node.delta())).append(" | ")
                .append(String.format("%.2f%%", baselinePct)).append(" | ")
                .append(String.format("%.2f%%", targetPct)).append(" | ")
                .append(node.changeType()).append(hasChildren ? " (+)" : "").append(" |\n");
    }

    static CallTreeCache.DiffTreeNode buildDiffTree(Node baselineNode, Node targetNode, String methodNameFallback) {
        String methodName = resolveMethodName(baselineNode, targetNode, methodNameFallback);
        double baselineWeight = baselineNode != null ? baselineNode.getWeight() : 0;
        double targetWeight = targetNode != null ? targetNode.getWeight() : 0;
        double baselineCumulative = baselineNode != null ? baselineNode.getCumulativeWeight() : 0;
        double targetCumulative = targetNode != null ? targetNode.getCumulativeWeight() : 0;

        String changeType;
        if (baselineCumulative == 0 && targetCumulative > 0) {
            changeType = "added";
        } else if (targetCumulative == 0 && baselineCumulative > 0) {
            changeType = "removed";
        } else if (baselineCumulative > 0) {
            double pctChange = ((targetCumulative - baselineCumulative) / baselineCumulative) * 100.0;
            changeType = Math.abs(pctChange) > 20.0 ? "changed" : "unchanged";
        } else {
            changeType = "unchanged";
        }

        Map<String, Node> baselineChildren = indexChildrenBySignature(baselineNode);
        Map<String, Node> targetChildren = indexChildrenBySignature(targetNode);

        List<String> allSignatures = new ArrayList<>();
        allSignatures.addAll(baselineChildren.keySet());
        for (String sig : targetChildren.keySet()) {
            if (!baselineChildren.containsKey(sig)) {
                allSignatures.add(sig);
            }
        }

        List<CallTreeCache.DiffTreeNode> children = new ArrayList<>();
        for (String sig : allSignatures) {
            Node bChild = baselineChildren.get(sig);
            Node tChild = targetChildren.get(sig);
            children.add(buildDiffTree(bChild, tChild, sig));
        }

        return new CallTreeCache.DiffTreeNode(methodName, baselineWeight, targetWeight,
                baselineCumulative, targetCumulative, changeType, children);
    }

    private static String resolveMethodName(Node baselineNode, Node targetNode, String fallback) {
        if (baselineNode != null && baselineNode.getFrame() != null && baselineNode.getFrame().getMethod() != null) {
            return CallTreeCache.formatMethodName(baselineNode);
        }
        if (targetNode != null && targetNode.getFrame() != null && targetNode.getFrame().getMethod() != null) {
            return CallTreeCache.formatMethodName(targetNode);
        }
        return fallback;
    }

    private static Map<String, Node> indexChildrenBySignature(Node node) {
        Map<String, Node> map = new LinkedHashMap<>();
        if (node == null) {
            return map;
        }
        for (Node child : node.getChildren()) {
            String sig = getFrameSignature(child);
            map.put(sig, child);
        }
        return map;
    }

    private static String getFrameSignature(Node node) {
        if (node == null || node.getFrame() == null || node.getFrame().getMethod() == null) {
            return "";
        }
        var method = node.getFrame().getMethod();
        String type = method.getType() != null ? method.getType().getFullName() : "";
        return type + "." + method.getMethodName();
    }

    public CallTreeCache getCallTreeCache() {
        return callTreeCache;
    }
}
