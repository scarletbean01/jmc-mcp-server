package io.github.deplague.jmcmcp.domain.model;

/**
 * Reference processing statistic per reference type / phase.
 */
public record ReferenceStatEntry(
        String type,
        long count,
        String processingTime
) {
}
