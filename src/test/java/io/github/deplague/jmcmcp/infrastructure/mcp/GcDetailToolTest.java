package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.GcDetailApplicationService;
import io.github.deplague.jmcmcp.domain.service.GcDetailService;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.GcDetailTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class GcDetailToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private GcDetailTool tool;

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
        GcDetailService domainService = new GcDetailService();
        GcDetailApplicationService appService = new GcDetailApplicationService(jfrProvider, domainService);
        tool = new GcDetailTool(appService);
    }

    @Test
    void analysisDoesNotThrowClassCastException() {
        ToolResponse result = tool.gcDetail(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Detailed GC Analysis");
    }

    @Test
    void analysisWithBeforeJfrDoesNotThrow() {
        ToolResponse result = tool.gcDetail(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Detailed GC Analysis");
    }

    @Test
    void analysisContainsGenerationalSummary() {
        ToolResponse result = tool.gcDetail(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Generational Summary");
    }

    @Test
    void analysisContainsPhaseBreakdownWhenAllSelected() {
        ToolResponse result = tool.gcDetail(afterPath, null, null, "all");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Pause Phase Breakdown");
    }

    @Test
    void analysisContainsHeapTrendsWhenAllSelected() {
        ToolResponse result = tool.gcDetail(afterPath, null, null, "all");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Heap Trends");
    }

    @Test
    void analysisOnlyPhasesWhenRequested() {
        ToolResponse result = tool.gcDetail(afterPath, null, null, "phases");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Detailed GC Analysis");
        assertThat(text).contains("## Pause Phase Breakdown");
    }

    @Test
    void analysisOnlyHeapTrendsWhenRequested() {
        ToolResponse result = tool.gcDetail(afterPath, null, null, "heap_trends");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Detailed GC Analysis");
        assertThat(text).contains("## Heap Trends");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
                ToolResponse first = tool.gcDetail(afterPath, null, null, null);
        ToolResponse second = tool.gcDetail(afterPath, null, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        ToolResponse result = tool.gcDetail("/nonexistent/path.jfr", null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        ToolResponse result = tool.gcDetail(null, null, null, null);

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
