package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.GetJobStatusApplicationService;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for polling the status of an asynchronous analysis job.
 * Delegates to the application layer and formats results as Markdown.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class GetJobStatusTool implements McpTool {

    private static final String NAME = "get_job_status";

    private final GetJobStatusApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Poll the status of an asynchronous analysis job submitted via the async parameter. " +
                                                "Returns PENDING, RUNNING, COMPLETED, FAILED, or CANCELLED."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "job_id",
                                                        SchemaUtil.stringProp(
                                                                "The job ID returned when the async job was submitted"
                                                        )
                                                ),
                                                SchemaUtil.required("job_id")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String jobId = SchemaUtil.getString(
                                request.arguments(),
                                "job_id"
                        );
                        String markdown = appService.getJobStatus(jobId);

                        if (markdown == null) {
                            return CallToolResult.builder()
                                    .addTextContent("Error: Job not found: " + jobId)
                                    .isError(true)
                                    .build();
                        }

                        return CallToolResult.builder()
                                .addTextContent(markdown)
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
