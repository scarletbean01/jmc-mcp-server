package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import io.github.deplague.jmcmcp.domain.model.ExpandCallTreeChildEntry;
import io.github.deplague.jmcmcp.domain.model.ExpandCallTreeResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.Node;

import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.infrastructure.jfr.CallTreeCache.*;

/**
 * Pure domain service for expanding a specific node in a call tree.
 * Contains no MCP-specific or UI formatting logic.
 */
@ApplicationScoped
public final class ExpandCallTreeService {

    public ExpandCallTreeResult expand(Node root, String nodeId, String packageFilter, double totalSamples) {
        Node targetNode = findNode(root, nodeId);
        if (targetNode == null) {
            throw new AnalysisFailedException("Node `" + nodeId + "` not found in tree.");
        }

        String methodName = formatMethodName(targetNode);
        double selfSamples = targetNode.getWeight();
        double cumulative = targetNode.getCumulativeWeight();
        double totalPct = totalSamples > 0 ? (cumulative / totalSamples) * 100.0 : 0.0;

        List<Node> visibleChildren = getVisibleChildren(targetNode, packageFilter);
        List<ExpandCallTreeChildEntry> children = new ArrayList<>();
        for (Node child : visibleChildren) {
            double childSelf = child.getWeight();
            double childCumulative = child.getCumulativeWeight();
            double childSelfPct = totalSamples > 0 ? (childSelf / totalSamples) * 100.0 : 0.0;
            double childTotalPct = totalSamples > 0 ? (childCumulative / totalSamples) * 100.0 : 0.0;
            boolean hasChildren = !getVisibleChildren(child, null).isEmpty();
            children.add(new ExpandCallTreeChildEntry(
                    formatMethodName(child),
                    childSelf,
                    childCumulative,
                    childSelfPct,
                    childTotalPct,
                    hasChildren
            ));
        }

        return new ExpandCallTreeResult(
                nodeId,
                methodName,
                selfSamples,
                cumulative,
                totalPct,
                packageFilter,
                totalSamples,
                children,
                children.isEmpty()
        );
    }
}
