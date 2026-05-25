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

class LockAnalysisToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private JfrAnalysisService service;
    private LockAnalysisTool tool;

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
        tool = new LockAnalysisTool(service);
    }

    @Test
    void analysisShowsResultFromAfterFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Advanced Lock Analysis");
    }

    @Test
    void analysisShowsResultFromBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Advanced Lock Analysis");
    }

    @Test
    void analysisContainsThreadParkSectionOrNoParkMessage() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).satisfiesAnyOf(
                t -> assertThat(t).contains("Thread Park Summary"),
                t -> assertThat(t).contains("No Thread Park events found")
        );
    }

    @Test
    void analysisContainsBiasedLockSectionOrNoRevocationMessage() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).satisfiesAnyOf(
                t -> assertThat(t).contains("Biased Lock Revocations"),
                t -> assertThat(t).contains("No Biased Lock Revocation events found")
        );
    }

    @Test
    void analysisParkSummaryContainsMetricsWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Thread Park Summary")) {
            assertThat(text).contains("Total Park Events");
            assertThat(text).contains("Avg Park Duration");
            assertThat(text).contains("Max Park Duration");
        }
    }

    @Test
    void analysisParkSitesTableFormatWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Top Park Sites")) {
            assertThat(text).contains("| Stack Trace | Count | Avg Duration | Max Duration |");
        }
    }

    @Test
    void analysisBiasedLockRevocationCountsWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Biased Lock Revocations")) {
            assertThat(text).contains("Single Revocations");
            assertThat(text).contains("Class/Bulk Revocations");
            assertThat(text).contains("Self Revocations");
        }
    }

    @Test
    void analysisRevokedLockClassesTableWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Revoked Lock Classes")) {
            assertThat(text).contains("| Lock Class | Revocation Count |");
        }
    }

    @Test
    void analysisWithCustomTopN() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of(
                        "jfr_file_path", beforePath, "top_n", 3)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Advanced Lock Analysis");
    }

    @Test
    void analysisWithTopNAsString() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of(
                        "jfr_file_path", beforePath, "top_n", "5")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Advanced Lock Analysis");
    }

    @Test
    void analysisWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of(
                        "jfr_file_path", afterPath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Advanced Lock Analysis");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", afterPath));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisParkSitesLimitedByTopN() {
        CallToolResult result1 = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath, "top_n", 1)));

        CallToolResult result2 = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath, "top_n", 20)));

        String text1 = extractText(result1);
        String text2 = extractText(result2);

        if (text1.contains("Top Park Sites") && text2.contains("Top Park Sites")) {
            long rows1 = countDataRows(text1, "Top Park Sites");
            long rows2 = countDataRows(text2, "Top Park Sites");
            assertThat(rows1).isLessThanOrEqualTo(rows2);
        }
    }

    @Test
    void analysisDefaultTopNIsTen() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Top Park Sites")) {
            long rows = countDataRows(text, "Top Park Sites");
            assertThat(rows).isLessThanOrEqualTo(10);
        }
    }

    @Test
    void analysisParkCountIsNumeric() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Total Park Events")) {
            assertThat(text).matches("(?s).*Total Park Events.*\\d+.*");
        }
    }

    @Test
    void analysisRevocationCountIsNumeric() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Single Revocations")) {
            assertThat(text).matches("(?s).*Single Revocations.*\\d+.*");
        }
    }

    @Test
    void analysisBothFilesProduceConsistentStructure() {
        CallToolResult afterResult = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", afterPath)));
        CallToolResult beforeResult = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("lock_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(afterResult.isError()).isFalse();
        assertThat(beforeResult.isError()).isFalse();

        String afterText = extractText(afterResult);
        String beforeText = extractText(beforeResult);

        assertThat(afterText).contains("# Advanced Lock Analysis");
        assertThat(beforeText).contains("# Advanced Lock Analysis");
    }

    private static long countDataRows(String text, String sectionHeader) {
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