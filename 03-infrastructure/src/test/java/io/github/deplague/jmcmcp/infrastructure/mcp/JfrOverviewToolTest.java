package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepositoryImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregatorImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.JfrOverviewTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.github.deplague.jmcmcp.application.service.JfrOverviewApplicationService;
import io.github.deplague.jmcmcp.domain.model.JfrOverviewResult;
import io.github.deplague.jmcmcp.domain.service.JfrOverviewService;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JfrOverviewToolTest {

    private static String jfrFilePath;
    private JfrRecordingCache cache;
    private JfrOverviewApplicationService appService;
    private JfrOverviewTool tool;

    @BeforeAll
    static void resolveJfrFile() {
        File file = new File("after.jfr");
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir"), "after.jfr");
        }
        assertThat(file).exists();
        jfrFilePath = file.getAbsolutePath();
    }

    @BeforeEach
    void setUp() {
        cache = new JfrRecordingCache();
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        JfrOverviewService domainService = new JfrOverviewService(new JfrQuantityAggregatorImpl(new JfrAccessorRepositoryImpl()));
        appService = new JfrOverviewApplicationService(jfrProvider, domainService);
        tool = new JfrOverviewTool(appService);
    }

    @Test
    void overviewShowsResultFromJfrFile() throws Exception {
        ToolResponse result = tool.jfrOverview(jfrFilePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# JFR Recording Overview");
        assertThat(text).contains("**File:**");
        assertThat(text).contains("**Duration:**");
        assertThat(text).contains("## Event Summary");
        assertThat(text).contains("**Total Events (full file):**");
    }

    @Test
    void overviewContainsFileName() throws Exception {
        ToolResponse result = tool.jfrOverview(jfrFilePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("after.jfr");
    }

    @Test
    void overviewDurationIsPositive() throws Exception {
        ToolResponse result = tool.jfrOverview(jfrFilePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("**Duration:**");
        JfrOverviewResult overview = appService.analyze(jfrFilePath, null, null);
        assertThat(overview.durationSeconds()).isGreaterThan(0);
    }

    @Test
    void overviewWithTimeRangeShowsFilteredEvents() throws Exception {
        ToolResponse result = tool.jfrOverview(jfrFilePath, "2025-01-01T00:00:00Z", "2027-12-31T23:59:59Z");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("Filtered Events");
    }

    @Test
    void overviewReturnsErrorForMissingFile() throws Exception {
        assertThatThrownBy(() -> tool.jfrOverview("/nonexistent/path.jfr", null, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void overviewReturnsErrorForMissingArgument() throws Exception {
        assertThatThrownBy(() -> tool.jfrOverview(null, null, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void overviewCachesResultOnSecondCall() throws Exception {
        ToolResponse first = tool.jfrOverview(jfrFilePath, null, null);
        ToolResponse second = tool.jfrOverview(jfrFilePath, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void overviewEventCountsArePositive() throws Exception {
        JfrOverviewResult overview = appService.analyze(jfrFilePath, null, null);

        assertThat(overview.eventCounts()).isNotEmpty();
        long totalEvents = overview.eventCounts().values().stream().mapToLong(Long::longValue).sum();
        assertThat(totalEvents).isGreaterThan(0);
    }

    @Test
    void overviewWithoutTimeRangeDoesNotShowFilteredLabel() throws Exception {
        ToolResponse result = tool.jfrOverview(jfrFilePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).doesNotContain("Filtered Events");
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
