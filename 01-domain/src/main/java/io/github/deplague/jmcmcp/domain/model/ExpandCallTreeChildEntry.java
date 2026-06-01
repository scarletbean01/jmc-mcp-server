package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single visible child node in an expanded call tree.
 */
public record ExpandCallTreeChildEntry(
    String methodName,
    double selfSamples,
    double totalSamples,
    double selfPct,
    double totalPct,
    boolean hasChildren
) {
}
