package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.CorrelateTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.github.deplague.jmcmcp.application.service.CorrelateApplicationService;
import io.github.deplague.jmcmcp.domain.service.CorrelateService;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelateToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private CorrelateTool tool;

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
        CorrelateService domainService = new CorrelateService();
        CorrelateApplicationService appService = new CorrelateApplicationService(jfrProvider, domainService);
        tool = new CorrelateTool(appService);
    }

    @Test
    void correlateShowsResultFromAfterFixesFile() {
        ToolResponse result = tool.smartCorrelate(afterPath, null, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Cross-Dimensional Correlation Analysis");
    }

    @Test
    void correlateShowsResultFromBeforeFixesFile() {
        ToolResponse result = tool.smartCorrelate(beforePath, null, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Cross-Dimensional Correlation Analysis");
    }

    @Test
    void correlateContainsLockIoSection() {
        ToolResponse result = tool.smartCorrelate(beforePath, "all", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Lock");
    }

    @Test
    void correlateContainsCpuGcSectionWhenAll() {
        ToolResponse result = tool.smartCorrelate(beforePath, "all", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## CPU");
    }

    @Test
    void correlateDimensionLockIoDbOmitsCpuGc() {
        ToolResponse result = tool.smartCorrelate(beforePath, "lock_io_db", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Lock");
        assertThat(text).doesNotContain("## CPU \u2194 GC Correlation");
    }

    @Test
    void correlateDimensionCpuGcShowsCpuGcSection() {
        ToolResponse result = tool.smartCorrelate(beforePath, "cpu_gc", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## CPU");
    }

    @Test
    void correlateWithTopNParameter() {
        ToolResponse result = tool.smartCorrelate(beforePath, null, null, null, 3);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Cross-Dimensional Correlation Analysis");
    }

    @Test
    void correlateWithTimeRange() {
        ToolResponse result = tool.smartCorrelate(afterPath, null, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z", null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Cross-Dimensional Correlation Analysis");
    }

    @Test
    void correlateContainsAgentHint() {
        ToolResponse result = tool.smartCorrelate(beforePath, null, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("<agent_hint>");
    }

    @Test
    void correlateReturnsErrorForMissingFile() {
        ToolResponse result = tool.smartCorrelate("/nonexistent/path.jfr", null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void correlateReturnsErrorForMissingJfrFilePath() {
        ToolResponse result = tool.smartCorrelate(null, null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void correlateCachesResultOnSecondCall() {
        ToolResponse first = tool.smartCorrelate(afterPath, null, null, null, null);
        ToolResponse second = tool.smartCorrelate(afterPath, null, null, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
