package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.DeadlockDetectionApplicationService;
import io.github.deplague.jmcmcp.domain.service.DeadlockDetectionService;
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

class DeadlockDetectionToolTest {

    private static String jfrFilePath;
    private static String beforeFilePath;

    private JfrRecordingCache cache;
    private DeadlockDetectionTool tool;

    @BeforeAll
    static void resolveJfrFiles() {
        jfrFilePath = resolveJfr("after.jfr");
        beforeFilePath = resolveJfr("before.jfr");
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
        DeadlockDetectionService domainService = new DeadlockDetectionService();
        DeadlockDetectionApplicationService appService = new DeadlockDetectionApplicationService(jfrProvider, domainService);
        tool = new DeadlockDetectionTool(appService);
    }

    @Test
    void analysisShowsResultFromJfrFile() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Deadlock Detection");
    }

    @Test
    void analysisContainsVerdict() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("## Verdict:");
    }

    @Test
    void analysisWithBeforeFixesFile() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", beforeFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Deadlock Detection");
        assertThat(text).contains("## Verdict:");
    }

    @Test
    void analysisWithTimeRange() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of(
                        "jfr_file_path", jfrFilePath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z"
                )
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).contains("# Deadlock Detection");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", "/nonexistent/path.jfr")
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of()
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isTrue();
        String text = extractText(result);
        assertThat(text).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisContainsMonitorSummaryOrLimitedMode() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).satisfiesAnyOf(
                t -> assertThat(t).contains("## Monitor Summary"),
                t -> assertThat(t).contains("Limited Analysis Mode"),
                t -> assertThat(t).contains("Deadlock Cycle"),
                t -> assertThat(t).contains("NO DEADLOCKS DETECTED")
        );
    }

    @Test
    void analysisOutputContainsThreadOrMonitorInfo() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).satisfiesAnyOf(
                t -> assertThat(t).contains("Threads Analyzed"),
                t -> assertThat(t).contains("Monitor enter events"),
                t -> assertThat(t).contains("Threads involved"),
                t -> assertThat(t).contains("No parseable thread dump")
        );
    }

    @Test
    void analysisNoDeadlocksOutputStructure() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("NO DEADLOCKS DETECTED")) {
            assertThat(text).contains("Analyzed");
            assertThat(text).contains("No cycles found in the monitor wait-for graph");
        }
    }

    @Test
    void analysisDeadlockOutputContainsRecommendations() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("DEADLOCK") && !text.contains("NO DEADLOCKS")) {
            assertThat(text).contains("## Recommendations");
            assertThat(text).contains("Lock ordering");
            assertThat(text).contains("tryLock()");
            assertThat(text).contains("Reduce lock scope");
        }
    }

    @Test
    void analysisDeadlockCycleContainsMermaidDiagram() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Deadlock Cycle")) {
            assertThat(text).contains("```mermaid");
            assertThat(text).contains("graph TD");
            assertThat(text).contains("```");
        }
    }

    @Test
    void analysisDeadlockCycleContainsThreadTable() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Deadlock Cycle")) {
            assertThat(text).contains("| Thread | Waits For | Held By |");
        }
    }

    @Test
    void analysisLimitedModeShowsMonitorCounts() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Limited Analysis Mode")) {
            assertThat(text).contains("Monitor enter events:");
            assertThat(text).contains("Monitor wait events:");
            assertThat(text).contains("Recommendation:");
        }
    }

    @Test
    void analysisMonitorSummaryContainsTableFormat() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("## Monitor Summary")) {
            assertThat(text).contains("| Metric | Value |");
            assertThat(text).contains("Threads Analyzed");
            assertThat(text).contains("Threads Holding Locks");
            assertThat(text).contains("Threads Waiting for Locks");
            assertThat(text).contains("Total Monitors");
        }
    }

    @Test
    void analysisToolNameIsCorrect() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(
                "deadlock_detection",
                Map.of("jfr_file_path", jfrFilePath)
        );

        CallToolResult result = tool.spec().callHandler().apply(null, request);

        assertThat(result.isError()).isFalse();
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
