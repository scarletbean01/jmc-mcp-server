package io.github.deplague.jmcmcp.domain.model;

/**
 * Lifecycle states for an asynchronous analysis job.
 */
public enum JobStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED
}
