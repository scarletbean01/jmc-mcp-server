package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of thread allocation analysis.
 */
public record ThreadAllocationResult(
        List<ThreadAllocEntry> entries,
        boolean hasData,
        boolean heavyAllocationDetected
) {
}
