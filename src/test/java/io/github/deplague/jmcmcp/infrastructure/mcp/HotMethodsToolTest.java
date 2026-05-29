package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.HotMethodsTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.github.deplague.jmcmcp.application.service.HotMethodsApplicationService;
import io.github.deplague.jmcmcp.domain.service.HotMethodsService;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class HotMethodsToolTest {

    private static String afterPath;
    private static String beforePath;

    private HotMethodsTool tool;

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
        JfrRecordingCache cache = new JfrRecordingCache();
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        HotMethodsService hotMethodsService = new HotMethodsService();
        HotMethodsApplicationService appService = new HotMethodsApplicationService(
                jfrProvider, hotMethodsService
        );
        tool = new HotMethodsTool(appService);
    }

    @Test
    void analysisShowsResultFromAfterFixesFile() {
        ToolResponse result = tool.hotMethods(afterPath, null, null, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisShowsResultFromBeforeFixesFile() {
        ToolResponse result = tool.hotMethods(beforePath, null, null, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisContainsTableHeaderWhenSamplesPresent() {
        ToolResponse result = tool.hotMethods(afterPath, null, null, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("No execution samples found")) {
            assertThat(text).contains("| Samples | Stack Trace (top 5 frames) |");
            assertThat(text).contains("|---------|----------------------------|");
        }
    }

    @Test
    void analysisContainsNoSamplesMessageWhenNoneFound() {
        ToolResponse result = tool.hotMethods(afterPath, "2099-12-31T23:59:59Z", "2099-12-31T23:59:58Z", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("No execution samples found")) {
            assertThat(text).contains("No execution samples found in the recording");
        }
    }

    @Test
    void analysisWithCustomTopN() {
        ToolResponse result = tool.hotMethods(beforePath, null, null, null, null, 3);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisWithTopNAsString() {
        ToolResponse result = tool.hotMethods(beforePath, null, null, null, null, 5);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisTopNLimitsResults() {
        ToolResponse result1 = tool.hotMethods(beforePath, null, null, null, null, 1);
        ToolResponse result2 = tool.hotMethods(beforePath, null, null, null, null, 20);

        String text1 = extractText(result1);
        String text2 = extractText(result2);

        if (!text1.contains("No execution samples found") && !text2.contains("No execution samples found")) {
            long rows1 = countDataRows(text1);
            long rows2 = countDataRows(text2);
            assertThat(rows1).isLessThanOrEqualTo(rows2);
        }
    }

    @Test
    void analysisDefaultTopNIsTen() {
        ToolResponse result = tool.hotMethods(beforePath, null, null, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("No execution samples found")) {
            long rows = countDataRows(text);
            assertThat(rows).isLessThanOrEqualTo(10);
        }
    }

    @Test
    void analysisWithThreadNameFilter() {
        ToolResponse result = tool.hotMethods(beforePath, null, null, "main", null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisWithPackagePrefixFilter() {
        ToolResponse result = tool.hotMethods(beforePath, null, null, null, "java.lang", null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisWithThreadNameAndPackagePrefix() {
        ToolResponse result = tool.hotMethods(beforePath, null, null, "main", "java", null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisWithTimeRange() {
        ToolResponse result = tool.hotMethods(afterPath, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z", null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        ToolResponse result = tool.hotMethods("/nonexistent/path.jfr", null, null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        ToolResponse result = tool.hotMethods(null, null, null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void analysisSampleCountsAreNumeric() {
        ToolResponse result = tool.hotMethods(beforePath, null, null, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("No execution samples found")) {
            assertThat(text).matches("(?s).*\\| \\d+ \\|.*");
        }
    }

    @Test
    void analysisStackTraceFramesAreFormatted() {
        ToolResponse result = tool.hotMethods(beforePath, null, null, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (!text.contains("No execution samples found")) {
            assertThat(text).contains("`");
        }
    }

    @Test
    void analysisThreadNameFilterReducesResults() {
        ToolResponse unfiltered = tool.hotMethods(beforePath, null, null, null, null, null);
        ToolResponse filtered = tool.hotMethods(beforePath, null, null, "nonexistent-thread-xyz", null, null);

        String unfilteredText = extractText(unfiltered);
        String filteredText = extractText(filtered);

        if (!unfilteredText.contains("No execution samples found")) {
            long unfilteredRows = countDataRows(unfilteredText);
            if (!filteredText.contains("No execution samples found")) {
                long filteredRows = countDataRows(filteredText);
                assertThat(filteredRows).isLessThanOrEqualTo(unfilteredRows);
            }
        }
    }

    @Test
    void analysisAllOptionalParametersTogether() {
        ToolResponse result = tool.hotMethods(beforePath, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z", "main", "java", 5);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Hot Methods");
    }

    private static long countDataRows(String text) {
        int tableStart = text.indexOf("| Samples |");
        if (tableStart < 0) return 0;
        String table = text.substring(tableStart);
        return table.lines()
                .filter(l -> l.startsWith("|") && !l.contains("---") && !l.contains("Samples |"))
                .count();
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
