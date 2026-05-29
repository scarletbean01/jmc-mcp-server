package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.RequestWaterfallTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.github.deplague.jmcmcp.application.service.RequestWaterfallApplicationService;
import io.github.deplague.jmcmcp.domain.service.RequestWaterfallService;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class RequestWaterfallToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private RequestWaterfallTool tool;

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
        RequestWaterfallService domainService = new RequestWaterfallService();
        RequestWaterfallApplicationService appService = new RequestWaterfallApplicationService(jfrProvider, domainService);
        tool = new RequestWaterfallTool(appService);
    }

    @Test
    void waterfallShowsResultFromAfterFixesFile() {
        ToolResponse result = tool.smartRequestWaterfall(afterPath, ".*", null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Request Waterfall");
    }

    @Test
    void waterfallShowsResultFromBeforeFixesFile() {
        ToolResponse result = tool.smartRequestWaterfall(beforePath, ".*", null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Request Waterfall");
    }

    @Test
    void waterfallContainsThreadSummary() {
        ToolResponse result = tool.smartRequestWaterfall(beforePath, ".*", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Thread Summary");
        assertThat(text).contains("**Matched Thread(s):**");
        assertThat(text).contains("**Total Events:**");
        assertThat(text).contains("**Time Span:**");
    }

    @Test
    void waterfallContainsWaterfallTimeline() {
        ToolResponse result = tool.smartRequestWaterfall(beforePath, ".*", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Waterfall Timeline");
        assertThat(text).contains("| Time | Event Type | Phase | Duration | Detail | Top Frame |");
    }

    @Test
    void waterfallContainsPhaseBreakdown() {
        ToolResponse result = tool.smartRequestWaterfall(beforePath, ".*", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Phase Breakdown");
        assertThat(text).contains("| Phase | Total Time | % of Recorded | Event Count |");
    }

    @Test
    void waterfallContainsAgentHint() {
        ToolResponse result = tool.smartRequestWaterfall(beforePath, ".*", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("<agent_hint>");
    }

    @Test
    void waterfallWithMaxEventsLimit() {
        ToolResponse result = tool.smartRequestWaterfall(beforePath, ".*", null, null, 5);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Request Waterfall");
    }

    @Test
    void waterfallWithTimeRange() {
        ToolResponse result = tool.smartRequestWaterfall(afterPath, ".*", "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z", null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Request Waterfall");
    }

    @Test
    void waterfallNoEventsForNonexistentThread() {
        ToolResponse result = tool.smartRequestWaterfall(beforePath, "ZZZNonExistentThreadZZZ", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("No events found for thread pattern");
    }

    @Test
    void waterfallReturnsErrorForMissingFile() {
        ToolResponse result = tool.smartRequestWaterfall("/nonexistent/path.jfr", "main", null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void waterfallReturnsErrorForMissingThreadName() {
        ToolResponse result = tool.smartRequestWaterfall(beforePath, null, null, null, null);

        assertThat(result.isError()).isTrue();
    }

    @Test
    void waterfallReturnsErrorForMissingJfrFilePath() {
        ToolResponse result = tool.smartRequestWaterfall(null, "main", null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void waterfallCachesResultOnSecondCall() {
        ToolResponse first = tool.smartRequestWaterfall(afterPath, ".*", null, null, null);
        ToolResponse second = tool.smartRequestWaterfall(afterPath, ".*", null, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void waterfallWithExactThreadName() {
        ToolResponse result = tool.smartRequestWaterfall(beforePath, "main", null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Request Waterfall");
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
