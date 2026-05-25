package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.async.JobRecord;
import io.github.deplague.jmcmcp.async.JobStatus;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

/**
 * MCP tool for polling the status of an asynchronous analysis job.
 */
public final class GetJobStatusTool {

    private static final String NAME = "get_job_status";

    private final AsyncJobService asyncJobService;

    public GetJobStatusTool(AsyncJobService asyncJobService) {
        this.asyncJobService = asyncJobService;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Poll the status of an asynchronous analysis job submitted via the async parameter. " +
                                "Returns PENDING, RUNNING, COMPLETED, FAILED, or CANCELLED.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "job_id", SchemaUtil.stringProp("The job ID returned when the async job was submitted")
                                ),
                                SchemaUtil.required("job_id")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String jobId = SchemaUtil.getString(request.arguments(), "job_id");
                        JobRecord job = asyncJobService.getJob(jobId);

                        if (job == null) {
                            return CallToolResult.builder()
                                    .addTextContent("Error: Job not found: " + jobId)
                                    .isError(true)
                                    .build();
                        }

                        StringBuilder sb = new StringBuilder();
                        sb.append("# Job Status\n\n");
                        sb.append("- **Job ID:** `").append(job.jobId()).append("`\n");
                        sb.append("- **Tool:** `").append(job.toolName()).append("`\n");
                        sb.append("- **Status:** `").append(job.status()).append("`\n");
                        sb.append("- **Created:** ").append(job.createdAt()).append("\n");
                        if (job.startedAt() != null) {
                            sb.append("- **Started:** ").append(job.startedAt()).append("\n");
                        }
                        if (job.completedAt() != null) {
                            sb.append("- **Completed:** ").append(job.completedAt()).append("\n");
                        }
                        if (job.status() == JobStatus.RUNNING || job.status() == JobStatus.COMPLETED || job.status() == JobStatus.FAILED) {
                            sb.append("- **Duration:** ").append(job.durationMillis()).append("ms\n");
                        }
                        if (job.errorMessage() != null) {
                            sb.append("- **Error:** ").append(job.errorMessage()).append("\n");
                        }

                        if (job.status() == JobStatus.COMPLETED) {
                            sb.append("\nUse `get_job_result` with this job ID to retrieve the full result.\n");
                        }

                        return CallToolResult.builder()
                                .addTextContent(sb.toString())
                                .isError(false)
                                .build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }
}
