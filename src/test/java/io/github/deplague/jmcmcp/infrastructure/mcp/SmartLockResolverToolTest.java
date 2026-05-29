package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.SmartLockResolverApplicationService;
import io.github.deplague.jmcmcp.domain.service.SmartLockResolverService;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.SmartLockResolverTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

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
        ToolResponse result = tool.smartLockResolver(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Smart Lock Resolver");
    }

    @Test
    void lockResolverReturnsResultFromBeforeFixesFile() {
        ToolResponse result = tool.smartLockResolver(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Smart Lock Resolver");
    }

    @Test
    void lockResolverContainsAgentHint() {
        ToolResponse result = tool.smartLockResolver(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("<agent_hint>");
        assertThat(text).contains("</agent_hint>");
    }

    @Test
    void lockResolverReturnsErrorForMissingFile() {
        ToolResponse result = tool.smartLockResolver("/nonexistent/path.jfr", null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void lockResolverReturnsErrorForMissingArgument() {
        ToolResponse result = tool.smartLockResolver(null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains(  "Error: Path cannot be null or blank");
    }

    @Test
    void lockResolverCachesResultOnSecondCall() {
                ToolResponse first = tool.smartLockResolver(afterPath, null, null, null);
        ToolResponse second = tool.smartLockResolver(afterPath, null, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void lockResolverWithTimeRange() {
        ToolResponse result = tool.smartLockResolver(afterPath, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z", null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Smart Lock Resolver");
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
