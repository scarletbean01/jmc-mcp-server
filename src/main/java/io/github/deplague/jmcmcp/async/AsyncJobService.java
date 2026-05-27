package io.github.deplague.jmcmcp.async;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages asynchronous execution of long-running JFR analysis tasks.
 *
 * <p>Jobs are submitted to a bounded thread pool and tracked by a unique
 * {@code job_id}. Clients poll status via {@link #getJob(String)} and retrieve
 * results via {@link #getResult(String)}.</p>
 *
 * <p>Completed jobs are retained for a configurable TTL (default 1 hour) before
 * automatic cleanup.</p>
 */
public final class AsyncJobService {

    private static final Logger LOG = LoggerFactory.getLogger(
        AsyncJobService.class
    );

    private static final int DEFAULT_CORE_POOL_SIZE = 4;
    private static final int DEFAULT_MAX_POOL_SIZE = 8;
    private static final long DEFAULT_KEEP_ALIVE_SECONDS = 60;
    private static final long DEFAULT_JOB_TTL_MINUTES = 60;
    private static final long DEFAULT_MAX_QUEUE_SIZE = 100;

    private final ThreadPoolExecutor executor;
    private final ConcurrentMap<String, JobRecord> jobs =
        new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ProgressReporter> progressReporters =
        new ConcurrentHashMap<>();
    private final long jobTtlMinutes;
    private final ScheduledExecutorService cleanupScheduler;

    public AsyncJobService() {
        this(
            DEFAULT_CORE_POOL_SIZE,
            DEFAULT_MAX_POOL_SIZE,
            DEFAULT_KEEP_ALIVE_SECONDS,
            DEFAULT_JOB_TTL_MINUTES
        );
    }

    public AsyncJobService(
        int corePoolSize,
        int maxPoolSize,
        long keepAliveSeconds,
        long jobTtlMinutes
    ) {
        this.jobTtlMinutes = jobTtlMinutes;

        this.executor = new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveSeconds,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>((int) DEFAULT_MAX_QUEUE_SIZE),
            new ThreadFactory() {
                private int counter = 0;

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "jmc-mcp-async-" + (++counter));
                    t.setDaemon(true);
                    return t;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        this.cleanupScheduler = Executors.newSingleThreadScheduledExecutor(
            r -> {
                Thread t = new Thread(r, "jmc-mcp-cleanup");
                t.setDaemon(true);
                return t;
            }
        );

        // Run cleanup every 5 minutes
        this.cleanupScheduler.scheduleWithFixedDelay(
            this::cleanupExpiredJobs,
            5,
            5,
            TimeUnit.MINUTES
        );

        LOG.info(
            "AsyncJobService started: core={}, max={}, queue={}, jobTTL={}min",
            corePoolSize,
            maxPoolSize,
            DEFAULT_MAX_QUEUE_SIZE,
            jobTtlMinutes
        );
    }

    /**
     * Submit a new async analysis job.
     *
     * @param toolName   the MCP tool name
     * @param arguments  the original tool arguments
     * @param task       the analysis task to run
     * @return the unique job ID
     */
    public String submit(
        String toolName,
        Map<String, Object> arguments,
        Callable<String> task
    ) {
        String jobId = UUID.randomUUID().toString();

        // Create the future first so we can atomically store it with the initial record
        Future<?> future = executor.submit(() -> {
            long start = System.currentTimeMillis();
            // Atomically transition PENDING → RUNNING
            JobRecord started = jobs.computeIfPresent(jobId, (id, rec) ->
                rec.withStarted()
            );
            if (started == null) {
                LOG.warn("Job {} was removed before starting", jobId);
                return;
            }
            LOG.info(
                "Job {} started: tool={}, args={}",
                jobId,
                toolName,
                arguments.keySet()
            );

            try {
                String result = task.call();
                jobs.computeIfPresent(jobId, (id, rec) ->
                    rec.withResult(result)
                );
                LOG.info(
                    "Job {} completed in {}ms",
                    jobId,
                    System.currentTimeMillis() - start
                );
            } catch (Exception e) {
                jobs.computeIfPresent(jobId, (id, rec) -> {
                    // If the job was explicitly cancelled, preserve that status
                    // instead of overwriting with FAILED.
                    if (rec.status() == JobStatus.CANCELLED) {
                        return rec;
                    }
                    return rec.withError(e.getMessage());
                });
                LOG.warn(
                    "Job {} failed after {}ms: {}",
                    jobId,
                    System.currentTimeMillis() - start,
                    e.getMessage()
                );
            } finally {
                progressReporters.remove(jobId);
            }
        });

        JobRecord initial = new JobRecord(
            jobId,
            toolName,
            arguments,
            JobStatus.PENDING,
            null,
            null,
            Instant.now(),
            null,
            null,
            future
        );
        jobs.put(jobId, initial);
        progressReporters.put(jobId, (pct, msg) -> {
            jobs.computeIfPresent(jobId, (id, rec) ->
                rec.withProgress(pct, msg)
            );
        });

        return jobId;
    }

    /**
     * Get the progress reporter for a running job.
     * Tools can call this to provide incremental updates.
     */
    public ProgressReporter getProgressReporter(String jobId) {
        return progressReporters.getOrDefault(jobId, ProgressReporter.noop());
    }

    /**
     * Get the current record for a job.
     */
    public JobRecord getJob(String jobId) {
        return jobs.get(jobId);
    }

    /**
     * Get the result of a completed job.
     *
     * @return the result string, or {@code null} if not yet completed
     */
    public String getResult(String jobId) {
        JobRecord job = jobs.get(jobId);
        if (job == null) {
            return null;
        }
        if (job.status() == JobStatus.COMPLETED) {
            return job.result();
        }
        return null;
    }

    /**
     * Cancel a pending or running job.
     *
     * @return true if the job was found and cancellation was attempted
     */
    public boolean cancel(String jobId) {
        JobRecord job = jobs.get(jobId);
        if (job == null) {
            return false;
        }
        if (job.future() != null) {
            job.future().cancel(true);
        }
        jobs.put(
            jobId,
            new JobRecord(
                job.jobId(),
                job.toolName(),
                job.arguments(),
                JobStatus.CANCELLED,
                job.result(),
                job.errorMessage(),
                job.createdAt(),
                job.startedAt(),
                Instant.now(),
                job.future()
            )
        );
        return true;
    }

    /**
     * @return total number of jobs tracked (including completed)
     */
    public int totalJobs() {
        return jobs.size();
    }

    /**
     * @return number of currently active (pending or running) jobs
     */
    public long activeJobs() {
        return jobs
            .values()
            .stream()
            .filter(
                j ->
                    j.status() == JobStatus.PENDING ||
                    j.status() == JobStatus.RUNNING
            )
            .count();
    }

    /**
     * @return number of pending jobs waiting in the queue
     */
    public long pendingJobs() {
        return jobs
            .values()
            .stream()
            .filter(j -> j.status() == JobStatus.PENDING)
            .count();
    }

    /**
     * Estimate recommended polling interval in seconds based on queue depth.
     * Deeper queues → longer intervals to reduce stdio chatter.
     */
    public int recommendedPollSeconds() {
        long pending = pendingJobs();
        if (pending > 20) return 10;
        if (pending > 5) return 5;
        return 2;
    }

    /**
     * @return number of completed jobs
     */
    public long completedJobs() {
        return jobs
            .values()
            .stream()
            .filter(j -> j.status() == JobStatus.COMPLETED)
            .count();
    }

    /**
     * @return number of failed jobs
     */
    public long failedJobs() {
        return jobs
            .values()
            .stream()
            .filter(j -> j.status() == JobStatus.FAILED)
            .count();
    }

    /**
     * Shutdown the executor gracefully.
     */
    public void shutdown() {
        LOG.info("Shutting down AsyncJobService...");
        cleanupScheduler.shutdown();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void cleanupExpiredJobs() {
        Instant cutoff = Instant.now().minus(Duration.ofMinutes(jobTtlMinutes));
        int removed = 0;
        for (var entry : jobs.entrySet()) {
            JobRecord job = entry.getValue();
            Instant endTime =
                job.completedAt() != null ? job.completedAt() : job.createdAt();
            boolean isTerminal =
                job.status() == JobStatus.COMPLETED ||
                job.status() == JobStatus.FAILED ||
                job.status() == JobStatus.CANCELLED;
            if (isTerminal && endTime.isBefore(cutoff)) {
                jobs.remove(entry.getKey());
                removed++;
            }
        }
        if (removed > 0) {
            LOG.debug("Cleaned up {} expired async jobs", removed);
        }
    }
}
