package io.github.deplague.jmcmcp.domain.model;

import io.github.deplague.jmcmcp.infrastructure.jfr.CallTreeCache;

/**
 * Raw result of diff call tree domain analysis containing the root node
 * and sample totals before caching and formatting.
 */
public record DiffCallTreeData(
    CallTreeCache.DiffTreeNode root,
    double baselineTotal,
    double targetTotal,
    boolean hasEvents
) {
}
