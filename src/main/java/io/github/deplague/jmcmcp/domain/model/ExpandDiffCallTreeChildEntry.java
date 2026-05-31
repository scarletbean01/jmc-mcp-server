package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single visible child node in an expanded diff call tree.
 */
public record ExpandDiffCallTreeChildEntry(
    String nodeId,
    String methodName,
    double baselineCumulative,
    double targetCumulative,
    double delta,
    double baselinePct,
    double targetPct,
    String changeType,
    boolean hasChildren
) {
}
