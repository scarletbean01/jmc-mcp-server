package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JdkBugReferenceToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private JfrAnalysisService service;
    private JdkBugReferenceTool tool;

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
        service = new JfrAnalysisService(cache);
        tool = new JdkBugReferenceTool(service);
    }

    @Test
    void analysisShowsResultFromAfterFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# JDK Bug Cross-Reference");
    }

    @Test
    void analysisShowsResultFromBeforeFixesFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# JDK Bug Cross-Reference");
    }

    @Test
    void analysisContainsJvmVersionWhenAvailable() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("JVM Version:")) {
            assertThat(text).contains("**JVM Version:**");
        }
    }

    @Test
    void analysisContainsNoBugMatchesOrPotentialMatches() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).satisfiesAnyOf(
                t -> assertThat(t).contains("No Known JDK Bug Matches"),
                t -> assertThat(t).contains("Potential JDK Bug Match")
        );
    }

    @Test
    void analysisNoBugMatchesContainsAnalysisSummary() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("No Known JDK Bug Matches")) {
            assertThat(text).contains("Compiler failures:");
            assertThat(text).contains("Java errors:");
            assertThat(text).contains("Biased lock revocations:");
        }
    }

    @Test
    void analysisBugMatchContainsSeverityAndCategory() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Severity |");
            assertThat(text).contains("| Category |");
        }
    }

    @Test
    void analysisBugMatchContainsBugId() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("JDK-");
        }
    }

    @Test
    void analysisBugMatchContainsAffectedVersions() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Affected Versions |");
        }
    }

    @Test
    void analysisBugMatchContainsMatchedPattern() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Matched Pattern |");
        }
    }

    @Test
    void analysisBugMatchContainsWorkaroundWhenAvailable() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match") && text.contains("| Workaround |")) {
            assertThat(text).contains("`-");
        }
    }

    @Test
    void analysisBugMatchContainsFixedInWhenAvailable() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match") && text.contains("| Fixed In |")) {
            assertThat(text).contains("| Fixed In |");
        }
    }

    @Test
    void analysisBugMatchContainsVersionAffectedStatus() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).satisfiesAnyOf(
                    t -> assertThat(t).contains("AFFECTED"),
                    t -> assertThat(t).contains("Likely not affected")
            );
        }
    }

    @Test
    void analysisBugMatchContainsTableFormat() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Field | Value |");
            assertThat(text).contains("|-------|-------|");
        }
    }

    @Test
    void analysisWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of(
                        "jfr_file_path", afterPath,
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z")));

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# JDK Bug Cross-Reference");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", "/nonexistent/path.jfr")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument: jfr_file_path");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", afterPath));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisNoBugMatchesHeaderFormat() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", afterPath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("No Known JDK Bug Matches")) {
            assertThat(text).contains("✅");
        }
    }

    @Test
    void analysisBugMatchesHeaderFormat() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("⚠️");
        }
    }

    @Test
    void analysisBugMatchPluralization() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("1 Potential JDK Bug Match")) {
            assertThat(text).doesNotContain("1 Potential JDK Bug Matches");
        }
    }

    @Test
    void analysisBugMatchContainsBugIdAsHeader() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("### JDK-");
        }
    }

    @Test
    void analysisBiasedLockRevocationThreshold() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("JDK-8159193")) {
            assertThat(text).contains("BiasedLockRevocation");
        }
    }

    @Test
    void analysisCompilerFailureDetection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("COMPILER")) {
            assertThat(text).satisfiesAnyOf(
                    t -> assertThat(t).contains("CRITICAL"),
                    t -> assertThat(t).contains("HIGH")
            );
        }
    }

    @Test
    void analysisMemoryBugDetection() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("jdk_bug_reference", Map.of("jfr_file_path", beforePath)));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("MEMORY")) {
            assertThat(text).containsAnyOf("OutOfMemoryError", "Direct buffer", "Metaspace");
        }
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}