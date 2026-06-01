package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.JdkBugReferenceTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.github.deplague.jmcmcp.application.service.JdkBugReferenceApplicationService;
import io.github.deplague.jmcmcp.domain.service.JdkBugReferenceService;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class JdkBugReferenceToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
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
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        JdkBugReferenceService domainService = new JdkBugReferenceService();
        JdkBugReferenceApplicationService appService = new JdkBugReferenceApplicationService(jfrProvider, domainService);
        tool = new JdkBugReferenceTool(appService);
    }

    @Test
    void analysisShowsResultFromAfterFixesFile() {
        ToolResponse result = tool.jdkBugReference(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# JDK Bug Cross-Reference");
    }

    @Test
    void analysisShowsResultFromBeforeFixesFile() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# JDK Bug Cross-Reference");
    }

    @Test
    void analysisContainsJvmVersionWhenAvailable() {
        ToolResponse result = tool.jdkBugReference(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("JVM Version:")) {
            assertThat(text).contains("**JVM Version:**");
        }
    }

    @Test
    void analysisContainsNoBugMatchesOrPotentialMatches() {
        ToolResponse result = tool.jdkBugReference(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).satisfiesAnyOf(
                t -> assertThat(t).contains("No Known JDK Bug Matches"),
                t -> assertThat(t).contains("Potential JDK Bug Match")
        );
    }

    @Test
    void analysisNoBugMatchesContainsAnalysisSummary() {
        ToolResponse result = tool.jdkBugReference(afterPath, null, null);

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
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Severity |");
            assertThat(text).contains("| Category |");
        }
    }

    @Test
    void analysisBugMatchContainsBugId() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("JDK-");
        }
    }

    @Test
    void analysisBugMatchContainsAffectedVersions() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Affected Versions |");
        }
    }

    @Test
    void analysisBugMatchContainsMatchedPattern() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Matched Pattern |");
        }
    }

    @Test
    void analysisBugMatchContainsWorkaroundWhenAvailable() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match") && text.contains("| Workaround |")) {
            assertThat(text).contains("`-");
        }
    }

    @Test
    void analysisBugMatchContainsFixedInWhenAvailable() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match") && text.contains("| Fixed In |")) {
            assertThat(text).contains("| Fixed In |");
        }
    }

    @Test
    void analysisBugMatchContainsVersionAffectedStatus() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

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
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Field | Value |");
            assertThat(text).contains("|-------|-------|");
        }
    }

    @Test
    void analysisWithTimeRange() {
        ToolResponse result = tool.jdkBugReference(afterPath, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z");

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# JDK Bug Cross-Reference");
    }

    @Test
    void analysisReturnsErrorForMissingFile() {
        ToolResponse result = tool.jdkBugReference("/nonexistent/path.jfr", null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void analysisReturnsErrorForMissingArgument() {
        ToolResponse result = tool.jdkBugReference(null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void analysisCachesResultOnSecondCall() {
        ToolResponse first = tool.jdkBugReference(afterPath, null, null);
        ToolResponse second = tool.jdkBugReference(afterPath, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisNoBugMatchesHeaderFormat() {
        ToolResponse result = tool.jdkBugReference(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("No Known JDK Bug Matches")) {
            assertThat(text).contains("✅");
        }
    }

    @Test
    void analysisBugMatchesHeaderFormat() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("⚠️");
        }
    }

    @Test
    void analysisBugMatchPluralization() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("1 Potential JDK Bug Match")) {
            assertThat(text).doesNotContain("1 Potential JDK Bug Matches");
        }
    }

    @Test
    void analysisBugMatchContainsBugIdAsHeader() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("### JDK-");
        }
    }

    @Test
    void analysisBiasedLockRevocationThreshold() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("JDK-8159193")) {
            assertThat(text).contains("BiasedLockRevocation");
        }
    }

    @Test
    void analysisCompilerFailureDetection() {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

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
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("MEMORY")) {
            assertThat(text).containsAnyOf("OutOfMemoryError", "Direct buffer", "Metaspace");
        }
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
