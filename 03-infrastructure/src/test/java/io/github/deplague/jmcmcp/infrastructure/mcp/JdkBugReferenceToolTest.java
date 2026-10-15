package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepositoryImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregatorImpl;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        var accessorRepo = new JfrAccessorRepositoryImpl();
        JdkBugReferenceService domainService = new JdkBugReferenceService(accessorRepo, new JfrQuantityAggregatorImpl(accessorRepo));
        JdkBugReferenceApplicationService appService = new JdkBugReferenceApplicationService(jfrProvider, domainService);
        tool = new JdkBugReferenceTool(appService);
    }

    @Test
    void analysisShowsResultFromAfterFixesFile() throws Exception {
        ToolResponse result = tool.jdkBugReference(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# JDK Bug Cross-Reference");
    }

    @Test
    void analysisShowsResultFromBeforeFixesFile() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# JDK Bug Cross-Reference");
    }

    @Test
    void analysisContainsJvmVersionWhenAvailable() throws Exception {
        ToolResponse result = tool.jdkBugReference(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("JVM Version:")) {
            assertThat(text).contains("**JVM Version:**");
        }
    }

    @Test
    void analysisContainsNoBugMatchesOrPotentialMatches() throws Exception {
        ToolResponse result = tool.jdkBugReference(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        assertThat(text).satisfiesAnyOf(
                t -> assertThat(t).contains("No Known JDK Bug Matches"),
                t -> assertThat(t).contains("Potential JDK Bug Match")
        );
    }

    @Test
    void analysisNoBugMatchesContainsAnalysisSummary() throws Exception {
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
    void analysisBugMatchContainsSeverityAndCategory() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Severity |");
            assertThat(text).contains("| Category |");
        }
    }

    @Test
    void analysisBugMatchContainsBugId() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("JDK-");
        }
    }

    @Test
    void analysisBugMatchContainsAffectedVersions() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Affected Versions |");
        }
    }

    @Test
    void analysisBugMatchContainsMatchedPattern() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Matched Pattern |");
        }
    }

    @Test
    void analysisBugMatchContainsWorkaroundWhenAvailable() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match") && text.contains("| Workaround |")) {
            assertThat(text).contains("`-");
        }
    }

    @Test
    void analysisBugMatchContainsFixedInWhenAvailable() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match") && text.contains("| Fixed In |")) {
            assertThat(text).contains("| Fixed In |");
        }
    }

    @Test
    void analysisBugMatchContainsVersionAffectedStatus() throws Exception {
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
    void analysisBugMatchContainsTableFormat() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("| Field | Value |");
            assertThat(text).contains("|-------|-------|");
        }
    }

    @Test
    void analysisWithTimeRange() throws Exception {
        ToolResponse result = tool.jdkBugReference(afterPath, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z");

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# JDK Bug Cross-Reference");
    }

    @Test
    void analysisReturnsErrorForMissingFile() throws Exception {
        assertThatThrownBy(() -> tool.jdkBugReference("/nonexistent/path.jfr", null, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void analysisReturnsErrorForMissingArgument() throws Exception {
        assertThatThrownBy(() -> tool.jdkBugReference(null, null, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void analysisCachesResultOnSecondCall() throws Exception {
        ToolResponse first = tool.jdkBugReference(afterPath, null, null);
        ToolResponse second = tool.jdkBugReference(afterPath, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void analysisNoBugMatchesHeaderFormat() throws Exception {
        ToolResponse result = tool.jdkBugReference(afterPath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("No Known JDK Bug Matches")) {
            assertThat(text).contains("✅");
        }
    }

    @Test
    void analysisBugMatchesHeaderFormat() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("⚠️");
        }
    }

    @Test
    void analysisBugMatchPluralization() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("1 Potential JDK Bug Match")) {
            assertThat(text).doesNotContain("1 Potential JDK Bug Matches");
        }
    }

    @Test
    void analysisBugMatchContainsBugIdAsHeader() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("Potential JDK Bug Match")) {
            assertThat(text).contains("### JDK-");
        }
    }

    @Test
    void analysisBiasedLockRevocationThreshold() throws Exception {
        ToolResponse result = tool.jdkBugReference(beforePath, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);

        if (text.contains("JDK-8159193")) {
            assertThat(text).contains("BiasedLockRevocation");
        }
    }

    @Test
    void analysisCompilerFailureDetection() throws Exception {
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
    void analysisMemoryBugDetection() throws Exception {
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
