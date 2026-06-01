package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.SmartThreadStarvationDetectorApplicationService;
import io.github.deplague.jmcmcp.domain.service.SmartThreadStarvationDetectorService;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.SmartThreadStarvationDetectorTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class SmartThreadStarvationDetectorToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private SmartThreadStarvationDetectorTool tool;

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
        SmartThreadStarvationDetectorService domainService = new SmartThreadStarvationDetectorService();
        SmartThreadStarvationDetectorApplicationService appService = new SmartThreadStarvationDetectorApplicationService(jfrProvider, domainService);
        tool = new SmartThreadStarvationDetectorTool(appService);
    }

    @Test
    void starvationDetectorReturnsResultFromAfterFixesFile() {
        ToolResponse result = tool.smartThreadStarvationDetector(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Smart Thread Starvation Detector");
        assertThat(text).contains("Primary Diagnosis:");
    }

    @Test
    void starvationDetectorReturnsResultFromBeforeFixesFile() {
        ToolResponse result = tool.smartThreadStarvationDetector(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Smart Thread Starvation Detector");
    }

    @Test
    void starvationDetectorContainsAgentHint() {
        ToolResponse result = tool.smartThreadStarvationDetector(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("<agent_hint>");
        assertThat(text).contains("</agent_hint>");
    }

    @Test
    void starvationDetectorReturnsErrorForMissingFile() {
        ToolResponse result = tool.smartThreadStarvationDetector("/nonexistent/path.jfr", null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void starvationDetectorReturnsErrorForMissingArgument() {
        ToolResponse result = tool.smartThreadStarvationDetector(null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains(  "Error: Path cannot be null or blank");
    }

    @Test
    void starvationDetectorCachesResultOnSecondCall() {
                ToolResponse first = tool.smartThreadStarvationDetector(afterPath, null, null, null);
        ToolResponse second = tool.smartThreadStarvationDetector(afterPath, null, null, null);

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
