package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.HealthCheckApplicationService;
import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.domain.service.HealthCheckService;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrRecordingCache;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HealthCheckToolTest {

    private JfrRecordingCache cache;
    private AsyncJobService asyncJobService;
    private HealthCheckTool tool;

    @BeforeEach
    void setUp() {
        cache = new JfrRecordingCache();
        asyncJobService = new AsyncJobService();
        HealthCheckService healthCheckService = new HealthCheckService();
        HealthCheckApplicationService appService = new HealthCheckApplicationService(cache, asyncJobService, healthCheckService);
        tool = new HealthCheckTool(appService);
    }

    @Test
    void healthCheckReturnsSuccess() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("health_check", Map.of()));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Server Health Check");
    }

    @Test
    void healthCheckContainsStatusSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("health_check", Map.of()));

        String text = extractText(result);
        assertThat(text).contains("## Status");
        assertThat(text).contains("**Overall:**");
        assertThat(text).contains("**Uptime:**");
    }

    @Test
    void healthCheckContainsJvmMemorySection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("health_check", Map.of()));

        String text = extractText(result);
        assertThat(text).contains("## JVM Memory");
        assertThat(text).contains("| Heap |");
    }

    @Test
    void healthCheckContainsJvmThreadsSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("health_check", Map.of()));

        String text = extractText(result);
        assertThat(text).contains("## JVM Threads");
        assertThat(text).contains("**Active threads:**");
    }

    @Test
    void healthCheckContainsRecordingCacheSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("health_check", Map.of()));

        String text = extractText(result);
        assertThat(text).contains("## Recording Cache");
        assertThat(text).contains("**Cached recordings:**");
    }

    @Test
    void healthCheckContainsAsyncJobSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("health_check", Map.of()));

        String text = extractText(result);
        assertThat(text).contains("## Async Job Queue");
        assertThat(text).contains("**Active jobs:**");
    }

    @Test
    void healthCheckContainsAgentHint() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("health_check", Map.of()));

        String text = extractText(result);
        assertThat(text).contains("<agent_hint>");
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
