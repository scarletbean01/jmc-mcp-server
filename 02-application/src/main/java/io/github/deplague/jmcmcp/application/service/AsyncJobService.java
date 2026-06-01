package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.model.JobStatusResponse;
import io.github.deplague.jmcmcp.domain.model.AsyncJob;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

/**
 * Manages asynchronous analysis jobs with in-memory storage and SSE event broadcasting.
 * Uses a managed executor for async work and a semaphore for backpressure.
 */
@Slf4j
@ApplicationScoped
public class AsyncJobService {

    private final Map<String, AsyncJob> jobs = new ConcurrentHashMap<>();
    private final Map<String, Consumer<JobStatusResponse>> listeners = new ConcurrentHashMap<>();

    /**
     * Semaphore to limit concurrent async analysis jobs and prevent
     * thread starvation / heap exhaustion under load.
     * Default permits: 50 (tunable via quarkus.thread-pool.max-threads).
     */
    private final Semaphore backpressure = new Semaphore(50);

    public AsyncJob createJob(String recordingId, String analysisType) {
        String jobId = UUID.randomUUID().toString();
        AsyncJob job = new AsyncJob(
                jobId, recordingId, analysisType,
                "PENDING", null, null,
                Instant.now(), null, 0,
                null
        );
        jobs.put(jobId, job);
        log.info("Created async job {} for analysis {} on recording {}", jobId, analysisType, recordingId);
        notifyListeners(job);
        return job;
    }

    public AsyncJob getJob(String jobId) {
        return jobs.get(jobId);
    }

    public void updateJobStatus(String jobId, String status, int progress) {
        AsyncJob old = jobs.get(jobId);
        if (old != null) {
            AsyncJob updated = new AsyncJob(
                    old.jobId(), old.recordingId(), old.analysisType(),
                    status, old.result(), old.error(),
                    old.createdAt(), old.completedAt(), progress,
                    old.future()
            );
            jobs.put(jobId, updated);
            notifyListeners(updated);
        }
    }

    public void completeJob(String jobId, Object result) {
        AsyncJob old = jobs.get(jobId);
        if (old != null) {
            AsyncJob updated = new AsyncJob(
                    old.jobId(), old.recordingId(), old.analysisType(),
                    "COMPLETED", result, null,
                    old.createdAt(), Instant.now(), 100,
                    old.future()
            );
            jobs.put(jobId, updated);
            notifyListeners(updated);
        }
    }

    public void failJob(String jobId, String error) {
        AsyncJob old = jobs.get(jobId);
        if (old != null) {
            AsyncJob updated = new AsyncJob(
                    old.jobId(), old.recordingId(), old.analysisType(),
                    "FAILED", null, error,
                    old.createdAt(), Instant.now(), old.progressPercent(),
                    old.future()
            );
            jobs.put(jobId, updated);
            notifyListeners(updated);
        }
    }

    public void setJobFuture(String jobId, CompletableFuture<?> future) {
        AsyncJob old = jobs.get(jobId);
        if (old != null) {
            jobs.put(jobId, new AsyncJob(
                    old.jobId(), old.recordingId(), old.analysisType(),
                    old.status(), old.result(), old.error(),
                    old.createdAt(), old.completedAt(), old.progressPercent(),
                    future
            ));
        }
    }

    /**
     * Submit async work using a managed executor with backpressure.
     * Blocks until a permit is available, then runs the task asynchronously.
     */
    public CompletableFuture<Void> submitAsync(Runnable task, Executor managedExecutor) {
        return CompletableFuture.runAsync(() -> {
            try {
                backpressure.acquire();
                try {
                    task.run();
                } finally {
                    backpressure.release();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Async job interrupted", e);
            }
        }, managedExecutor);
    }

    /**
     * Subscribe a listener for SSE updates on a specific job.
     */
    public void subscribe(String jobId, Consumer<JobStatusResponse> listener) {
        listeners.put(jobId, listener);
        // Immediately send current state
        AsyncJob job = jobs.get(jobId);
        if (job != null) {
            listener.accept(toStatusResponse(job));
        }
    }

    /**
     * Unsubscribe a listener.
     */
    public void unsubscribe(String jobId) {
        listeners.remove(jobId);
    }

    private void notifyListeners(AsyncJob job) {
        Consumer<JobStatusResponse> listener = listeners.get(job.jobId());
        if (listener != null) {
            try {
                listener.accept(toStatusResponse(job));
            } catch (Exception e) {
                log.warn("Failed to notify listener for job {}", job.jobId(), e);
                listeners.remove(job.jobId());
            }
        }
    }

    private JobStatusResponse toStatusResponse(AsyncJob job) {
        return new JobStatusResponse(
                job.jobId(),
                job.status(),
                job.result(),
                job.error(),
                job.createdAt(),
                job.completedAt(),
                job.progressPercent()
        );
    }
}
