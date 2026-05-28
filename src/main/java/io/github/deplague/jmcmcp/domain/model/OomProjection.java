package io.github.deplague.jmcmcp.domain.model;

/**
 * Projected OutOfMemoryError time based on linear regression.
 */
public record OomProjection(
        long projectedOomTimeMs,
        Double minutesToOom,
        boolean alreadyExceeded
) {
}
