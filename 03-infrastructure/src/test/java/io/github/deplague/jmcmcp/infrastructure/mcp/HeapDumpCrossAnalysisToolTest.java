package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.HeapDumpCrossAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.CrossAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.CrossAnalysisSummary;
import io.github.deplague.jmcmcp.domain.model.UnifiedClassEntry;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeapDumpCrossAnalysisToolTest {

    private HeapDumpCrossAnalysisApplicationService appService;
    private HeapDumpCrossAnalysisTool tool;

    @BeforeEach
    void setUp() {
        appService = mock(HeapDumpCrossAnalysisApplicationService.class);
        tool = new HeapDumpCrossAnalysisTool(appService);
    }

    @Test
    void returnsUnifiedMarkdownReport() throws Exception {
        CrossAnalysisResult result = new CrossAnalysisResult(true,
                List.of(new UnifiedClassEntry("com.example.Leak", 15, List.of(), 100, 2_000_000, "HIGH")),
                new CrossAnalysisSummary(1, 0, 0, 1),
                "Investigate allocation sites.");

        when(appService.analyze("/path/recording.jfr", "/path/heap.hprof", null, null, 50))
                .thenReturn(result);

        ToolResponse response = tool.heapDumpCrossAnalysis("/path/recording.jfr", "/path/heap.hprof", null, null, null);

        assertThat(response.isError()).isFalse();
        String text = extractText(response);
        assertThat(text).contains("# JFR ↔ Heap Dump Cross Analysis");
        assertThat(text).contains("com.example.Leak");
        assertThat(text).contains("HIGH");
        assertThat(text).contains("Investigate allocation sites.");
    }

    @Test
    void handlesError() throws Exception {
        when(appService.analyze("/bad/recording.jfr", "/bad/heap.hprof", null, null, 50))
                .thenThrow(new RuntimeException("cross-analysis failed"));

        ToolResponse response = tool.heapDumpCrossAnalysis("/bad/recording.jfr", "/bad/heap.hprof", null, null, null);

        assertThat(response.isError()).isTrue();
    }

    private static String extractText(ToolResponse result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }
}
