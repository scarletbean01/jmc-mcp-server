package io.github.deplague.jmcmcp.async;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Immutable snapshot of an async analysis job.
 */
public final class JobRecord {

    private final String jobId;
    private final String toolName;
    private final Map<String, Object> arguments;
    private final JobStatus status;
    private final String result;
    private final String errorMessage;
    private final Instant createdAt;
    private final Instant startedAt;
    private final Instant completedAt;
    private final transient Future<?> future;

    public JobRecord(String jobId, String toolName, Map<String, Object> arguments,
                     JobStatus status, String result, String errorMessage,
                     Instant createdAt, Instant startedAt, Instant completedAt,
                     Future<?> future) {
        this.jobId = jobId;
        this.toolName = toolName;
        this.arguments = arguments != null ? Map.copyOf(arguments) : Map.of();
        this.status = status;
        this.result = result;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.future = future;
    }

    public String jobId() {
        return jobId;
    }

    public String toolName() {
        return toolName;
    }

    public Map<String, Object> arguments() {
        return arguments;
    }

    public JobStatus status() {
        return status;
    }

    public String result() {
        return result;
    }

    public String errorMessage() {
        return errorMessage;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant startedAt() {
        return startedAt;
    }

    public Instant completedAt() {
        return completedAt;
    }

    public Future<?> future() {
        return future;
    }

    public long durationMillis() {
        if (startedAt == null) return 0;
        Instant end = completedAt != null ? completedAt : Instant.now();
        return end.toEpochMilli() - startedAt.toEpochMilli();
    }

    public JobRecord withStatus(JobStatus newStatus) {
        return new JobRecord(jobId, toolName, arguments, newStatus, result, errorMessage,
                createdAt, startedAt, completedAt, future);
    }

    public JobRecord withStarted() {
        return new JobRecord(jobId, toolName, arguments, JobStatus.RUNNING, result, errorMessage,
                createdAt, Instant.now(), completedAt, future);
    }

    public JobRecord withResult(String newResult) {
        return new JobRecord(jobId, toolName, arguments, JobStatus.COMPLETED, newResult, errorMessage,
                createdAt, startedAt, Instant.now(), future);
    }

    public JobRecord withError(String message) {
        return new JobRecord(jobId, toolName, arguments, JobStatus.FAILED, result, message,
                createdAt, startedAt, Instant.now(), future);
    }
}
