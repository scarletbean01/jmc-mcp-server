package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ExpandDiffCallTreeChildEntry;
import io.github.deplague.jmcmcp.domain.model.ExpandDiffCallTreeResult;
import io.github.deplague.jmcmcp.infrastructure.jfr.CallTreeCache;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain service for expanding nodes in a differential call tree.
 */
@ApplicationScoped
public final class ExpandDiffCallTreeService {

    public ExpandDiffCallTreeResult expand(
            CallTreeCache.DiffTreeNode parentNode,
            String treeId,
            String parentNodeId,
            String packageFilter,
            double baselineTotal,
            double targetTotal
    ) {
        List<ExpandDiffCallTreeChildEntry> children = new ArrayList<>();
        List<CallTreeCache.DiffTreeNode> visibleChildren = CallTreeCache.getVisibleDiffChildren(parentNode, packageFilter);

        for (int i = 0; i < visibleChildren.size(); i++) {
            CallTreeCache.DiffTreeNode child = visibleChildren.get(i);
            String childId = parentNodeId + "-" + i;

            double baselinePct = baselineTotal > 0 ? (child.baselineCumulative() / baselineTotal) * 100.0 : 0.0;
            double targetPct = targetTotal > 0 ? (child.targetCumulative() / targetTotal) * 100.0 : 0.0;

            boolean hasChildren = !CallTreeCache.getVisibleDiffChildren(child, null).isEmpty();

            children.add(new ExpandDiffCallTreeChildEntry(
                    childId,
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
                treeId,
                parentNodeId,
                CallTreeCache.formatMethodName(parentNode),
                parentNode.baselineCumulative(),
                parentNode.targetCumulative(),
                parentNode.delta(),
                parentNode.percentageChange(),
                parentNode.changeType(),
                baselineTotal,
                targetTotal,
                children
        );
    }
}
