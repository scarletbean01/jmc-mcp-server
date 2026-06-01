package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single CPU state distribution entry.
 */
public record StateDistributionEntry(String state, long samples, double percentage) {
}
