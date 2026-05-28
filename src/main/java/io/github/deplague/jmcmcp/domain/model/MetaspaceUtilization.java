package io.github.deplague.jmcmcp.domain.model;

/**
 * Metaspace utilization metrics extracted from jdk.MetaspaceSummary.
 */
public record MetaspaceUtilization(
        double usedMB,
        double committedMB,
        double utilizationPct
) {
}
