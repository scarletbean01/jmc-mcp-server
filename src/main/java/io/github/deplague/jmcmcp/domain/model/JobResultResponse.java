package io.github.deplague.jmcmcp.domain.model;

/**
 * Outcome of retrieving the result of an async analysis job.
 */
public record JobResultResponse(
        Type type,
        String content,
        String jobId
) {
    public enum Type {
        NOT_FOUND,
        NOT_READY,
        FAILED,
        CANCELLED,
        MISSING_RESULT,
        SUCCESS
    }
}
