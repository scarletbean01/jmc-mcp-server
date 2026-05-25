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

class CompareRecordingsToolTest {

    private static String baselinePath;
    private static String targetPath;

    private static JfrRecordingCache cache;
    private static JfrAnalysisService service;
    private CompareRecordingsTool tool;

    @BeforeAll
    static void resolveJfrFiles() {
        baselinePath = resolveJfr("before.jfr");
        targetPath = resolveJfr("after.jfr");
        cache = new JfrRecordingCache();
        service = new JfrAnalysisService(cache);
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
        tool = new CompareRecordingsTool(service);
    }

    @Test
    void compareShowsResultFromTwoJfrFiles() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Comprehensive JFR Recording Comparison");
        assertThat(text).contains("**Baseline:**");
        assertThat(text).contains("**Target:**");
    }

    @Test
    void compareContainsCategorySections() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Summary of Major Findings");
        assertThat(text).contains("### CPU");
        assertThat(text).contains("### Garbage Collection");
        assertThat(text).contains("### Memory");
        assertThat(text).contains("### Contention");
        assertThat(text).contains("### I/O");
        assertThat(text).contains("### Runtime");
        assertThat(text).contains("### JVM Internals");
    }

    @Test
    void compareContainsMetricTables() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("| Metric | Baseline | Target | Delta | Stat |");
    }

    @Test
    void compareContainsRulesSection() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## JMC Rules Comparison");
    }

    @Test
    void compareContainsCpuMetrics() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("Avg Machine Total");
        assertThat(text).contains("Avg JVM User");
        assertThat(text).contains("Avg JVM System");
    }

    @Test
    void compareContainsGcMetrics() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("Total GC Pause Time");
        assertThat(text).contains("Max GC Pause Time");
    }

    @Test
    void compareContainsRegressionSections() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## CPU Hotspot Deltas");
        assertThat(text).contains("## Allocation Deltas");
        assertThat(text).contains("## Lock Contention Deltas");
        assertThat(text).contains("## Exception & Error Deltas");
    }

    @Test
    void compareContainsNormalizationNote() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("normalized per-second");
    }

    @Test
    void compareContainsFileNames() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("before.jfr");
        assertThat(text).contains("after.jfr");
    }

    @Test
    void compareReturnsErrorForMissingBaselineFile() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", "/nonexistent/baseline.jfr", "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Error:");
    }

    @Test
    void compareReturnsErrorForMissingTargetFile() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", "/nonexistent/target.jfr")
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Error:");
    }

    @Test
    void compareReturnsErrorForMissingBaselineArgument() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Missing required argument: baseline_jfr_path");
    }

    @Test
    void compareReturnsErrorForMissingTargetArgument() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Missing required argument: target_jfr_path");
    }

    @Test
    void compareCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void compareSameFileAsBaselineAndTarget() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", baselinePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Comprehensive JFR Recording Comparison");
        assertThat(text).contains("0.00%");
    }

    @Test
    void compareDeltaValuesContainPercentSign() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "smart_compare_recordings",
                Map.of("baseline_jfr_path", baselinePath, "target_jfr_path", targetPath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("%");
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}