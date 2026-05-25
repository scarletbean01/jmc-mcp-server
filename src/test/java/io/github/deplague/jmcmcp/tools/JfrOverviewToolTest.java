package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService.RecordingOverview;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JfrOverviewToolTest {

    private static String jfrFilePath;
    private JfrRecordingCache cache;
    private JfrAnalysisService service;
    private JfrOverviewTool tool;

    @BeforeAll
    static void resolveJfrFile() {
        File file = new File("after.jfr");
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir"), "after.jfr");
        }
        assertThat(file).exists();
        jfrFilePath = file.getAbsolutePath();
    }

    @BeforeEach
    void setUp() {
        cache = new JfrRecordingCache();
        service = new JfrAnalysisService(cache);
        tool = new JfrOverviewTool(service);
    }

    @Test
    void overviewShowsResultFromJfrFile() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "jfr_overview",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# JFR Recording Overview");
        assertThat(text).contains("**File:**");
        assertThat(text).contains("**Duration:**");
        assertThat(text).contains("## Event Summary");
        assertThat(text).contains("**Total Events (full file):**");
    }

    @Test
    void overviewContainsFileName() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "jfr_overview",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("after.jfr");
    }

    @Test
    void overviewDurationIsPositive() throws Exception {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "jfr_overview",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("**Duration:**");
        RecordingOverview overview = service.getOverview(jfrFilePath);
        assertThat(overview.durationSeconds()).isGreaterThan(0);
    }

    @Test
    void overviewWithTimeRangeShowsFilteredEvents() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "jfr_overview",
                Map.of(
                        "jfr_file_path", jfrFilePath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2027-12-31T23:59:59Z"
                )
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("Filtered Events");
    }

    @Test
    void overviewReturnsErrorForMissingFile() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "jfr_overview",
                Map.of("jfr_file_path", "/nonexistent/path.jfr")
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Error:");
    }

    @Test
    void overviewReturnsErrorForMissingArgument() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "jfr_overview",
                Map.of()
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void overviewCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "jfr_overview",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void overviewEventCountsArePositive() throws Exception {
        RecordingOverview overview = service.getOverview(jfrFilePath);

        assertThat(overview.eventCounts()).isNotEmpty();
        long totalEvents = overview.eventCounts().values().stream().mapToLong(Long::longValue).sum();
        assertThat(totalEvents).isGreaterThan(0);
    }

    @Test
    void overviewWithoutTimeRangeDoesNotShowFilteredLabel() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "jfr_overview",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).doesNotContain("Filtered Events");
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}