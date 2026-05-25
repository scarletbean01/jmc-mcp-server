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

class StackTraceSearchToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private JfrAnalysisService service;
    private StackTraceSearchTool tool;

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
        tool = new StackTraceSearchTool(service);
    }

    @Test
    void searchShowsResultFromAfterFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", afterPath,
                        "class_pattern", ".*java.*")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Stack Trace Search");
    }

    @Test
    void searchShowsResultFromBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", beforePath,
                        "class_pattern", ".*java.*")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Stack Trace Search");
    }

    @Test
    void searchContainsPatternAndEventTypesInOutput() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", beforePath,
                        "class_pattern", ".*java.*")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("**Pattern:**");
        assertThat(text).contains("**Event types searched:**");
        assertThat(text).contains("**Total matches found:**");
    }

    @Test
    void searchContainsClassDistributionSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", beforePath,
                        "class_pattern", ".*java.*")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Class Distribution");
        assertThat(text).contains("| Event Type | Matches |");
    }

    @Test
    void searchWithEventTypeFilter() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", beforePath,
                        "class_pattern", ".*java.*",
                        "event_type", "jdk.ExecutionSample")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Stack Trace Search");
        assertThat(text).contains("jdk.ExecutionSample");
    }

    @Test
    void searchWithLimitParameter() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", beforePath,
                        "class_pattern", ".*java.*",
                        "limit", 5)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Stack Trace Search");
    }

    @Test
    void searchWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", afterPath,
                        "class_pattern", ".*java.*",
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Stack Trace Search");
    }

    @Test
    void searchReturnsErrorForInvalidRegex() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", beforePath,
                        "class_pattern", "[invalid")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("Invalid regex pattern");
    }

    @Test
    void searchReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", "/nonexistent/path.jfr",
                        "class_pattern", ".*DAO.*")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void searchReturnsErrorForMissingClassPattern() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", beforePath)));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: class_pattern");
    }

    @Test
    void searchReturnsErrorForMissingJfrFilePath() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "class_pattern", ".*DAO.*")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void searchCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                "jfr_file_path", afterPath,
                "class_pattern", ".*java.*"));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void searchNoMatchesReturnsEmptyDistribution() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", beforePath,
                        "class_pattern", "ZZZNonExistentClassZZZ")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Stack Trace Search");
        assertThat(text).contains("**Total matches found:** 0");
    }

    @Test
    void searchResultContainsAgentHintWhenMatchesFound() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_stack_trace_search", Map.of(
                        "jfr_file_path", beforePath,
                        "class_pattern", ".*java.*")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Class Distribution") && text.contains("| `jdk.")) {
            assertThat(text).contains("<agent_hint>");
        }
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
