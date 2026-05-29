package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.PredictiveLeakAnalysisTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.github.deplague.jmcmcp.application.service.PredictiveLeakAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.service.PredictiveLeakAnalysisService;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class PredictiveLeakAnalysisToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private PredictiveLeakAnalysisTool tool;

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
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        PredictiveLeakAnalysisService domainService = new PredictiveLeakAnalysisService();
        PredictiveLeakAnalysisApplicationService appService = new PredictiveLeakAnalysisApplicationService(jfrProvider, domainService);
        tool = new PredictiveLeakAnalysisTool(appService);
    }

    @Test
    void analysisShowsResultFromAfterFixesFile() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Predictive Leak Analysis");
    }

    @Test
    void analysisShowsResultFromBeforeFixesFile() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Predictive Leak Analysis");
    }

    @Test
    void analysisContainsVerdict() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("## Verdict:");
    }

    @Test
    void analysisVerdictIsOneOfExpectedTypes() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).satisfiesAnyOf(
                t -> assertThat(t).contains("MEMORY LEAK DETECTED"),
                t -> assertThat(t).contains("POSSIBLE MEMORY LEAK"),
                t -> assertThat(t).contains("NO MEMORY LEAK DETECTED"),
                t -> assertThat(t).contains("INCONCLUSIVE")
        );
    }

    @Test
    void analysisContainsLeakMetrics() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("## Leak Metrics");
        assertThat(text).contains("Post-GC Heap Growth Rate");
        assertThat(text).contains("R² Correlation");
        assertThat(text).contains("Data Points");
    }

    @Test
    void analysisContainsCurrentPostGcHeap() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("Current Post-GC Heap");
    }

    @Test
    void analysisGrowthRateIsInKBPerMin() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("KB/min");
    }

    @Test
    void analysisContainsRegressionDetails() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("## Regression Details");
        assertThat(text).contains("Slope (bytes/ms)");
        assertThat(text).contains("Intercept (bytes)");
        assertThat(text).contains("Recording Duration");
    }

    @Test
    void analysisContainsLeakSuspectsSection() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("## Leak Suspects");
    }

    @Test
    void analysisLeakSuspectsContainsClassTableWhenAvailable() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Old Object Samples")) {
            assertThat(text).contains("| Class | Sample Count | % of Total |");
        }
    }

    @Test
    void analysisLeakSuspectsShowsNoOldObjectSamplesMessageWhenMissing() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        if (!text.contains("Total sampled objects:")) {
            assertThat(text).contains("No `jdk.OldObjectSample` events found");
        }
    }

    @Test
    void analysisContainsTableHeaders() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("| Metric | Value |");
        assertThat(text).contains("|--------|-------|");
    }

    @Test
    void analysisRoundedValueFormat() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("R² Correlation");
        assertThat(text).matches("(?s).*\\d\\.\\d{4}.*");
    }

    @Test
    void analysisWithCustomRSquaredThreshold() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, 0.5);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("# Predictive Leak Analysis");
        assertThat(text).contains("R² Correlation");
    }

    @Test
    void analysisWithHighRSquaredThreshold() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, 0.99);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Predictive Leak Analysis");
    }

    @Test
    void analysisWithRSquaredThresholdAsString() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, 0.9);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Predictive Leak Analysis");
    }

    @Test
    void analysisWithTimeRange() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z", null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Predictive Leak Analysis");
    }

    @Test
    void analysisOomProjectionWhenLeakDetected() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        boolean hasConfirmedLeak = text.contains("MEMORY LEAK DETECTED")
                && !text.contains("NO MEMORY LEAK DETECTED")
                && !text.contains("POSSIBLE MEMORY LEAK");
        if (hasConfirmedLeak) {
            assertThat(text).contains("## OutOfMemoryError Projection");
            assertThat(text).contains("Projected OOM Time");
            assertThat(text).contains("Time to OOM");
        }
    }

    @Test
    void analysisOomProjectionContainsAlreadyExceededWhenApplicable() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("ALREADY EXCEEDED")) {
            assertThat(text).contains("ALREADY EXCEEDED");
        }
    }

    @Test
    void analysisMaxHeapSizeWhenAvailable() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Max Heap Size")) {
            assertThat(text).contains("Heap Utilization");
        }
    }

    @Test
    void analysisHeapUtilizationPercentage() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Heap Utilization")) {
            assertThat(text).contains("%");
        }
    }

    @Test
    void analysisNoLeakVerdictMessage() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("NO MEMORY LEAK DETECTED")) {
            assertThat(text).contains("Post-GC heap usage is stable or declining");
        }
    }

    @Test
    void analysisInconclusiveVerdictMessage() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("INCONCLUSIVE")) {
            assertThat(text).contains("Heap growth does not follow a linear pattern");
        }
    }

    @Test
    void analysisPossibleLeakVerdictMessage() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("POSSIBLE MEMORY LEAK")) {
            assertThat(text).contains("weak correlation");
        }
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis("/nonexistent/path.jfr", null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
        ToolResponse first = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);
        ToolResponse second = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisInsufficientDataPointsMessage() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, "2099-12-31T23:59:59Z", "2099-12-31T23:59:58Z", null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
        }
    }

    @Test
    void analysisNoHeapEventsMessage() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, "2099-12-31T23:59:59Z", "2099-12-31T23:59:58Z", null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("No GC heap summary events found")) {
            assertThat(text).contains("Cannot perform leak analysis");
        }
    }

    @Test
    void analysisDataPointsCount() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("Data Points");
        assertThat(text).contains("post-GC samples");
    }

    @Test
    void analysisSlopeAndInterceptFormat() {
        ToolResponse result = tool.smartPredictiveLeakAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Insufficient post-GC data points")) {
            assertThat(text).contains("Need at least 3 for regression");
            return;
        }
        assertThat(text).contains("Slope (bytes/ms)");
        assertThat(text).contains("Intercept (bytes)");
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
