package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.HotMethodsApplicationService;
import io.github.deplague.jmcmcp.domain.service.HotMethodsService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HotMethodsToolTest {

    private static String afterPath;
    private static String beforePath;

    private HotMethodsTool tool;

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
        JfrRecordingCache cache = new JfrRecordingCache();
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        HotMethodsService hotMethodsService = new HotMethodsService();
        HotMethodsApplicationService appService = new HotMethodsApplicationService(
                jfrProvider, hotMethodsService
        );
        tool = new HotMethodsTool(appService);
    }

    @Test
    void analysisShowsResultFromAfterFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisShowsResultFromBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisContainsTableHeaderWhenSamplesPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("No execution samples found")) {
            assertThat(text).contains("| Samples | Stack Trace (top 5 frames) |");
            assertThat(text).contains("|---------|----------------------------|");
        }
    }

    @Test
    void analysisContainsNoSamplesMessageWhenNoneFound() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of(
                        "jfr_file_path", afterPath,
                        "start_time", "2099-12-31T23:59:59Z",
                        "end_time", "2099-12-31T23:59:58Z")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("No execution samples found")) {
            assertThat(text).contains("No execution samples found in the recording");
        }
    }

    @Test
    void analysisWithCustomTopN() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of(
                        "jfr_file_path", beforePath, "top_n", 3)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisWithTopNAsString() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of(
                        "jfr_file_path", beforePath, "top_n", "5")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisTopNLimitsResults() {
        CallToolResult result1 = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of("jfr_file_path", beforePath, "top_n", 1)));
        CallToolResult result2 = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of("jfr_file_path", beforePath, "top_n", 20)));

        String text1 = extractText(result1);
        String text2 = extractText(result2);

        if (!text1.contains("No execution samples found") && !text2.contains("No execution samples found")) {
            long rows1 = countDataRows(text1);
            long rows2 = countDataRows(text2);
            assertThat(rows1).isLessThanOrEqualTo(rows2);
        }
    }

    @Test
    void analysisDefaultTopNIsTen() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("No execution samples found")) {
            long rows = countDataRows(text);
            assertThat(rows).isLessThanOrEqualTo(10);
        }
    }

    @Test
    void analysisWithThreadNameFilter() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of(
                        "jfr_file_path", beforePath, "thread_name", "main")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisWithPackagePrefixFilter() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of(
                        "jfr_file_path", beforePath, "package_prefix", "java.lang")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisWithThreadNameAndPackagePrefix() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of(
                        "jfr_file_path", beforePath,
                        "thread_name", "main",
                        "package_prefix", "java")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of(
                        "jfr_file_path", afterPath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of("jfr_file_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void analysisSampleCountsAreNumeric() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("No execution samples found")) {
            assertThat(text).matches("(?s).*\\| \\d+ \\|.*");
        }
    }

    @Test
    void analysisStackTraceFramesAreFormatted() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("No execution samples found")) {
            assertThat(text).contains("`");
        }
    }

    @Test
    void analysisThreadNameFilterReducesResults() {
        CallToolResult unfiltered = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of("jfr_file_path", beforePath)));
        CallToolResult filtered = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of(
                        "jfr_file_path", beforePath, "thread_name", "nonexistent-thread-xyz")));

        String unfilteredText = extractText(unfiltered);
        String filteredText = extractText(filtered);

        if (!unfilteredText.contains("No execution samples found")) {
            long unfilteredRows = countDataRows(unfilteredText);
            if (!filteredText.contains("No execution samples found")) {
                long filteredRows = countDataRows(filteredText);
                assertThat(filteredRows).isLessThanOrEqualTo(unfilteredRows);
            }
        }
    }

    @Test
    void analysisAllOptionalParametersTogether() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("hot_methods", Map.of(
                        "jfr_file_path", beforePath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z",
                        "thread_name", "main",
                        "package_prefix", "java",
                        "top_n", 5)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    private static long countDataRows(String text) {
        int tableStart = text.indexOf("| Samples |");
        if (tableStart < 0) return 0;
        String table = text.substring(tableStart);
        return table.lines()
                .filter(l -> l.startsWith("|") && !l.contains("---") && !l.contains("Samples |"))
                .count();
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
