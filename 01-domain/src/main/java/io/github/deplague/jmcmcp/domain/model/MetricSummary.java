package io.github.deplague.jmcmcp.domain.model;

/**
 * Simple min/avg/max summary for a metric.
 */
public record MetricSummary(
        Long min,
        Long avg,
        Long max
) {
}
