package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of expanding a specific node in a call tree.
 */
public record ExpandCallTreeResult(
    String parentNodeId,
    String parentMethodName,
    double parentSelfSamples,
    double parentTotalSamples,
    double parentTotalPct,
    String packageFilter,
    double totalSamples,
    List<ExpandCallTreeChildEntry> children,
    boolean isLeaf
) {
}
