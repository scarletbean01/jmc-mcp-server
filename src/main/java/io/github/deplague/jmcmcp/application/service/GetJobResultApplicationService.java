package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JobRepository;
import io.github.deplague.jmcmcp.domain.model.JobResultResponse;
import io.github.deplague.jmcmcp.domain.model.JobSummary;
import io.github.deplague.jmcmcp.domain.service.GetJobResultService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Application service that orchestrates async job result retrieval.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class GetJobResultApplicationService {

    private final JobRepository jobRepository;
    private final GetJobResultService getJobResultService;

    /**
     * Retrieve the result response for the given job ID.
     */
    public JobResultResponse getResult(String jobId) {
        JobSummary job = jobRepository.getJob(jobId);
        String result = jobRepository.getResult(jobId);
        return getJobResultService.resolve(jobId, job, result);
    }
}
