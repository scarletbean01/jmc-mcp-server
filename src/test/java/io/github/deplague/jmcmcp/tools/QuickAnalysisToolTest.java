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

class QuickAnalysisToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private JfrAnalysisService service;
    private QuickAnalysisTool tool;

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
        tool = new QuickAnalysisTool(service);
    }

    @Test
    void quickAnalysisShowsResultFromAfterFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Quick Analysis Dashboard");
    }

    @Test
    void quickAnalysisShowsResultFromBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Quick Analysis Dashboard");
    }

    @Test
    void quickAnalysisContainsRecordingOverview() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Recording Overview");
        assertThat(text).contains("**Total Events:**");
    }

    @Test
    void quickAnalysisContainsSystemHealth() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## System Health");
    }

    @Test
    void quickAnalysisContainsSeverityFindings() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Severity-Classified Findings");
    }

    @Test
    void quickAnalysisContainsRecommendedNextSteps() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Recommended Next Steps");
    }

    @Test
    void quickAnalysisAutoFocus() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of(
                        "jfr_file_path", beforePath,
                        "focus", "auto")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Quick Analysis Dashboard");
    }

    @Test
    void quickAnalysisCpuFocus() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of(
                        "jfr_file_path", beforePath,
                        "focus", "cpu")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## CPU Analysis (Focus)");
    }

    @Test
    void quickAnalysisMemoryFocus() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of(
                        "jfr_file_path", beforePath,
                        "focus", "memory")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## GC Analysis (Focus)");
    }

    @Test
    void quickAnalysisLatencyFocus() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of(
                        "jfr_file_path", beforePath,
                        "focus", "latency")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Thread Contention (Focus)");
    }

    @Test
    void quickAnalysisLocksFocus() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of(
                        "jfr_file_path", beforePath,
                        "focus", "locks")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Lock Analysis (Focus)");
    }

    @Test
    void quickAnalysisWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of(
                        "jfr_file_path", afterPath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Quick Analysis Dashboard");
    }

    @Test
    void quickAnalysisReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of("jfr_file_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void quickAnalysisReturnsErrorForMissingJfrFilePath() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("quick_analysis", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void quickAnalysisCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("quick_analysis", Map.of("jfr_file_path", afterPath));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
