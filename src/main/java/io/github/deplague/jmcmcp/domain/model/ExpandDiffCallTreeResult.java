package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of expanding a node in a differential call tree.
 */
public record ExpandDiffCallTreeResult(
    String treeId,
    String parentNodeId,
    String parentMethodName,
    double parentBaselineCumulative,
    double parentTargetCumulative,
    double parentDelta,
    double parentPercentageChange,
    String parentChangeType,
    double baselineTotal,
    double targetTotal,
    List<ExpandDiffCallTreeChildEntry> children
) {
}
