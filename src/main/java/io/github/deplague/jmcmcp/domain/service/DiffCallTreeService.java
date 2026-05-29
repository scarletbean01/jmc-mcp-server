package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.DiffCallTreeData;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.flightrecorder.stacktrace.FrameSeparator;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.Node;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.StacktraceTreeModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.CallTreeCache.*;
import static java.lang.Math.abs;
import static org.openjdk.jmc.common.item.ItemFilters.or;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.flightrecorder.stacktrace.FrameSeparator.FrameCategorization.METHOD;

/**
 * Pure domain service for building a diff call tree between two JFR recordings.
 * Contains no MCP-specific or UI formatting logic.
 */
@ApplicationScoped
public final class DiffCallTreeService {

    public DiffCallTreeData analyze(IItemCollection baselineEvents, IItemCollection targetEvents, String subsystem) {
        IItemCollection baselineFiltered = filterBySubsystem(baselineEvents, subsystem);
        IItemCollection targetFiltered = filterBySubsystem(targetEvents, subsystem);

        if (!baselineFiltered.hasItems() && !targetFiltered.hasItems()) {
            return new DiffCallTreeData(null, 0, 0, false);
        }

        StacktraceTreeModel baselineTree = baselineFiltered.hasItems()
                ? new StacktraceTreeModel(baselineFiltered, new FrameSeparator(METHOD, false), false)
                : null;
        StacktraceTreeModel targetTree = targetFiltered.hasItems()
                ? new StacktraceTreeModel(targetFiltered, new FrameSeparator(METHOD, false), false)
                : null;

        Node baselineRoot = baselineTree != null ? baselineTree.getRoot() : null;
        Node targetRoot = targetTree != null ? targetTree.getRoot() : null;

        double baselineTotal = baselineRoot != null ? computeTotalSamples(baselineRoot) : 0;
        double targetTotal = targetRoot != null ? computeTotalSamples(targetRoot) : 0;

        DiffTreeNode diffRoot = buildDiffTree(baselineRoot, targetRoot, "root");

        return new DiffCallTreeData(diffRoot, baselineTotal, targetTotal, true);
    }

    public static IItemCollection filterBySubsystem(IItemCollection events, String subsystem) {
        return switch (subsystem.toLowerCase()) {
            case "cpu" -> events.apply(type("jdk.ExecutionSample"));
            case "socket" -> events.apply(or(
                    type("jdk.SocketRead"),
                    type("jdk.SocketWrite")
            ));
            case "file" -> events.apply(or(
                    type("jdk.FileRead"),
                    type("jdk.FileWrite")
            ));
            case "lock" -> events.apply(type("jdk.JavaMonitorEnter"));
            default -> events;
        };
    }

    public static DiffTreeNode buildDiffTree(Node baselineNode, Node targetNode, String methodNameFallback) {
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
            changeType = abs(pctChange) > 20.0 ? "changed" : "unchanged";
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

        List<DiffTreeNode> children = new ArrayList<>();
        for (String sig : allSignatures) {
            Node bChild = baselineChildren.get(sig);
            Node tChild = targetChildren.get(sig);
            children.add(buildDiffTree(bChild, tChild, sig));
        }

        return new DiffTreeNode(methodName, baselineWeight, targetWeight,
                baselineCumulative, targetCumulative, changeType, children);
    }

    private static String resolveMethodName(Node baselineNode, Node targetNode, String fallback) {
        if (baselineNode != null && baselineNode.getFrame() != null && baselineNode.getFrame().getMethod() != null) {
            return formatMethodName(baselineNode);
        }
        if (targetNode != null && targetNode.getFrame() != null && targetNode.getFrame().getMethod() != null) {
            return formatMethodName(targetNode);
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
}
