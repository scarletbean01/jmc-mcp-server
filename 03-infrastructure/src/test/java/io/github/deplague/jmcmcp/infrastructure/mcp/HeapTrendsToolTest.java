package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.HeapTrendsApplicationService;
import io.github.deplague.jmcmcp.domain.service.HeapTrendsService;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.HeapTrendsTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class HeapTrendsToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private HeapTrendsTool tool;

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
        HeapTrendsService domainService = new HeapTrendsService();
        HeapTrendsApplicationService appService = new HeapTrendsApplicationService(jfrProvider, domainService);
        tool = new HeapTrendsTool(appService);
    }

    @Test
    void analysisDoesNotThrowClassCastException() {
        ToolResponse result = tool.heapTrends(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Heap & Memory Trends");
    }

    @Test
    void analysisWithBeforeJfrDoesNotThrow() {
        ToolResponse result = tool.heapTrends(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Heap & Memory Trends");
    }

    @Test
    void analysisContainsHeapUsageTrend() {
        ToolResponse result = tool.heapTrends(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Heap Usage Trend");
    }

    @Test
    void analysisContainsMetaspaceUsageTrend() {
        ToolResponse result = tool.heapTrends(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Metaspace Usage Trend");
    }

    @Test
    void analysisContainsThreadCountTrend() {
        ToolResponse result = tool.heapTrends(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Thread Count Trend");
    }

    @Test
    void analysisContainsSummaryStatistics() {
        ToolResponse result = tool.heapTrends(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Summary Statistics");
    }

    @Test
    void analysisWithCustomBucketSize() {
        ToolResponse result = tool.heapTrends(afterPath, null, null, "5m");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Heap & Memory Trends");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
                ToolResponse first = tool.heapTrends(afterPath, null, null, null);
        ToolResponse second = tool.heapTrends(afterPath, null, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        ToolResponse result = tool.heapTrends("/nonexistent/path.jfr", null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        ToolResponse result = tool.heapTrends(null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
