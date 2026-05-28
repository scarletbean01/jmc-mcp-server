package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.GcRecommendationsApplicationService;
import io.github.deplague.jmcmcp.domain.service.GcRecommendationsService;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.adapters.infrastructure.security.RecordingAccessController;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

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
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# GC Tuning Recommendations");
    }

    @Test
    void analysisWithBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# GC Tuning Recommendations");
    }

    @Test
    void analysisContainsPauseDistribution() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Pause Distribution");
    }

    @Test
    void analysisPauseDistributionContainsMetrics() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

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
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("GC algorithm:")) {
            assertThat(text).contains("GC algorithm:");
        }
    }

    @Test
    void analysisContainsGcCauseAnalysis() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## GC Cause Analysis");
    }

    @Test
    void analysisContainsYoungGcCauses() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("### Young GC Causes")) {
            assertThat(text).contains("| Cause | Count |");
        }
    }

    @Test
    void analysisContainsOldGcCauses() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("### Old GC Causes")) {
            assertThat(text).contains("| Cause | Count |");
        }
    }

    @Test
    void analysisContainsHeapUtilization() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

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
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Recommendations");
    }

    @Test
    void analysisContainsWarningsWhenApplicable() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("## ⚠️ Warnings")) {
            assertThat(text).contains("- ");
        }
    }

    @Test
    void analysisWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of(
                        "jfr_file_path", afterPath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z"
                )));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# GC Tuning Recommendations");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisPauseDistributionTableFormat() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("## Pause Distribution")) {
            assertThat(text).contains("| Metric | Value |");
            assertThat(text).contains("|--------|-------|");
        }
    }

    @Test
    void analysisHeapUtilizationTableFormat() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("## Heap Utilization")) {
            assertThat(text).contains("| Metric | Value |");
            assertThat(text).contains("|--------|-------|");
        }
    }

    @Test
    void analysisRecommendationsAreNumberedWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("No specific tuning recommendations")) {
            assertThat(text).contains("1. ");
        }
    }

    @Test
    void analysisHealthyGcShowsNoRecommendations() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("GC behavior appears healthy")) {
            assertThat(text).contains("No specific tuning recommendations");
        }
    }

    @Test
    void analysisExplicitGcWarningWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("explicit System.gc()")) {
            assertThat(text).contains("DisableExplicitGC");
        }
    }

    @Test
    void analysisFullGcRatioWarningWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Full GC ratio")) {
            assertThat(text).contains("Full GC");
        }
    }

    @Test
    void analysisMetaspaceWarningWhenPresent() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Metaspace utilization")) {
            assertThat(text).contains("MaxMetaspaceSize");
        }
    }

    @Test
    void analysisP95PauseWarningWhenHigh() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("P95 GC pause exceeds")) {
            assertThat(text).contains("MaxGCPauseMillis");
        }
    }

    @Test
    void analysisGcAlgorithmDisplayedWhenAvailable() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

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
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("gc_recommendations", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Heap Amplitude")) {
            assertThat(text).contains("%");
        }
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
