package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.CallTreeNodeEntry;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.CallTreeCache;
import java.util.ArrayList;
import java.util.List;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.flightrecorder.stacktrace.FrameSeparator;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.Node;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.StacktraceTreeModel;

/**
 * Pure domain service for building call trees from JFR recordings.
 */
public final class CallTreeService {

    public CallTreeAnalysis analyze(IItemCollection events, String subsystem, String packageFilter) {
        IItemCollection filtered = filterBySubsystem(events, subsystem);
        if (!filtered.hasItems()) {
            return new CallTreeAnalysis(null, 0, List.of());
        }

        StacktraceTreeModel tree = new StacktraceTreeModel(
                filtered,
                new FrameSeparator(FrameSeparator.FrameCategorization.METHOD, false),
                false
        );

        Node root = tree.getRoot();
        double totalSamples = CallTreeCache.computeTotalSamples(root);
        List<Node> visibleChildren = CallTreeCache.getVisibleChildren(root, packageFilter);

        List<CallTreeNodeEntry> nodes = new ArrayList<>();
        for (Node child : visibleChildren) {
            nodes.add(new CallTreeNodeEntry(
                    CallTreeCache.formatMethodName(child),
                    child.getWeight(),
                    child.getCumulativeWeight(),
                    !CallTreeCache.getVisibleChildren(child, null).isEmpty()
            ));
        }

        return new CallTreeAnalysis(tree, totalSamples, nodes);
    }

    public static IItemCollection filterBySubsystem(IItemCollection events, String subsystem) {
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

    /**
     * Intermediate analysis result containing the tree model and extracted nodes.
     */
    public record CallTreeAnalysis(
        StacktraceTreeModel treeModel,
        double totalSamples,
        List<CallTreeNodeEntry> nodes
    ) {}
}
