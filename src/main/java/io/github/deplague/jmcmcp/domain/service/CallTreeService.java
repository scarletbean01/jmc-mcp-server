package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.CallTreeNodeEntry;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.flightrecorder.stacktrace.FrameSeparator;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.Node;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.StacktraceTreeModel;

import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.infrastructure.jfr.CallTreeCache.*;
import static java.util.List.of;
import static org.openjdk.jmc.common.item.ItemFilters.or;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.flightrecorder.stacktrace.FrameSeparator.FrameCategorization.METHOD;

/**
 * Pure domain service for building call trees from JFR recordings.
 */
@ApplicationScoped
public final class CallTreeService {

    public CallTreeAnalysis analyze(IItemCollection events, String subsystem, String packageFilter) {
        IItemCollection filtered = filterBySubsystem(events, subsystem);
        if (!filtered.hasItems()) {
            return new CallTreeAnalysis(null, 0, of());
        }

        StacktraceTreeModel tree = new StacktraceTreeModel(
                filtered,
                new FrameSeparator(METHOD, false),
                false
        );

        Node root = tree.getRoot();
        double totalSamples = computeTotalSamples(root);
        List<Node> visibleChildren = getVisibleChildren(root, packageFilter);

        List<CallTreeNodeEntry> nodes = new ArrayList<>();
        for (Node child : visibleChildren) {
            nodes.add(new CallTreeNodeEntry(
                    formatMethodName(child),
                    child.getWeight(),
                    child.getCumulativeWeight(),
                    !getVisibleChildren(child, null).isEmpty()
            ));
        }

        return new CallTreeAnalysis(tree, totalSamples, nodes);
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

    /**
     * Intermediate analysis result containing the tree model and extracted nodes.
     */
    public record CallTreeAnalysis(
            StacktraceTreeModel treeModel,
            double totalSamples,
            List<CallTreeNodeEntry> nodes
    ) {
    }
}
