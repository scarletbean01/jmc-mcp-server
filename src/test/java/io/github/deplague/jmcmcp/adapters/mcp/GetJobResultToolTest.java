package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.adapters.infrastructure.JobRepositoryImpl;
import io.github.deplague.jmcmcp.application.service.GetJobResultApplicationService;
import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.async.JobStatus;
import io.github.deplague.jmcmcp.domain.service.GetJobResultService;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GetJobResultToolTest {

    private AsyncJobService asyncJobService;
    private GetJobResultTool tool;

    @BeforeEach
    void setUp() {
        asyncJobService = new AsyncJobService();
        JobRepositoryImpl jobRepository = new JobRepositoryImpl(asyncJobService);
        GetJobResultService getJobResultService = new GetJobResultService();
        GetJobResultApplicationService appService = new GetJobResultApplicationService(jobRepository, getJobResultService);
        tool = new GetJobResultTool(appService);
    }

    @AfterEach
    void tearDown() {
        asyncJobService.shutdown();
    }

    @Test
    void resultReturnsErrorForMissingJob() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_job_result", Map.of("job_id", "nonexistent")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Job not found");
    }

    @Test
    void resultReturnsPendingMessageForRunningJob() throws InterruptedException {
        String jobId = asyncJobService.submit("test_tool", Map.of(), () -> {
            try { Thread.sleep(5000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "done";
        });

        Thread.sleep(100); // let it start

        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_job_result", Map.of("job_id", jobId)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("still");
    }

    @Test
    void resultReturnsOutputForCompletedJob() throws InterruptedException {
        String jobId = asyncJobService.submit("test_tool", Map.of(), () -> "# Analysis Result\n\nDone.");

        long deadline = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < deadline) {
            if (asyncJobService.getJob(jobId).status() == JobStatus.COMPLETED) break;
            Thread.sleep(50);
        }

        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_job_result", Map.of("job_id", jobId)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Analysis Result");
    }

    @Test
    void resultReturnsErrorForFailedJob() throws InterruptedException {
        String jobId = asyncJobService.submit("test_tool", Map.of(), () -> {
            throw new RuntimeException("something broke");
        });

        long deadline = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < deadline) {
            if (asyncJobService.getJob(jobId).status() == JobStatus.FAILED) break;
            Thread.sleep(50);
        }

        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_job_result", Map.of("job_id", jobId)));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("something broke");
    }

    @Test
    void resultReturnsErrorForMissingArg() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_job_result", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: job_id");
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
