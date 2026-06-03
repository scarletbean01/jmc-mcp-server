package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.HeapDumpAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramEntry;
import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramResult;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeapDumpClassHistogramToolTest {

    private HeapDumpAnalysisApplicationService appService;
    private HeapDumpClassHistogramTool tool;

    @BeforeEach
    void setUp() {
        appService = mock(HeapDumpAnalysisApplicationService.class);
        tool = new HeapDumpClassHistogramTool(appService);
    }

    @Test
    void returnsMarkdownTable() throws Exception {
        HeapDumpClassHistogramResult result = new HeapDumpClassHistogramResult(true, 100, 1000,
                List.of(
                        new HeapDumpClassHistogramEntry("java.lang.String", 50, 400, 800),
                        new HeapDumpClassHistogramEntry("java.util.ArrayList", 20, 300, 600)
                ));
        when(appService.classHistogram("/path/heap.hprof", 50)).thenReturn(result);

        ToolResponse response = tool.heapDumpClassHistogram("/path/heap.hprof", null);

        assertThat(response.isError()).isFalse();
        String text = extractText(response);
        assertThat(text).contains("# Heap Dump Class Histogram");
        assertThat(text).contains("java.lang.String");
        assertThat(text).contains("java.util.ArrayList");
        assertThat(text).contains("50");
    }

    @Test
    void respectsTopNParameter() throws Exception {
        when(appService.classHistogram("/path/heap.hprof", 10)).thenReturn(
                new HeapDumpClassHistogramResult(true, 0, 0, List.of()));

        tool.heapDumpClassHistogram("/path/heap.hprof", 10);
    }

    @Test
    void handlesError() throws Exception {
        when(appService.classHistogram("/bad/path", 50)).thenThrow(new RuntimeException("parse error"));

        ToolResponse response = tool.heapDumpClassHistogram("/bad/path", null);

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
