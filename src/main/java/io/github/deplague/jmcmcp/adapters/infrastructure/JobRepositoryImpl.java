package io.github.deplague.jmcmcp.adapters.infrastructure;

import io.github.deplague.jmcmcp.application.port.JobRepository;
import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.async.JobRecord;
import io.github.deplague.jmcmcp.domain.model.JobStatus;
import io.github.deplague.jmcmcp.domain.model.JobSummary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Infrastructure adapter that implements {@link JobRepository} using
 * {@link AsyncJobService}.
 */
@ApplicationScoped
public class JobRepositoryImpl implements JobRepository {

    private final AsyncJobService asyncJobService;

    @Inject
    public JobRepositoryImpl(AsyncJobService asyncJobService) {
        this.asyncJobService = asyncJobService;
    }

    @Override
    public JobSummary getJob(String jobId) {
        JobRecord job = asyncJobService.getJob(jobId);
        if (job == null) {
            return null;
        }
        return new JobSummary(
                job.jobId(),
                mapStatus(job.status()),
                job.errorMessage()
        );
    }

    @Override
    public String getResult(String jobId) {
        return asyncJobService.getResult(jobId);
    }

    private static JobStatus mapStatus(io.github.deplague.jmcmcp.async.JobStatus status) {
        return JobStatus.valueOf(status.name());
    }
}
