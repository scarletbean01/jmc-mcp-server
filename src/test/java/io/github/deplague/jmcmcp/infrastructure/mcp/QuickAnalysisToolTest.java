package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.ErrorAnalysisApplicationService;
import io.github.deplague.jmcmcp.application.service.GcAnalysisApplicationService;
import io.github.deplague.jmcmcp.application.service.HeapTrendsApplicationService;
import io.github.deplague.jmcmcp.application.service.HotMethodsApplicationService;
import io.github.deplague.jmcmcp.application.service.IoHotspotsApplicationService;
import io.github.deplague.jmcmcp.application.service.LockAnalysisApplicationService;
import io.github.deplague.jmcmcp.application.service.QuickAnalysisApplicationService;
import io.github.deplague.jmcmcp.application.service.SystemHealthApplicationService;
import io.github.deplague.jmcmcp.application.service.ThreadContentionApplicationService;
import io.github.deplague.jmcmcp.application.service.ThreadCpuApplicationService;
import io.github.deplague.jmcmcp.domain.service.ErrorAnalysisService;
import io.github.deplague.jmcmcp.domain.service.GcAnalysisService;
import io.github.deplague.jmcmcp.domain.service.HeapTrendsService;
import io.github.deplague.jmcmcp.domain.service.HotMethodsService;
import io.github.deplague.jmcmcp.domain.service.IoHotspotsService;
import io.github.deplague.jmcmcp.domain.service.LockAnalysisService;
import io.github.deplague.jmcmcp.domain.service.QuickAnalysisService;
import io.github.deplague.jmcmcp.domain.service.SystemHealthService;
import io.github.deplague.jmcmcp.domain.service.ThreadContentionService;
import io.github.deplague.jmcmcp.domain.service.ThreadCpuService;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.QuickAnalysisTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class QuickAnalysisToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private QuickAnalysisTool tool;

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

        QuickAnalysisService quickAnalysisService = new QuickAnalysisService();
        SystemHealthApplicationService systemHealthAppService =
                new SystemHealthApplicationService(jfrProvider, new SystemHealthService());
        ThreadCpuApplicationService threadCpuAppService =
                new ThreadCpuApplicationService(jfrProvider, new ThreadCpuService());
        HotMethodsApplicationService hotMethodsAppService =
                new HotMethodsApplicationService(jfrProvider, new HotMethodsService());
        GcAnalysisApplicationService gcAnalysisAppService =
                new GcAnalysisApplicationService(jfrProvider, new GcAnalysisService());
        HeapTrendsApplicationService heapTrendsAppService =
                new HeapTrendsApplicationService(jfrProvider, new HeapTrendsService());
        ThreadContentionApplicationService threadContentionAppService =
                new ThreadContentionApplicationService(jfrProvider, new ThreadContentionService());
        IoHotspotsApplicationService ioHotspotsAppService =
                new IoHotspotsApplicationService(jfrProvider, new IoHotspotsService());
        LockAnalysisApplicationService lockAnalysisAppService =
                new LockAnalysisApplicationService(jfrProvider, new LockAnalysisService());
        ErrorAnalysisApplicationService errorAnalysisAppService =
                new ErrorAnalysisApplicationService(jfrProvider, new ErrorAnalysisService());

        QuickAnalysisApplicationService appService =
                new QuickAnalysisApplicationService(
                        jfrProvider,
                        quickAnalysisService,
                        systemHealthAppService,
                        threadCpuAppService,
                        hotMethodsAppService,
                        gcAnalysisAppService,
                        heapTrendsAppService,
                        threadContentionAppService,
                        ioHotspotsAppService,
                        lockAnalysisAppService,
                        errorAnalysisAppService
                );

        tool = new QuickAnalysisTool(appService);
    }

    @Test
    void quickAnalysisShowsResultFromAfterFixesFile() {
        ToolResponse result = tool.smartQuickAnalysis(afterPath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Quick Analysis Dashboard");
    }

    @Test
    void quickAnalysisShowsResultFromBeforeFixesFile() {
        ToolResponse result = tool.smartQuickAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Quick Analysis Dashboard");
    }

    @Test
    void quickAnalysisContainsRecordingOverview() {
        ToolResponse result = tool.smartQuickAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Recording Overview");
        assertThat(text).contains("**Total Events:**");
    }

    @Test
    void quickAnalysisContainsSystemHealth() {
        ToolResponse result = tool.smartQuickAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## System Health");
    }

    @Test
    void quickAnalysisContainsSeverityFindings() {
        ToolResponse result = tool.smartQuickAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Severity-Classified Findings");
    }

    @Test
    void quickAnalysisContainsRecommendedNextSteps() {
        ToolResponse result = tool.smartQuickAnalysis(beforePath, null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Recommended Next Steps");
    }

    @Test
    void quickAnalysisAutoFocus() {
        ToolResponse result = tool.smartQuickAnalysis(beforePath, null, null, "auto");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Quick Analysis Dashboard");
    }

    @Test
    void quickAnalysisCpuFocus() {
        ToolResponse result = tool.smartQuickAnalysis(beforePath, null, null, "cpu");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## CPU Analysis (Focus)");
    }

    @Test
    void quickAnalysisMemoryFocus() {
        ToolResponse result = tool.smartQuickAnalysis(beforePath, null, null, "memory");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## GC Analysis (Focus)");
    }

    @Test
    void quickAnalysisLatencyFocus() {
        ToolResponse result = tool.smartQuickAnalysis(beforePath, null, null, "latency");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Thread Contention (Focus)");
    }

    @Test
    void quickAnalysisLocksFocus() {
        ToolResponse result = tool.smartQuickAnalysis(beforePath, null, null, "locks");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("## Lock Analysis (Focus)");
    }

    @Test
    void quickAnalysisWithTimeRange() {
        ToolResponse result = tool.smartQuickAnalysis(afterPath, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z", null);

        assertThat(result.isError()).isFalse();
        assertThat(extractText(result)).contains("# Quick Analysis Dashboard");
    }

    @Test
    void quickAnalysisReturnsErrorForMissingFile() {
        ToolResponse result = tool.smartQuickAnalysis("/nonexistent/path.jfr", null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void quickAnalysisReturnsErrorForMissingJfrFilePath() {
        ToolResponse result = tool.smartQuickAnalysis(null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void quickAnalysisCachesResultOnSecondCall() {
                ToolResponse first = tool.smartQuickAnalysis(afterPath, null, null, null);
        ToolResponse second = tool.smartQuickAnalysis(afterPath, null, null, null);

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
