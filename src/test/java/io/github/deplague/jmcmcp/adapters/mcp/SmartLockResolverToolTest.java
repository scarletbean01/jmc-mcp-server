package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.SmartLockResolverApplicationService;
import io.github.deplague.jmcmcp.domain.service.SmartLockResolverService;
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

class SmartLockResolverToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private SmartLockResolverTool tool;

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
        SmartLockResolverService domainService = new SmartLockResolverService();
        SmartLockResolverApplicationService appService = new SmartLockResolverApplicationService(jfrProvider, domainService);
        tool = new SmartLockResolverTool(appService);
    }

    @Test
    void lockResolverReturnsResultFromAfterFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_lock_resolver", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Smart Lock Resolver");
    }

    @Test
    void lockResolverReturnsResultFromBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_lock_resolver", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Smart Lock Resolver");
    }

    @Test
    void lockResolverContainsAgentHint() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_lock_resolver", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("<agent_hint>");
        assertThat(text).contains("</agent_hint>");
    }

    @Test
    void lockResolverReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_lock_resolver", Map.of("jfr_file_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void lockResolverReturnsErrorForMissingArgument() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_lock_resolver", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument");
    }

    @Test
    void lockResolverCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("smart_lock_resolver", Map.of("jfr_file_path", afterPath));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void lockResolverWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_lock_resolver", Map.of(
                        "jfr_file_path", afterPath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Smart Lock Resolver");
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
