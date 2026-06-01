package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of diff call tree analysis ready for Markdown formatting.
 */
public record DiffCallTreeResult(
    String treeId,
    String subsystem,
    String packageFilter,
    double baselineTotal,
    double targetTotal,
    List<DiffCallTreeNodeEntry> nodes,
    boolean hasNodes
) {
}
