package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.DiffCallTreeNodeEntry;
import io.github.deplague.jmcmcp.domain.model.ExpandDiffCallTreeResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.CallTreeCache;
import java.util.ArrayList;
import java.util.List;

/**
 * Pure domain service for expanding a node in a diff call tree.
 * Contains no MCP-specific or UI formatting logic.
 */
public final class ExpandDiffCallTreeService {

    public ExpandDiffCallTreeResult expand(
            String parentNodeId,
            CallTreeCache.DiffTreeNode parentNode,
            String packageFilter,
            double baselineTotal,
            double targetTotal) {

        List<CallTreeCache.DiffTreeNode> visibleChildren = CallTreeCache.getVisibleDiffChildren(
                parentNode, packageFilter);
        List<DiffCallTreeNodeEntry> childEntries = new ArrayList<>();

        for (CallTreeCache.DiffTreeNode child : visibleChildren) {
            double baselinePct = baselineTotal > 0
                    ? (child.baselineCumulative() / baselineTotal) * 100.0 : 0.0;
            double targetPct = targetTotal > 0
                    ? (child.targetCumulative() / targetTotal) * 100.0 : 0.0;
            boolean hasChildren = !CallTreeCache.getVisibleDiffChildren(child, null).isEmpty();
            childEntries.add(new DiffCallTreeNodeEntry(
                    CallTreeCache.formatMethodName(child),
                    child.baselineCumulative(),
                    child.targetCumulative(),
                    child.delta(),
                    baselinePct,
                    targetPct,
                    child.changeType(),
                    hasChildren
            ));
        }

        return new ExpandDiffCallTreeResult(
                parentNodeId,
                CallTreeCache.formatMethodName(parentNode),
                parentNode.baselineCumulative(),
                parentNode.targetCumulative(),
                parentNode.delta(),
                parentNode.percentageChange(),
                parentNode.changeType(),
                childEntries,
                baselineTotal,
                targetTotal,
                !visibleChildren.isEmpty()
        );
    }
}
