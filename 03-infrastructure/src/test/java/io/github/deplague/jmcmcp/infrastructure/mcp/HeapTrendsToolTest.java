package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepositoryImpl;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        HeapTrendsService domainService = new HeapTrendsService(new JfrAccessorRepositoryImpl());
        HeapTrendsApplicationService appService = new HeapTrendsApplicationService(jfrProvider, domainService);
        tool = new HeapTrendsTool(appService);
    }

    @Test
    void analysisDoesNotThrowClassCastException() throws Exception {
        ToolResponse result = tool.heapTrends(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Heap & Memory Trends");
    }

    @Test
    void analysisWithBeforeJfrDoesNotThrow() throws Exception {
        ToolResponse result = tool.heapTrends(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Heap & Memory Trends");
    }

    @Test
    void analysisContainsHeapUsageTrend() throws Exception {
        ToolResponse result = tool.heapTrends(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Heap Usage Trend");
    }

    @Test
    void analysisContainsMetaspaceUsageTrend() throws Exception {
        ToolResponse result = tool.heapTrends(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Metaspace Usage Trend");
    }

    @Test
    void analysisContainsThreadCountTrend() throws Exception {
        ToolResponse result = tool.heapTrends(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Thread Count Trend");
    }

    @Test
    void analysisContainsSummaryStatistics() throws Exception {
        ToolResponse result = tool.heapTrends(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Summary Statistics");
    }

    @Test
    void analysisWithCustomBucketSize() throws Exception {
        ToolResponse result = tool.heapTrends(afterPath, null, null, "5m");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Heap & Memory Trends");
    }

    @Test
    void analysisCachesResultOnSecondCall() throws Exception {
                ToolResponse first = tool.heapTrends(afterPath, null, null, null);
        ToolResponse second = tool.heapTrends(afterPath, null, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisReturnsErrorForMissingFile() throws Exception {
        assertThatThrownBy(() -> tool.heapTrends("/nonexistent/path.jfr", null, null, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void analysisReturnsErrorForMissingArgument() throws Exception {
        assertThatThrownBy(() -> tool.heapTrends(null, null, null, null))
                .isInstanceOf(Exception.class);
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
