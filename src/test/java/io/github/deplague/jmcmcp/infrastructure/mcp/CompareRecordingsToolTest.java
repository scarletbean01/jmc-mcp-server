package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.CompareRecordingsApplicationService;
import io.github.deplague.jmcmcp.domain.service.CompareRecordingsService;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.CompareRecordingsTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class CompareRecordingsToolTest {

    private static String baselinePath;
    private static String targetPath;

    private static JfrRecordingCache cache;
    private CompareRecordingsTool tool;

    @BeforeAll
    static void resolveJfrFiles() {
        baselinePath = resolveJfr("before.jfr");
        targetPath = resolveJfr("after.jfr");
        cache = new JfrRecordingCache();
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
        CompareRecordingsService domainService = new CompareRecordingsService();
        CompareRecordingsApplicationService appService = new CompareRecordingsApplicationService(jfrProvider, domainService);
        tool = new CompareRecordingsTool(appService);
    }

    @Test
    void compareShowsResultFromTwoJfrFiles() {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Comprehensive JFR Recording Comparison");
        assertThat(text).contains("**Baseline:**");
        assertThat(text).contains("**Target:**");
    }

    @Test
    void compareContainsCategorySections() {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

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
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("| Metric | Baseline | Target | Delta | Stat |");
    }

    @Test
    void compareContainsRulesSection() {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## JMC Rules Comparison");
    }

    @Test
    void compareContainsCpuMetrics() {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("Avg Machine Total");
        assertThat(text).contains("Avg JVM User");
        assertThat(text).contains("Avg JVM System");
    }

    @Test
    void compareContainsGcMetrics() {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("Total GC Pause Time");
        assertThat(text).contains("Max GC Pause Time");
    }

    @Test
    void compareContainsRegressionSections() {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## CPU Hotspot Deltas");
        assertThat(text).contains("## Allocation Deltas");
        assertThat(text).contains("## Lock Contention Deltas");
        assertThat(text).contains("## Exception & Error Deltas");
    }

    @Test
    void compareContainsNormalizationNote() {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("normalized per-second");
    }

    @Test
    void compareContainsFileNames() {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("before.jfr");
        assertThat(text).contains("after.jfr");
    }

    @Test
    void compareReturnsErrorForMissingBaselineFile() {
        ToolResponse result = tool.compareRecordings("/nonexistent/baseline.jfr", targetPath);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Error:");
    }

    @Test
    void compareReturnsErrorForMissingTargetFile() {
        ToolResponse result = tool.compareRecordings(baselinePath, "/nonexistent/target.jfr");

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Error:");
    }

    @Test
    void compareReturnsErrorForMissingBaselineArgument() {
        ToolResponse result = tool.compareRecordings(null, targetPath);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Error: Path cannot be null or blank");
    }

    @Test
    void compareReturnsErrorForMissingTargetArgument() {
        ToolResponse result = tool.compareRecordings(baselinePath, null);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Error: Path cannot be null or blank");
    }

    @Test
    void compareCachesResultOnSecondCall() {
        ToolResponse first = tool.compareRecordings(baselinePath, targetPath);
        ToolResponse second = tool.compareRecordings(baselinePath, targetPath);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void compareSameFileAsBaselineAndTarget() {
        ToolResponse result = tool.compareRecordings(baselinePath, baselinePath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Comprehensive JFR Recording Comparison");
        assertThat(text).contains("0.00%");
    }

    @Test
    void compareDeltaValuesContainPercentSign() {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("%");
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
