package io.github.deplague.jmcmcp.async;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class AsyncJobServiceTest {

    private AsyncJobService service;

    @BeforeEach
    void setUp() {
        service = new AsyncJobService(1, 2, 60, 5);
    }

    @AfterEach
    void tearDown() {
        service.shutdown();
    }

    @Test
    void submitReturnsJobId() {
        String jobId = service.submit("test_tool", Map.of(), () -> "hello");
        assertThat(jobId).isNotNull().isNotEmpty();
    }

    @Test
    void jobStartsAsPending() {
        String jobId = service.submit("test_tool", Map.of(), () -> {
            Thread.sleep(500);
            return "done";
        });

        JobRecord job = service.getJob(jobId);
        assertThat(job).isNotNull();
        assertThat(job.status()).isIn(JobStatus.PENDING, JobStatus.RUNNING);
        assertThat(job.toolName()).isEqualTo("test_tool");
    }

    @Test
    void jobCompletesSuccessfully() throws InterruptedException {
        String jobId = service.submit("test_tool", Map.of(), () -> "success result");

        // Wait for completion
        boolean completed = waitForStatus(jobId, JobStatus.COMPLETED, 5);
        assertThat(completed).isTrue();

        assertThat(service.getResult(jobId)).isEqualTo("success result");
        assertThat(service.completedJobs()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void jobFailsGracefully() throws InterruptedException {
        String jobId = service.submit("test_tool", Map.of(), () -> {
            throw new RuntimeException("simulated failure");
        });

        boolean failed = waitForStatus(jobId, JobStatus.FAILED, 5);
        assertThat(failed).isTrue();

        JobRecord job = service.getJob(jobId);
        assertThat(job.errorMessage()).contains("simulated failure");
        assertThat(service.failedJobs()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void cancelJob() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        String jobId = service.submit("test_tool", Map.of(), () -> {
            latch.await(10, TimeUnit.SECONDS);
            return "should not complete";
        });

        // Give it time to start
        Thread.sleep(100);

        boolean cancelled = service.cancel(jobId);
        assertThat(cancelled).isTrue();

        JobRecord job = service.getJob(jobId);
        assertThat(job.status()).isEqualTo(JobStatus.CANCELLED);

        latch.countDown(); // Release the worker thread
    }

    @Test
    void getResultReturnsNullForPendingJob() {
        String jobId = service.submit("test_tool", Map.of(), () -> {
            try { Thread.sleep(5000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "late";
        });
        assertThat(service.getResult(jobId)).isNull();
    }

    @Test
    void getJobReturnsNullForUnknownId() {
        assertThat(service.getJob("nonexistent-id")).isNull();
    }

    @Test
    void activeJobsCount() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        String jobId = service.submit("test_tool", Map.of(), () -> {
            latch.await(10, TimeUnit.SECONDS);
            return "done";
        });

        Thread.sleep(100); // let it start
        assertThat(service.activeJobs()).isGreaterThanOrEqualTo(1);

        latch.countDown();
    }

    @Test
    void totalJobsTracksAll() throws InterruptedException {
        service.submit("t1", Map.of(), () -> "a");
        service.submit("t2", Map.of(), () -> "b");

        Thread.sleep(200);
        assertThat(service.totalJobs()).isGreaterThanOrEqualTo(2);
    }

    private boolean waitForStatus(String jobId, JobStatus expectedStatus, int timeoutSeconds) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000;
        while (System.currentTimeMillis() < deadline) {
            JobRecord job = service.getJob(jobId);
            if (job != null && job.status() == expectedStatus) {
                return true;
            }
            Thread.sleep(50);
        }
        return false;
    }
}
