package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of VM operations analysis.
 */
public record VmOperationsResult(
        List<VmOperationEntry> operations,
        String totalDuration,
        String maxDuration,
        String avgDuration
) {

    public boolean hasOperations() {
        return operations != null && !operations.isEmpty();
    }
}
