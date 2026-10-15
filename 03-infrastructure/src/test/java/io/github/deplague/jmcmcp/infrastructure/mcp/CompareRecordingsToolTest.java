package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepositoryImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregatorImpl;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        JfrAccessorRepositoryImpl accessorRepo = new JfrAccessorRepositoryImpl();
        JfrQuantityAggregatorImpl quantityAgg = new JfrQuantityAggregatorImpl(accessorRepo);
        CompareRecordingsService domainService = new CompareRecordingsService(accessorRepo, quantityAgg);
        CompareRecordingsApplicationService appService = new CompareRecordingsApplicationService(jfrProvider, domainService);
        tool = new CompareRecordingsTool(appService);
    }

    @Test
    void compareShowsResultFromTwoJfrFiles() throws Exception {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Comprehensive JFR Recording Comparison");
        assertThat(text).contains("**Baseline:**");
        assertThat(text).contains("**Target:**");
    }

    @Test
    void compareContainsCategorySections() throws Exception {
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
    void compareContainsMetricTables() throws Exception {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("| Metric | Baseline | Target | Delta | Stat |");
    }

    @Test
    void compareContainsRulesSection() throws Exception {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## JMC Rules Comparison");
    }

    @Test
    void compareContainsCpuMetrics() throws Exception {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("Avg Machine Total");
        assertThat(text).contains("Avg JVM User");
        assertThat(text).contains("Avg JVM System");
    }

    @Test
    void compareContainsGcMetrics() throws Exception {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("Total GC Pause Time");
        assertThat(text).contains("Max GC Pause Time");
    }

    @Test
    void compareContainsRegressionSections() throws Exception {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## CPU Hotspot Deltas");
        assertThat(text).contains("## Allocation Deltas");
        assertThat(text).contains("## Lock Contention Deltas");
        assertThat(text).contains("## Exception & Error Deltas");
    }

    @Test
    void compareContainsNormalizationNote() throws Exception {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("normalized per-second");
    }

    @Test
    void compareContainsFileNames() throws Exception {
        ToolResponse result = tool.compareRecordings(baselinePath, targetPath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("before.jfr");
        assertThat(text).contains("after.jfr");
    }

    @Test
    void compareReturnsErrorForMissingBaselineFile() throws Exception {
        assertThatThrownBy(() -> tool.compareRecordings("/nonexistent/baseline.jfr", targetPath))
                .isInstanceOf(Exception.class);
    }

    @Test
    void compareReturnsErrorForMissingTargetFile() throws Exception {
        assertThatThrownBy(() -> tool.compareRecordings(baselinePath, "/nonexistent/target.jfr"))
                .isInstanceOf(Exception.class);
    }

    @Test
    void compareReturnsErrorForMissingBaselineArgument() throws Exception {
        assertThatThrownBy(() -> tool.compareRecordings(null, targetPath))
                .isInstanceOf(Exception.class);
    }

    @Test
    void compareReturnsErrorForMissingTargetArgument() throws Exception {
        assertThatThrownBy(() -> tool.compareRecordings(baselinePath, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void compareCachesResultOnSecondCall() throws Exception {
        ToolResponse first = tool.compareRecordings(baselinePath, targetPath);
        ToolResponse second = tool.compareRecordings(baselinePath, targetPath);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void compareSameFileAsBaselineAndTarget() throws Exception {
        ToolResponse result = tool.compareRecordings(baselinePath, baselinePath);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Comprehensive JFR Recording Comparison");
        assertThat(text).contains("0.00%");
    }

    @Test
    void compareDeltaValuesContainPercentSign() throws Exception {
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
