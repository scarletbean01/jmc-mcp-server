package io.github.deplague.jmcmcp.application.port;

import io.github.deplague.jmcmcp.domain.model.JobSummary;

/**
 * Port for accessing asynchronous job information.
 * Implemented by infrastructure adapters.
 */
public interface JobRepository {

    /**
     * Get the current summary for a job.
     *
     * @param jobId the unique job identifier
     * @return the job summary, or {@code null} if not found
     */
    JobSummary getJob(String jobId);

    /**
     * Get the result of a completed job.
     *
     * @param jobId the unique job identifier
     * @return the result string, or {@code null} if not yet completed
     */
    String getResult(String jobId);
}
