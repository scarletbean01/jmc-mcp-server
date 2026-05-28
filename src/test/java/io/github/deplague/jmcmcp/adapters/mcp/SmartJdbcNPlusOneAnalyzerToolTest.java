package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.SmartJdbcNPlusOneAnalyzerApplicationService;
import io.github.deplague.jmcmcp.domain.service.SmartJdbcNPlusOneAnalyzerService;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.adapters.infrastructure.security.RecordingAccessController;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
        SmartJdbcNPlusOneAnalyzerService domainService = new SmartJdbcNPlusOneAnalyzerService();
        SmartJdbcNPlusOneAnalyzerApplicationService appService = new SmartJdbcNPlusOneAnalyzerApplicationService(jfrProvider, domainService);
        tool = new SmartJdbcNPlusOneAnalyzerTool(appService);
    }

    @Test
    void nPlusOneAnalyzerReturnsResultFromAfterFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_jdbc_n_plus_one_analyzer", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Smart JDBC N+1 Analyzer");
    }

    @Test
    void nPlusOneAnalyzerReturnsResultFromBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_jdbc_n_plus_one_analyzer", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Smart JDBC N+1 Analyzer");
    }

    @Test
    void nPlusOneAnalyzerContainsAgentHint() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_jdbc_n_plus_one_analyzer", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("<agent_hint>");
        assertThat(text).contains("</agent_hint>");
    }

    @Test
    void nPlusOneAnalyzerReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_jdbc_n_plus_one_analyzer", Map.of("jfr_file_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void nPlusOneAnalyzerReturnsErrorForMissingArgument() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_jdbc_n_plus_one_analyzer", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument");
    }

    @Test
    void nPlusOneAnalyzerCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("smart_jdbc_n_plus_one_analyzer", Map.of("jfr_file_path", afterPath));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
