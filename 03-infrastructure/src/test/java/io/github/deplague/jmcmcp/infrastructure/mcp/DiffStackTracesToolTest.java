package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.DiffStackTracesTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.github.deplague.jmcmcp.application.service.DiffStackTracesApplicationService;
import io.github.deplague.jmcmcp.domain.service.DiffStackTracesService;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class DiffStackTracesToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private DiffStackTracesTool tool;

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
        DiffStackTracesService domainService = new DiffStackTracesService();
        DiffStackTracesApplicationService appService = new DiffStackTracesApplicationService(jfrProvider, domainService);
        tool = new DiffStackTracesTool(appService);
    }

    @Test
    void diffShowsResultForBeforeVsAfter() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Stack Trace Diff");
    }

    @Test
    void diffContainsRecordingContext() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Recording Context");
        assertThat(text).contains("| Recording | Duration | Total Samples | Samples/sec |");
        assertThat(text).contains("| Baseline |");
        assertThat(text).contains("| Target |");
    }

    @Test
    void diffContainsNewMethodsSection() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## New Methods");
    }

    @Test
    void diffContainsDisappearedMethodsSection() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Disappeared Methods");
    }

    @Test
    void diffContainsChangedProminenceSection() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Changed Prominence");
    }

    @Test
    void diffContainsStableMethodsSection() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Stable Methods");
    }

    @Test
    void diffContainsAgentHint() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("<agent_hint>");
    }

    @Test
    void diffWithPackagePrefix() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, afterPath, "java", null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Stack Trace Diff");
    }

    @Test
    void diffWithTopNParameter() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, afterPath, null, 5);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Stack Trace Diff");
    }

    @Test
    void diffCachesResultOnSecondCall() {
        ToolResponse first = tool.smartDiffStackTraces(beforePath, afterPath, null, null);
        ToolResponse second = tool.smartDiffStackTraces(beforePath, afterPath, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void diffReturnsErrorForMissingBaselinePath() {
        ToolResponse result = tool.smartDiffStackTraces(null, afterPath, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void diffReturnsErrorForMissingTargetPath() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void diffReturnsErrorForMissingBaselineFile() {
        ToolResponse result = tool.smartDiffStackTraces("/nonexistent/path.jfr", afterPath, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void diffReturnsErrorForMissingTargetFile() {
        ToolResponse result = tool.smartDiffStackTraces(beforePath, "/nonexistent/path.jfr", null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
