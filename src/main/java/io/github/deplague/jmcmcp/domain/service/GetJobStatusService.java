package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.async.JobStatus;
import io.github.deplague.jmcmcp.domain.model.JobStatusInfo;

/**
 * Pure domain service for formatting job status as Markdown.
 */
public final class GetJobStatusService {

    public String formatJobStatus(JobStatusInfo job, int recommendedPollSeconds) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Job Status\n\n");
        sb.append("- **Job ID:** `")
                .append(job.jobId())
                .append("`\n");
        sb.append("- **Tool:** `")
                .append(job.toolName())
                .append("`\n");
        sb.append("- **Status:** `")
                .append(job.status())
                .append("`\n");

        if (
                job.progressPercent() > 0 ||
                        !job.progressMessage().isEmpty()
        ) {
            sb.append("- **Progress:** ")
                    .append(job.progressPercent())
                    .append("%");
            if (!job.progressMessage().isEmpty()) {
                sb.append(" — ").append(job.progressMessage());
            }
            sb.append("\n");
        }

        sb.append("- **Created:** ")
                .append(job.createdAt())
                .append("\n");
        if (job.startedAt() != null) {
            sb.append("- **Started:** ")
                    .append(job.startedAt())
                    .append("\n");
        }
        if (job.completedAt() != null) {
            sb.append("- **Completed:** ")
                    .append(job.completedAt())
                    .append("\n");
        }
        if (
                job.status() == JobStatus.RUNNING ||
                        job.status() == JobStatus.COMPLETED ||
                        job.status() == JobStatus.FAILED
        ) {
            sb.append("- **Duration:** ")
                    .append(job.durationMillis())
                    .append("ms\n");
        }
        if (job.errorMessage() != null) {
            sb.append("- **Error:** ")
                    .append(job.errorMessage())
                    .append("\n");
        }

        if (job.status() == JobStatus.COMPLETED) {
            sb.append(
                    "\nUse `get_job_result` with this job ID to retrieve the full result.\n"
            );
        } else if (
                job.status() == JobStatus.PENDING ||
                        job.status() == JobStatus.RUNNING
        ) {
            sb.append("\n*Recommendation:* Poll again in ")
                    .append(recommendedPollSeconds)
                    .append("s.\n");
        }

        return sb.toString();
    }
}
