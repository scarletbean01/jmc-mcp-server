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

class CorrelateToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private JfrAnalysisService service;
    private CorrelateTool tool;

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
        tool = new CorrelateTool(service);
    }

    @Test
    void correlateShowsResultFromAfterFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Cross-Dimensional Correlation Analysis");
    }

    @Test
    void correlateShowsResultFromBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Cross-Dimensional Correlation Analysis");
    }

    @Test
    void correlateContainsLockIoSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of(
                        "jfr_file_path", beforePath,
                        "dimension", "all")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Lock");
    }

    @Test
    void correlateContainsCpuGcSectionWhenAll() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of(
                        "jfr_file_path", beforePath,
                        "dimension", "all")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## CPU");
    }

    @Test
    void correlateDimensionLockIoDbOmitsCpuGc() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of(
                        "jfr_file_path", beforePath,
                        "dimension", "lock_io_db")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Lock");
        assertThat(text).doesNotContain("## CPU \u2194 GC Correlation");
    }

    @Test
    void correlateDimensionCpuGcShowsCpuGcSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of(
                        "jfr_file_path", beforePath,
                        "dimension", "cpu_gc")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## CPU");
    }

    @Test
    void correlateWithTopNParameter() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of(
                        "jfr_file_path", beforePath,
                        "top_n", 3)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Cross-Dimensional Correlation Analysis");
    }

    @Test
    void correlateWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of(
                        "jfr_file_path", afterPath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Cross-Dimensional Correlation Analysis");
    }

    @Test
    void correlateContainsAgentHint() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("<agent_hint>");
    }

    @Test
    void correlateReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of("jfr_file_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void correlateReturnsErrorForMissingJfrFilePath() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_correlate", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void correlateCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("smart_correlate", Map.of("jfr_file_path", afterPath));

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
