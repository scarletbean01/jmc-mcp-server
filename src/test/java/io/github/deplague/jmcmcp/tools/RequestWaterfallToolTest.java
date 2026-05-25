package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RequestWaterfallToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private JfrAnalysisService service;
    private RequestWaterfallTool tool;

    @BeforeAll
    static void resolveJfrFiles() {
        afterPath = resolveJfr("after.jfr");
        beforePath = resolveJfr("before.jfr");
    }

    private static String resolveJfr(String name) {
        File file = new File(name);
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir"), name);
        }
        assertThat(file).exists();
        return file.getAbsolutePath();
    }

    @BeforeEach
    void setUp() {
        cache = new JfrRecordingCache();
        service = new JfrAnalysisService(cache);
        tool = new RequestWaterfallTool(service);
    }

    @Test
    void waterfallShowsResultFromAfterFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", afterPath,
                        "thread_name", ".*")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Request Waterfall");
    }

    @Test
    void waterfallShowsResultFromBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", beforePath,
                        "thread_name", ".*")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Request Waterfall");
    }

    @Test
    void waterfallContainsThreadSummary() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", beforePath,
                        "thread_name", ".*")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Thread Summary");
        assertThat(text).contains("**Matched Thread(s):**");
        assertThat(text).contains("**Total Events:**");
        assertThat(text).contains("**Time Span:**");
    }

    @Test
    void waterfallContainsWaterfallTimeline() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", beforePath,
                        "thread_name", ".*")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Waterfall Timeline");
        assertThat(text).contains("| Time | Event Type | Phase | Duration | Detail | Top Frame |");
    }

    @Test
    void waterfallContainsPhaseBreakdown() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", beforePath,
                        "thread_name", ".*")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Phase Breakdown");
        assertThat(text).contains("| Phase | Total Time | % of Recorded | Event Count |");
    }

    @Test
    void waterfallContainsAgentHint() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", beforePath,
                        "thread_name", ".*")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("<agent_hint>");
    }

    @Test
    void waterfallWithMaxEventsLimit() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", beforePath,
                        "thread_name", ".*",
                        "max_events", 5)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Request Waterfall");
    }

    @Test
    void waterfallWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", afterPath,
                        "thread_name", ".*",
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Request Waterfall");
    }

    @Test
    void waterfallNoEventsForNonexistentThread() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", beforePath,
                        "thread_name", "ZZZNonExistentThreadZZZ")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("No events found for thread pattern");
    }

    @Test
    void waterfallReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", "/nonexistent/path.jfr",
                        "thread_name", "main")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void waterfallReturnsErrorForMissingThreadName() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", beforePath)));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: thread_name");
    }

    @Test
    void waterfallReturnsErrorForMissingJfrFilePath() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "thread_name", "main")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void waterfallCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                "jfr_file_path", afterPath,
                "thread_name", ".*"));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void waterfallWithExactThreadName() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_request_waterfall", Map.of(
                        "jfr_file_path", beforePath,
                        "thread_name", "main")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Request Waterfall");
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
