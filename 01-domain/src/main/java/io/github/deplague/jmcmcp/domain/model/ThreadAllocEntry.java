package io.github.deplague.jmcmcp.domain.model;

/**
 * A single thread allocation entry.
 */
public record ThreadAllocEntry(String threadName, String totalAllocated, String allocationRate, boolean heavy) {
}
