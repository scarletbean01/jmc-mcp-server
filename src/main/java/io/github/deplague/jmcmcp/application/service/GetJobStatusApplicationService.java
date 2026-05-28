package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.async.JobRecord;
import io.github.deplague.jmcmcp.domain.model.JobStatusInfo;
import io.github.deplague.jmcmcp.domain.service.GetJobStatusService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Application service that orchestrates job status lookup and formatting.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class GetJobStatusApplicationService {

    private final AsyncJobService asyncJobService;
    private final GetJobStatusService getJobStatusService;

    /**
     * Retrieve and format the status for the given job ID.
     *
     * @return formatted Markdown, or {@code null} if the job does not exist
     */
    public String getJobStatus(String jobId) {
        JobRecord job = asyncJobService.getJob(jobId);
        if (job == null) {
            return null;
        }

        JobStatusInfo info = new JobStatusInfo(
                job.jobId(),
                job.toolName(),
                job.status(),
                job.progressPercent(),
                job.progressMessage(),
                job.createdAt(),
                job.startedAt(),
                job.completedAt(),
                job.durationMillis(),
                job.errorMessage()
        );

        int pollSeconds = asyncJobService.recommendedPollSeconds();
        return getJobStatusService.formatJobStatus(info, pollSeconds);
    }
}
