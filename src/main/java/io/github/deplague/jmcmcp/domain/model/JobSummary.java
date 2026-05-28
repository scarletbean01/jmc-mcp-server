package io.github.deplague.jmcmcp.domain.model;

/**
 * Immutable snapshot of an async analysis job for the domain layer.
 */
public record JobSummary(
        String jobId,
        JobStatus status,
        String errorMessage
) {
}
