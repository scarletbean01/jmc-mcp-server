package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.StackTraceSearchApplicationService;
import io.github.deplague.jmcmcp.domain.service.StackTraceSearchService;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.StackTraceSearchTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class StackTraceSearchToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private StackTraceSearchTool tool;

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
        StackTraceSearchService domainService = new StackTraceSearchService();
        StackTraceSearchApplicationService appService = new StackTraceSearchApplicationService(jfrProvider, domainService);
        tool = new StackTraceSearchTool(appService);
    }

    @Test
    void searchShowsResultFromAfterFixesFile() {
        ToolResponse result = tool.smartStackTraceSearch(afterPath, ".*java.*", null, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Stack Trace Search");
    }

    @Test
    void searchShowsResultFromBeforeFixesFile() {
        ToolResponse result = tool.smartStackTraceSearch(beforePath, ".*java.*", null, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Stack Trace Search");
    }

    @Test
    void searchContainsPatternAndEventTypesInOutput() {
        ToolResponse result = tool.smartStackTraceSearch(beforePath, ".*java.*", null, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("**Pattern:**");
        assertThat(text).contains("**Event types searched:**");
        assertThat(text).contains("**Total matches found:**");
    }

    @Test
    void searchContainsClassDistributionSection() {
        ToolResponse result = tool.smartStackTraceSearch(beforePath, ".*java.*", null, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Class Distribution");
        assertThat(text).contains("| Event Type | Matches |");
    }

    @Test
    void searchWithEventTypeFilter() {
        ToolResponse result = tool.smartStackTraceSearch(beforePath, ".*java.*", "jdk.ExecutionSample", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Stack Trace Search");
        assertThat(text).contains("jdk.ExecutionSample");
    }

    @Test
    void searchWithLimitParameter() {
        ToolResponse result = tool.smartStackTraceSearch(beforePath, ".*java.*", null, null, null, 5);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Stack Trace Search");
    }

    @Test
    void searchWithTimeRange() {
        ToolResponse result = tool.smartStackTraceSearch(afterPath, ".*java.*", null, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z", null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Stack Trace Search");
    }

    @Test
    void searchReturnsErrorForInvalidRegex() {
        ToolResponse result = tool.smartStackTraceSearch(beforePath, "[invalid", null, null, null, null);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Invalid regex pattern");
    }

    @Test
    void searchReturnsErrorForMissingFile() {
        ToolResponse result = tool.smartStackTraceSearch("/nonexistent/path.jfr", ".*DAO.*", null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void searchReturnsErrorForMissingClassPattern() {
        ToolResponse result = tool.smartStackTraceSearch(beforePath, null, null, null, null, null);

        assertThat(result.isError()).isTrue();
    }

    @Test
    void searchReturnsErrorForMissingJfrFilePath() {
        ToolResponse result = tool.smartStackTraceSearch(null, ".*DAO.*", null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void searchCachesResultOnSecondCall() {
                ToolResponse first = tool.smartStackTraceSearch(afterPath, ".*java.*", null, null, null, null);
        ToolResponse second = tool.smartStackTraceSearch(afterPath, ".*java.*", null, null, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void searchNoMatchesReturnsEmptyDistribution() {
        ToolResponse result = tool.smartStackTraceSearch(beforePath, "ZZZNonExistentClassZZZ", null, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Stack Trace Search");
        assertThat(text).contains("**Total matches found:** 0");
    }

    @Test
    void searchResultContainsAgentHintWhenMatchesFound() {
        ToolResponse result = tool.smartStackTraceSearch(beforePath, ".*java.*", null, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Class Distribution") && text.contains("| `jdk.")) {
            assertThat(text).contains("<agent_hint>");
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
