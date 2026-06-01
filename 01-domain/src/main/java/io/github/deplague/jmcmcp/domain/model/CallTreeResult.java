package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of a call tree analysis.
 */
public record CallTreeResult(
    String treeId,
    String subsystem,
    String packageFilter,
    double totalSamples,
    List<CallTreeNodeEntry> nodes
) {
    public boolean hasResults() {
        return nodes != null && !nodes.isEmpty();
    }
}
