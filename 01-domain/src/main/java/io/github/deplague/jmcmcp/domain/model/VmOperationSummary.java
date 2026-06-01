package io.github.deplague.jmcmcp.domain.model;

/**
 * VM operation summary.
 */
public record VmOperationSummary(
        long count,
        long avgNanos,
        long maxNanos
) {
}
