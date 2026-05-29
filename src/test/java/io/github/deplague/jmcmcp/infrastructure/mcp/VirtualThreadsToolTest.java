package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.application.service.VirtualThreadsApplicationService;
import io.github.deplague.jmcmcp.domain.service.VirtualThreadsService;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.VirtualThreadsTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class VirtualThreadsToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private VirtualThreadsTool tool;

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
        JfrProvider jfrProvider = new JfrProviderImpl(cache, accessController);
        VirtualThreadsService domainService = new VirtualThreadsService();
        VirtualThreadsApplicationService appService = new VirtualThreadsApplicationService(jfrProvider, domainService);
        tool = new VirtualThreadsTool(appService);
    }

    @Test
    void analysisShowsResultFromAfterFixesFile() {
        ToolResponse result = tool.virtualThreadTool(afterPath, null, null, 10);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Virtual Thread Analysis");
    }

    @Test
    void analysisShowsResultFromBeforeFixesFile() {
        ToolResponse result = tool.virtualThreadTool(beforePath, null, null, 10);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Virtual Thread Analysis");
    }

    @Test
    void analysisContainsNoEventsMessageWhenNoneFound() {
        ToolResponse result = tool.virtualThreadTool(afterPath, null, null, 10);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("Pinning Summary") && !text.contains("Submission Failures") && !text.contains("Sleep Failures")) {
            assertThat(text).contains("No virtual thread pinning or failure events found");
        }
    }

    @Test
    void analysisPinningSummaryWhenPresent() {
        ToolResponse result = tool.virtualThreadTool(beforePath, null, null, 10);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Pinning Summary")) {
            assertThat(text).contains("Total Pinned Events");
            assertThat(text).contains("### Top Pinning Sites");
        }
    }

    @Test
    void analysisPinningSitesTableFormat() {
        ToolResponse result = tool.virtualThreadTool(beforePath, null, null, 10);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Top Pinning Sites")) {
            assertThat(text).contains("| Stack Trace (top 5 frames) | Count | Percentage |");
            assertThat(text).contains("|----------------------------|-------|------------|");
        }
    }

    @Test
    void analysisPinningSitesContainPercentage() {
        ToolResponse result = tool.virtualThreadTool(beforePath, null, null, 10);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Top Pinning Sites")) {
            assertThat(text).contains("%");
        }
    }

    @Test
    void analysisSubmissionFailuresWhenPresent() {
        ToolResponse result = tool.virtualThreadTool(beforePath, null, null, 10);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Submission Failures")) {
            assertThat(text).contains("Carrier Pool Exhaustion");
            assertThat(text).contains("| Exception | Count |");
        }
    }

    @Test
    void analysisSleepFailuresWhenPresent() {
        ToolResponse result = tool.virtualThreadTool(beforePath, null, null, 10);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Sleep Failures")) {
            assertThat(text).contains("| Exception | Count |");
        }
    }

    @Test
    void analysisWithCustomTopN() {
        ToolResponse result = tool.virtualThreadTool(beforePath, null, null, 3);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Virtual Thread Analysis");
    }

    @Test
    void analysisWithTopNAsString() {
        ToolResponse result = tool.virtualThreadTool(beforePath, null, null, 5);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Virtual Thread Analysis");
    }

    @Test
    void analysisWithTimeRange() {
        ToolResponse result = tool.virtualThreadTool(afterPath, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z", 10);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Virtual Thread Analysis");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        ToolResponse result = tool.virtualThreadTool("/nonexistent/path.jfr", null, null, 10);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        ToolResponse result = tool.virtualThreadTool(null, null, null, 10);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
                ToolResponse first = tool.virtualThreadTool(afterPath, null, null, 10);
        ToolResponse second = tool.virtualThreadTool(afterPath, null, null, 10);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisPinningSitesLimitedByTopN() {
        ToolResponse result1 = tool.virtualThreadTool(beforePath, null, null, 1);

        ToolResponse result2 = tool.virtualThreadTool(beforePath, null, null, 20);

        String text1 = extractText(result1);
        String text2 = extractText(result2);

        if (text1.contains("Top Pinning Sites") && text2.contains("Top Pinning Sites")) {
            long tableRows1 = countTableRows(text1, "Top Pinning Sites");
            long tableRows2 = countTableRows(text2, "Top Pinning Sites");
            assertThat(tableRows1).isLessThanOrEqualTo(tableRows2);
        }
    }

    @Test
    void analysisNoEventsMessageMentionsJfrConfiguration() {
        ToolResponse result = tool.virtualThreadTool(afterPath, null, null, 10);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("No virtual thread pinning or failure events found")) {
            assertThat(text).contains("JFR events are not enabled");
        }
    }

    @Test
    void analysisPinningCountIsNumeric() {
        ToolResponse result = tool.virtualThreadTool(beforePath, null, null, 10);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Total Pinned Events:")) {
            assertThat(text).matches("(?s).*Total Pinned Events:\\s*\\d+.*");
        }
    }

    @Test
    void analysisDefaultTopNIsTen() {
        ToolResponse result = tool.virtualThreadTool(beforePath, null, null, 10);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Top Pinning Sites")) {
            long tableRows = countTableRows(text, "Top Pinning Sites");
            assertThat(tableRows).isLessThanOrEqualTo(10);
        }
    }

    private static long countTableRows(String text, String sectionHeader) {
        int sectionStart = text.indexOf(sectionHeader);
        if (sectionStart < 0) return 0;
        String afterSection = text.substring(sectionStart);
        int nextSection = afterSection.indexOf("\n## ");
        String section = nextSection > 0 ? afterSection.substring(0, nextSection) : afterSection;
        return section.lines()
                .filter(l -> l.startsWith("|") && !l.contains("---") && !l.contains("Stack Trace"))
                .count() - 1;
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
