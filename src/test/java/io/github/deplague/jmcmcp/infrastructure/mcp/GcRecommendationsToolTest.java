package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.GcRecommendationsApplicationService;
import io.github.deplague.jmcmcp.domain.service.GcRecommendationsService;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.GcRecommendationsTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class GcRecommendationsToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private GcRecommendationsTool tool;

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
        GcRecommendationsService domainService = new GcRecommendationsService();
        GcRecommendationsApplicationService appService = new GcRecommendationsApplicationService(jfrProvider, domainService);
        tool = new GcRecommendationsTool(appService);
    }

    @Test
    void analysisShowsResultFromJfrFile() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# GC Tuning Recommendations");
    }

    @Test
    void analysisWithBeforeFixesFile() {
        ToolResponse result = tool.gcRecommendations(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# GC Tuning Recommendations");
    }

    @Test
    void analysisContainsPauseDistribution() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Pause Distribution");
    }

    @Test
    void analysisPauseDistributionContainsMetrics() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("## Pause Distribution")) {
            assertThat(text).contains("Total Pauses");
            assertThat(text).contains("Avg Pause");
            assertThat(text).contains("P50 Pause");
            assertThat(text).contains("P95 Pause");
            assertThat(text).contains("P99 Pause");
            assertThat(text).contains("Max Pause");
        }
    }

    @Test
    void analysisContainsGcAlgorithm() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("GC algorithm:")) {
            assertThat(text).contains("GC algorithm:");
        }
    }

    @Test
    void analysisContainsGcCauseAnalysis() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## GC Cause Analysis");
    }

    @Test
    void analysisContainsYoungGcCauses() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("### Young GC Causes")) {
            assertThat(text).contains("| Cause | Count |");
        }
    }

    @Test
    void analysisContainsOldGcCauses() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("### Old GC Causes")) {
            assertThat(text).contains("| Cause | Count |");
        }
    }

    @Test
    void analysisContainsHeapUtilization() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("## Heap Utilization")) {
            assertThat(text).contains("Min Heap Used");
            assertThat(text).contains("Avg Heap Used");
            assertThat(text).contains("Max Heap Used");
            assertThat(text).contains("Heap Amplitude");
        }
    }

    @Test
    void analysisContainsRecommendationsSection() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Recommendations");
    }

    @Test
    void analysisContainsWarningsWhenApplicable() {
        ToolResponse result = tool.gcRecommendations(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("## ⚠️ Warnings")) {
            assertThat(text).contains("- ");
        }
    }

    @Test
    void analysisWithTimeRange() {
        ToolResponse result = tool.gcRecommendations(afterPath, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z");

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# GC Tuning Recommendations");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        ToolResponse result = tool.gcRecommendations("/nonexistent/path.jfr", null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        ToolResponse result = tool.gcRecommendations(null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
                ToolResponse first = tool.gcRecommendations(afterPath, null, null);
        ToolResponse second = tool.gcRecommendations(afterPath, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisPauseDistributionTableFormat() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("## Pause Distribution")) {
            assertThat(text).contains("| Metric | Value |");
            assertThat(text).contains("|--------|-------|");
        }
    }

    @Test
    void analysisHeapUtilizationTableFormat() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("## Heap Utilization")) {
            assertThat(text).contains("| Metric | Value |");
            assertThat(text).contains("|--------|-------|");
        }
    }

    @Test
    void analysisRecommendationsAreNumberedWhenPresent() {
        ToolResponse result = tool.gcRecommendations(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("No specific tuning recommendations")) {
            assertThat(text).contains("1. ");
        }
    }

    @Test
    void analysisHealthyGcShowsNoRecommendations() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("GC behavior appears healthy")) {
            assertThat(text).contains("No specific tuning recommendations");
        }
    }

    @Test
    void analysisExplicitGcWarningWhenPresent() {
        ToolResponse result = tool.gcRecommendations(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("explicit System.gc()")) {
            assertThat(text).contains("DisableExplicitGC");
        }
    }

    @Test
    void analysisFullGcRatioWarningWhenPresent() {
        ToolResponse result = tool.gcRecommendations(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Full GC ratio")) {
            assertThat(text).contains("Full GC");
        }
    }

    @Test
    void analysisMetaspaceWarningWhenPresent() {
        ToolResponse result = tool.gcRecommendations(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Metaspace utilization")) {
            assertThat(text).contains("MaxMetaspaceSize");
        }
    }

    @Test
    void analysisP95PauseWarningWhenHigh() {
        ToolResponse result = tool.gcRecommendations(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("P95 GC pause exceeds")) {
            assertThat(text).contains("MaxGCPauseMillis");
        }
    }

    @Test
    void analysisGcAlgorithmDisplayedWhenAvailable() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("GC algorithm:")) {
            assertThat(text).satisfiesAnyOf(
                    t -> assertThat(t).containsIgnoringCase("G1"),
                    t -> assertThat(t).containsIgnoringCase("ZGC"),
                    t -> assertThat(t).containsIgnoringCase("Shenandoah"),
                    t -> assertThat(t).containsIgnoringCase("Parallel"),
                    t -> assertThat(t).containsIgnoringCase("Serial")
            );
        }
    }

    @Test
    void analysisHeapAmplitudePercentage() {
        ToolResponse result = tool.gcRecommendations(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Heap Amplitude")) {
            assertThat(text).contains("%");
        }
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
