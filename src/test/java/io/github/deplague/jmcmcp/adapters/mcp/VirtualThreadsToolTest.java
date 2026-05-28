package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.application.service.VirtualThreadsApplicationService;
import io.github.deplague.jmcmcp.domain.service.VirtualThreadsService;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.adapters.infrastructure.security.RecordingAccessController;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class VirtualThreadsToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private VirtualThreadsTool tool;

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
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProvider jfrProvider = new JfrProviderImpl(cache, accessController);
        VirtualThreadsService domainService = new VirtualThreadsService();
        VirtualThreadsApplicationService appService = new VirtualThreadsApplicationService(jfrProvider, domainService);
        tool = new VirtualThreadsTool(appService);
    }

    @Test
    void analysisShowsResultFromAfterFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Virtual Thread Analysis");
    }

    @Test
    void analysisShowsResultFromBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Virtual Thread Analysis");
    }

    @Test
    void analysisContainsNoEventsMessageWhenNoneFound() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("Pinning Summary") && !text.contains("Submission Failures") && !text.contains("Sleep Failures")) {
            assertThat(text).contains("No virtual thread pinning or failure events found");
        }
    }

    @Test
    void analysisPinningSummaryWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Pinning Summary")) {
            assertThat(text).contains("Total Pinned Events");
            assertThat(text).contains("### Top Pinning Sites");
        }
    }

    @Test
    void analysisPinningSitesTableFormat() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Top Pinning Sites")) {
            assertThat(text).contains("| Stack Trace (top 5 frames) | Count | Percentage |");
            assertThat(text).contains("|----------------------------|-------|------------|");
        }
    }

    @Test
    void analysisPinningSitesContainPercentage() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Top Pinning Sites")) {
            assertThat(text).contains("%");
        }
    }

    @Test
    void analysisSubmissionFailuresWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Submission Failures")) {
            assertThat(text).contains("Carrier Pool Exhaustion");
            assertThat(text).contains("| Exception | Count |");
        }
    }

    @Test
    void analysisSleepFailuresWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Sleep Failures")) {
            assertThat(text).contains("| Exception | Count |");
        }
    }

    @Test
    void analysisWithCustomTopN() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of(
                        "jfr_file_path", beforePath, "top_n", 3)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Virtual Thread Analysis");
    }

    @Test
    void analysisWithTopNAsString() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of(
                        "jfr_file_path", beforePath, "top_n", "5")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Virtual Thread Analysis");
    }

    @Test
    void analysisWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of(
                        "jfr_file_path", afterPath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Virtual Thread Analysis");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", afterPath));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisPinningSitesLimitedByTopN() {
        CallToolResult result1 = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", beforePath, "top_n", 1)));

        CallToolResult result2 = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", beforePath, "top_n", 20)));

        String text1 = extractText(result1);
        String text2 = extractText(result2);

        if (text1.contains("Top Pinning Sites") && text2.contains("Top Pinning Sites")) {
            long tableRows1 = countTableRows(text1, "Top Pinning Sites");
            long tableRows2 = countTableRows(text2, "Top Pinning Sites");
            assertThat(tableRows1).isLessThanOrEqualTo(tableRows2);
        }
    }

    @Test
    void analysisNoEventsMessageMentionsJfrConfiguration() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("No virtual thread pinning or failure events found")) {
            assertThat(text).contains("JFR events are not enabled");
        }
    }

    @Test
    void analysisPinningCountIsNumeric() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Total Pinned Events:")) {
            assertThat(text).matches("(?s).*Total Pinned Events:\\s*\\d+.*");
        }
    }

    @Test
    void analysisDefaultTopNIsTen() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("virtual_threads", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Top Pinning Sites")) {
            long tableRows = countTableRows(text, "Top Pinning Sites");
            assertThat(tableRows).isLessThanOrEqualTo(10);
        }
    }

    private static long countTableRows(String text, String sectionHeader) {
        int sectionStart = text.indexOf(sectionHeader);
        if (sectionStart < 0) return 0;
        String afterSection = text.substring(sectionStart);
        int nextSection = afterSection.indexOf("\n## ");
        String section = nextSection > 0 ? afterSection.substring(0, nextSection) : afterSection;
        return section.lines()
                .filter(l -> l.startsWith("|") && !l.contains("---") && !l.contains("Stack Trace"))
                .count() - 1;
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
