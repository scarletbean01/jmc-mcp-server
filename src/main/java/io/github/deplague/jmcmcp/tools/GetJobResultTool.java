package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.async.JobRecord;
import io.github.deplague.jmcmcp.async.JobStatus;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

/**
 * MCP tool for retrieving the result of a completed asynchronous analysis job.
 */
public final class GetJobResultTool {

    private static final String NAME = "get_job_result";

    private final AsyncJobService asyncJobService;

    public GetJobResultTool(AsyncJobService asyncJobService) {
        this.asyncJobService = asyncJobService;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Retrieve the result of a completed asynchronous analysis job. " +
                                "Use `get_job_status` first to confirm the job is COMPLETED.")
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

                        if (job.status() == JobStatus.PENDING || job.status() == JobStatus.RUNNING) {
                            return CallToolResult.builder()
                                    .addTextContent("Job is still " + job.status()
                                            + ". Use `get_job_status` to check progress.")
                                    .isError(false)
                                    .build();
                        }

                        if (job.status() == JobStatus.FAILED) {
                            return CallToolResult.builder()
                                    .addTextContent("# Job Failed\n\nError: " + job.errorMessage())
                                    .isError(true)
                                    .build();
                        }

                        if (job.status() == JobStatus.CANCELLED) {
                            return CallToolResult.builder()
                                    .addTextContent("Job was cancelled.")
                                    .isError(false)
                                    .build();
                        }

                        String result = asyncJobService.getResult(jobId);
                        if (result == null) {
                            return CallToolResult.builder()
                                    .addTextContent("Error: Result not available for job " + jobId)
                                    .isError(true)
                                    .build();
                        }

                        return CallToolResult.builder()
                                .addTextContent(result)
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
