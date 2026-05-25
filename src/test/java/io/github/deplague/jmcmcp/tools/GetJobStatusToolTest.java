package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.async.JobStatus;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GetJobStatusToolTest {

    private AsyncJobService asyncJobService;
    private GetJobStatusTool tool;

    @BeforeEach
    void setUp() {
        asyncJobService = new AsyncJobService();
        tool = new GetJobStatusTool(asyncJobService);
    }

    @AfterEach
    void tearDown() {
        asyncJobService.shutdown();
    }

    @Test
    void statusReturnsErrorForMissingJob() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_job_status", Map.of("job_id", "nonexistent")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Job not found");
    }

    @Test
    void statusReturnsJobInfo() throws InterruptedException {
        String jobId = asyncJobService.submit("test_tool", Map.of("key", "value"), () -> "result");

        // Wait for completion
        long deadline = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < deadline) {
            if (asyncJobService.getJob(jobId).status() == JobStatus.COMPLETED) break;
            Thread.sleep(50);
        }

        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_job_status", Map.of("job_id", jobId)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Job Status");
        assertThat(text).contains("`" + jobId + "`");
        assertThat(text).contains("`test_tool`");
        assertThat(text).contains("COMPLETED");
        assertThat(text).contains("Use `get_job_result`");
    }

    @Test
    void statusReturnsErrorForMissingArg() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_job_status", Map.of()));

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
