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

class DiffStackTracesToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private JfrAnalysisService service;
    private DiffStackTracesTool tool;

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
        tool = new DiffStackTracesTool(service);
    }

    @Test
    void diffShowsResultForBeforeVsAfter() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Stack Trace Diff");
    }

    @Test
    void diffContainsRecordingContext() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Recording Context");
        assertThat(text).contains("| Recording | Duration | Total Samples | Samples/sec |");
        assertThat(text).contains("| Baseline |");
        assertThat(text).contains("| Target |");
    }

    @Test
    void diffContainsNewMethodsSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## New Methods");
    }

    @Test
    void diffContainsDisappearedMethodsSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Disappeared Methods");
    }

    @Test
    void diffContainsChangedProminenceSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Changed Prominence");
    }

    @Test
    void diffContainsStableMethodsSection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Stable Methods");
    }

    @Test
    void diffContainsAgentHint() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("<agent_hint>");
    }

    @Test
    void diffWithPackagePrefix() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath,
                        "package_prefix", "java")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Stack Trace Diff");
    }

    @Test
    void diffWithTopNParameter() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath,
                        "top_n", 5)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Stack Trace Diff");
    }

    @Test
    void diffCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                "baseline_jfr_path", beforePath,
                "target_jfr_path", afterPath));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void diffReturnsErrorForMissingBaselinePath() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "target_jfr_path", afterPath)));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: baseline_jfr_path");
    }

    @Test
    void diffReturnsErrorForMissingTargetPath() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath)));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: target_jfr_path");
    }

    @Test
    void diffReturnsErrorForMissingBaselineFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", "/nonexistent/path.jfr",
                        "target_jfr_path", afterPath)));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void diffReturnsErrorForMissingTargetFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("diff_stack_traces", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
