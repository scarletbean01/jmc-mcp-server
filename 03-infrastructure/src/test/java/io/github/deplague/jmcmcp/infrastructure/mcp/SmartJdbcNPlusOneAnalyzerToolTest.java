package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepositoryImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.SmartJdbcNPlusOneAnalyzerApplicationService;
import io.github.deplague.jmcmcp.domain.service.SmartJdbcNPlusOneAnalyzerService;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.SmartJdbcNPlusOneAnalyzerTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SmartJdbcNPlusOneAnalyzerToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private SmartJdbcNPlusOneAnalyzerTool tool;

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
        SmartJdbcNPlusOneAnalyzerService domainService = new SmartJdbcNPlusOneAnalyzerService(new JfrAccessorRepositoryImpl());
        SmartJdbcNPlusOneAnalyzerApplicationService appService = new SmartJdbcNPlusOneAnalyzerApplicationService(jfrProvider, domainService);
        tool = new SmartJdbcNPlusOneAnalyzerTool(appService);
    }

    @Test
    void nPlusOneAnalyzerReturnsResultFromAfterFixesFile() throws Exception {
        ToolResponse result = tool.smartJdbcNPlusOneAnalyzer(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Smart JDBC N+1 Analyzer");
    }

    @Test
    void nPlusOneAnalyzerReturnsResultFromBeforeFixesFile() throws Exception {
        ToolResponse result = tool.smartJdbcNPlusOneAnalyzer(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Smart JDBC N+1 Analyzer");
    }

    @Test
    void nPlusOneAnalyzerContainsAgentHint() throws Exception {
        ToolResponse result = tool.smartJdbcNPlusOneAnalyzer(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("<agent_hint>");
        assertThat(text).contains("</agent_hint>");
    }

    @Test
    void nPlusOneAnalyzerReturnsErrorForMissingFile() throws Exception {
        assertThatThrownBy(() -> tool.smartJdbcNPlusOneAnalyzer("/nonexistent/path.jfr", null, null, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void nPlusOneAnalyzerReturnsErrorForMissingArgument() throws Exception {
        assertThatThrownBy(() -> tool.smartJdbcNPlusOneAnalyzer(null, null, null, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void nPlusOneAnalyzerCachesResultOnSecondCall() throws Exception {
                ToolResponse first = tool.smartJdbcNPlusOneAnalyzer(afterPath, null, null, null);
        ToolResponse second = tool.smartJdbcNPlusOneAnalyzer(afterPath, null, null, null);

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
