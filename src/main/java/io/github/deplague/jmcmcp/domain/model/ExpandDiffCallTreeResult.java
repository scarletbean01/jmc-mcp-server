package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of expanding a specific node in a diff call tree.
 */
public record ExpandDiffCallTreeResult(
    String parentNodeId,
    String parentMethodName,
    double parentBaselineCumulative,
    double parentTargetCumulative,
    double parentDelta,
    double parentPercentageChange,
    String parentChangeType,
    List<DiffCallTreeNodeEntry> children,
    double baselineTotal,
    double targetTotal,
    boolean hasChildren
) {
}
