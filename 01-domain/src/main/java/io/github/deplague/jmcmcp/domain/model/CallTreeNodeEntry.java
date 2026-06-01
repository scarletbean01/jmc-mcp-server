package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single node in a call tree result.
 */
public record CallTreeNodeEntry(
    String methodName,
    double selfSamples,
    double totalSamples,
    boolean hasChildren
) {}
