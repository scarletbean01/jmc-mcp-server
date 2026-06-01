package io.github.deplague.jmcmcp.domain.model;

/**
 * Blocking time distribution by category.
 */
public record CategoryDistributionEntry(String category, String totalTime, long count, String avgDuration) {
}
