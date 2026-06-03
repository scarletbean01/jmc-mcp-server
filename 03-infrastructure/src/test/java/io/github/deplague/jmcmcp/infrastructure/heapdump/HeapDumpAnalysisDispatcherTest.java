package io.github.deplague.jmcmcp.infrastructure.heapdump;

import io.github.deplague.jmcmcp.application.service.HeapDumpAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramResult;
import io.github.deplague.jmcmcp.domain.model.HeapObjectEntry;
import io.github.deplague.jmcmcp.infrastructure.api.model.AnalysisRequest;
import io.github.deplague.jmcmcp.infrastructure.jfr.AnalysisResultCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void expandDominatorNodeResolvesTreeId() throws Exception {
        List<HeapObjectEntry> expected = List.of(
                new HeapObjectEntry(1L, "java.lang.String", 100, 200, 1, List.of(), false)
        );
        when(appService.expandDominatorNode("/path/heap.hprof", 42L, 50)).thenReturn(expected);

        // First dispatch to register the treeId mapping
        AnalysisRequest request = new AnalysisRequest();
        dispatcher.dispatch("dominator-tree", "/path/heap.hprof", request);

        // Then expand a node
        List<HeapObjectEntry> result = dispatcher.expandDominatorNode(
                hashPath("/path/heap.hprof") + "|dominator-tree|default", "42");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void expandDominatorNodeUnknownTreeThrows() {
        assertThatThrownBy(() -> dispatcher.expandDominatorNode("unknown-tree", "42"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown dominator tree");
    }

    @Test
    void expandDominatorNodeInvalidNodeIdThrows() throws Exception {
        // Register a tree first
        AnalysisRequest request = new AnalysisRequest();
        dispatcher.dispatch("dominator-tree", "/path/heap.hprof", request);

        String treeId = hashPath("/path/heap.hprof") + "|dominator-tree|default";
        assertThatThrownBy(() -> dispatcher.expandDominatorNode(treeId, "not-a-number"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid nodeId");
    }

    private static String hashPath(String path) {
        return String.valueOf(java.util.Objects.hash(path));
    }
}
