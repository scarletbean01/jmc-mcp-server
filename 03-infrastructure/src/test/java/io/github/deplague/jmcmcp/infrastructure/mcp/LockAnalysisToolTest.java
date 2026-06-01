package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.LockAnalysisTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.github.deplague.jmcmcp.application.service.LockAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.service.LockAnalysisService;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class LockAnalysisToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private LockAnalysisTool tool;

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
        LockAnalysisService domainService = new LockAnalysisService();
        LockAnalysisApplicationService appService = new LockAnalysisApplicationService(jfrProvider, domainService);
        tool = new LockAnalysisTool(appService);
    }

    @Test
    void analysisShowsResultFromAfterFixesFile() {
        ToolResponse result = tool.lockAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Advanced Lock Analysis");
    }

    @Test
    void analysisShowsResultFromBeforeFixesFile() {
        ToolResponse result = tool.lockAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Advanced Lock Analysis");
    }

    @Test
    void analysisContainsThreadParkSectionOrNoParkMessage() {
        ToolResponse result = tool.lockAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).satisfiesAnyOf(
                t -> assertThat(t).contains("Thread Park Summary"),
                t -> assertThat(t).contains("No Thread Park events found")
        );
    }

    @Test
    void analysisContainsBiasedLockSectionOrNoRevocationMessage() {
        ToolResponse result = tool.lockAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).satisfiesAnyOf(
                t -> assertThat(t).contains("Biased Lock Revocations"),
                t -> assertThat(t).contains("No Biased Lock Revocation events found")
        );
    }

    @Test
    void analysisParkSummaryContainsMetricsWhenPresent() {
        ToolResponse result = tool.lockAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Thread Park Summary")) {
            assertThat(text).contains("Total Park Events");
            assertThat(text).contains("Avg Park Duration");
            assertThat(text).contains("Max Park Duration");
        }
    }

    @Test
    void analysisParkSitesTableFormatWhenPresent() {
        ToolResponse result = tool.lockAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Top Park Sites")) {
            assertThat(text).contains("| Stack Trace | Count | Avg Duration | Max Duration |");
        }
    }

    @Test
    void analysisBiasedLockRevocationCountsWhenPresent() {
        ToolResponse result = tool.lockAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Biased Lock Revocations")) {
            assertThat(text).contains("Single Revocations");
            assertThat(text).contains("Class/Bulk Revocations");
            assertThat(text).contains("Self Revocations");
        }
    }

    @Test
    void analysisRevokedLockClassesTableWhenPresent() {
        ToolResponse result = tool.lockAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Revoked Lock Classes")) {
            assertThat(text).contains("| Lock Class | Revocation Count |");
        }
    }

    @Test
    void analysisWithCustomTopN() {
        ToolResponse result = tool.lockAnalysis(beforePath, null, null, 3);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Advanced Lock Analysis");
    }

    @Test
    void analysisWithTopNAsString() {
        ToolResponse result = tool.lockAnalysis(beforePath, null, null, 5);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Advanced Lock Analysis");
    }

    @Test
    void analysisWithTimeRange() {
        ToolResponse result = tool.lockAnalysis(afterPath, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z", null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Advanced Lock Analysis");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        ToolResponse result = tool.lockAnalysis("/nonexistent/path.jfr", null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        ToolResponse result = tool.lockAnalysis(null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
        ToolResponse first = tool.lockAnalysis(afterPath, null, null, null);
        ToolResponse second = tool.lockAnalysis(afterPath, null, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisParkSitesLimitedByTopN() {
        ToolResponse result1 = tool.lockAnalysis(beforePath, null, null, 1);

        ToolResponse result2 = tool.lockAnalysis(beforePath, null, null, 20);

        String text1 = extractText(result1);
        String text2 = extractText(result2);

        if (text1.contains("Top Park Sites") && text2.contains("Top Park Sites")) {
            long rows1 = countDataRows(text1, "Top Park Sites");
            long rows2 = countDataRows(text2, "Top Park Sites");
            assertThat(rows1).isLessThanOrEqualTo(rows2);
        }
    }

    @Test
    void analysisDefaultTopNIsTen() {
        ToolResponse result = tool.lockAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Top Park Sites")) {
            long rows = countDataRows(text, "Top Park Sites");
            assertThat(rows).isLessThanOrEqualTo(10);
        }
    }

    @Test
    void analysisParkCountIsNumeric() {
        ToolResponse result = tool.lockAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Total Park Events")) {
            assertThat(text).matches("(?s).*Total Park Events.*\\d+.*");
        }
    }

    @Test
    void analysisRevocationCountIsNumeric() {
        ToolResponse result = tool.lockAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Single Revocations")) {
            assertThat(text).matches("(?s).*Single Revocations.*\\d+.*");
        }
    }

    @Test
    void analysisBothFilesProduceConsistentStructure() {
        ToolResponse afterResult = tool.lockAnalysis(afterPath, null, null, null);
        ToolResponse beforeResult = tool.lockAnalysis(beforePath, null, null, null);

        assertThat(afterResult.isError()).isFalse();
        assertThat(beforeResult.isError()).isFalse();

        String afterText = extractText(afterResult);
        String beforeText = extractText(beforeResult);

        assertThat(afterText).contains("# Advanced Lock Analysis");
        assertThat(beforeText).contains("# Advanced Lock Analysis");
    }

    private static long countDataRows(String text, String sectionHeader) {
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
