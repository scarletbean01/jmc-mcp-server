package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.GetJobResultApplicationService;
import io.github.deplague.jmcmcp.domain.model.JobResultResponse;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for retrieving the result of a completed asynchronous analysis job.
 * Delegates to the application layer and formats results as Markdown.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class GetJobResultTool implements McpTool {

    private static final String NAME = "get_job_result";

    private final GetJobResultApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Retrieve the result of a completed asynchronous analysis job. " +
                                "Use `get_job_status` first to confirm the job is COMPLETED.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "job_id",
                                        SchemaUtil.stringProp("The job ID returned when the async job was submitted")
                                ),
                                SchemaUtil.required("job_id")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String jobId = SchemaUtil.getString(request.arguments(), "job_id");
                        JobResultResponse response = appService.getResult(jobId);
                        return toCallToolResult(response);
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private CallToolResult toCallToolResult(JobResultResponse response) {
        return switch (response.type()) {
            case NOT_FOUND -> CallToolResult.builder()
                    .addTextContent("Error: Job not found: " + response.jobId())
                    .isError(true)
                    .build();
            case NOT_READY -> CallToolResult.builder()
                    .addTextContent("Job is still " + response.content()
                            + ". Use `get_job_status` to check progress.")
                    .isError(false)
                    .build();
            case FAILED -> CallToolResult.builder()
                    .addTextContent("# Job Failed\n\nError: " + response.content())
                    .isError(true)
                    .build();
            case CANCELLED -> CallToolResult.builder()
                    .addTextContent("Job was cancelled.")
                    .isError(false)
                    .build();
            case MISSING_RESULT -> CallToolResult.builder()
                    .addTextContent("Error: Result not available for job " + response.jobId())
                    .isError(true)
                    .build();
            case SUCCESS -> CallToolResult.builder()
                    .addTextContent(response.content())
                    .isError(false)
                    .build();
        };
    }
}
