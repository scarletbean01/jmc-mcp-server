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

class GcDetailToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private JfrAnalysisService service;
    private GcDetailTool tool;

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
        tool = new GcDetailTool(service);
    }

    @Test
    void analysisDoesNotThrowClassCastException() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_detail", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Detailed GC Analysis");
    }

    @Test
    void analysisWithBeforeJfrDoesNotThrow() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_detail", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Detailed GC Analysis");
    }

    @Test
    void analysisContainsGenerationalSummary() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_detail", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Generational Summary");
    }

    @Test
    void analysisContainsPhaseBreakdownWhenAllSelected() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_detail", Map.of(
                        "jfr_file_path", afterPath,
                        "detail_level", "all"
                )));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Pause Phase Breakdown");
    }

    @Test
    void analysisContainsHeapTrendsWhenAllSelected() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_detail", Map.of(
                        "jfr_file_path", afterPath,
                        "detail_level", "all"
                )));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Heap Trends");
    }

    @Test
    void analysisOnlyPhasesWhenRequested() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_detail", Map.of(
                        "jfr_file_path", afterPath,
                        "detail_level", "phases"
                )));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Detailed GC Analysis");
        assertThat(text).contains("## Pause Phase Breakdown");
    }

    @Test
    void analysisOnlyHeapTrendsWhenRequested() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_detail", Map.of(
                        "jfr_file_path", afterPath,
                        "detail_level", "heap_trends"
                )));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Detailed GC Analysis");
        assertThat(text).contains("## Heap Trends");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("gc_detail",
                Map.of("jfr_file_path", afterPath));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_detail", Map.of("jfr_file_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_detail", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: jfr_file_path");
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
