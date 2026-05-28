package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single visible child node in a diff call tree result.
 */
public record DiffCallTreeNodeEntry(
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
