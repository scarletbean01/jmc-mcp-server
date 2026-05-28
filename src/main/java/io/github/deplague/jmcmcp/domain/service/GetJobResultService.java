package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.JobResultResponse;
import io.github.deplague.jmcmcp.domain.model.JobStatus;
import io.github.deplague.jmcmcp.domain.model.JobSummary;

/**
 * Pure domain service for resolving the result of an asynchronous analysis job.
 * Contains no MCP-specific or framework logic.
 */
public final class GetJobResultService {

    public JobResultResponse resolve(String jobId, JobSummary job, String result) {
        if (job == null) {
            return new JobResultResponse(
                    JobResultResponse.Type.NOT_FOUND,
                    null,
                    jobId
            );
        }

        return switch (job.status()) {
            case PENDING, RUNNING -> new JobResultResponse(
                    JobResultResponse.Type.NOT_READY,
                    job.status().name(),
                    jobId
            );
            case FAILED -> new JobResultResponse(
                    JobResultResponse.Type.FAILED,
                    job.errorMessage(),
                    jobId
            );
            case CANCELLED -> new JobResultResponse(
                    JobResultResponse.Type.CANCELLED,
                    null,
                    jobId
            );
            case COMPLETED -> {
                if (result == null) {
                    yield new JobResultResponse(
                            JobResultResponse.Type.MISSING_RESULT,
                            null,
                            jobId
                    );
                }
                yield new JobResultResponse(
                        JobResultResponse.Type.SUCCESS,
                        result,
                        jobId
                );
            }
        };
    }
}
