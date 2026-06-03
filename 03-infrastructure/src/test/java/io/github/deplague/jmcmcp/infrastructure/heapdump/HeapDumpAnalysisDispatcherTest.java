package io.github.deplague.jmcmcp.infrastructure.heapdump;

import io.github.deplague.jmcmcp.application.service.HeapDumpAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramResult;
import io.github.deplague.jmcmcp.infrastructure.api.model.AnalysisRequest;
import io.github.deplague.jmcmcp.infrastructure.jfr.AnalysisResultCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class HeapDumpAnalysisDispatcherTest {

    private HeapDumpAnalysisApplicationService appService;
    private AnalysisResultCache resultCache;
    private HeapDumpAnalysisDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        appService = mock(HeapDumpAnalysisApplicationService.class);
        resultCache = new AnalysisResultCache();
        dispatcher = new HeapDumpAnalysisDispatcher(appService, resultCache);
    }

    @Test
    void dispatchClassHistogram() throws Exception {
        HeapDumpClassHistogramResult expected = new HeapDumpClassHistogramResult(true, 100, 1000, List.of());
        when(appService.classHistogram("/path/heap.hprof", 50)).thenReturn(expected);

        AnalysisRequest request = new AnalysisRequest();
        Object result = dispatcher.dispatch("class-histogram", "/path/heap.hprof", request);

        assertThat(result).isEqualTo(expected);
        verify(appService).classHistogram("/path/heap.hprof", 50);
    }

    @Test
    void dispatchDominatorTree() throws Exception {
        when(appService.dominatorTree("/path/heap.hprof", 50)).thenReturn(null);

        AnalysisRequest request = new AnalysisRequest();
        dispatcher.dispatch("dominator-tree", "/path/heap.hprof", request);

        verify(appService).dominatorTree("/path/heap.hprof", 50);
    }

    @Test
    void dispatchReferenceGraph() throws Exception {
        when(appService.referenceGraph("/path/heap.hprof", 42L, 5)).thenReturn(null);

        AnalysisRequest request = new AnalysisRequest();
        request.addParam("objectId", 42);
        request.addParam("maxPaths", 5);
        dispatcher.dispatch("reference-graph", "/path/heap.hprof", request);

        verify(appService).referenceGraph("/path/heap.hprof", 42L, 5);
    }

    @Test
    void dispatchUnknownTypeThrows() {
        AnalysisRequest request = new AnalysisRequest();

        assertThatThrownBy(() -> dispatcher.dispatch("unknown", "/path/heap.hprof", request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown heap dump analysis type");
    }

    @Test
    void dispatchUsesCustomTopN() throws Exception {
        when(appService.classHistogram("/path/heap.hprof", 10)).thenReturn(null);

        AnalysisRequest request = new AnalysisRequest();
        request.addParam("topN", 10);
        dispatcher.dispatch("class-histogram", "/path/heap.hprof", request);

        verify(appService).classHistogram("/path/heap.hprof", 10);
    }
}
