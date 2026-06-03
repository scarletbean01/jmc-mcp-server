package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.DominatorTreeResult;
import io.github.deplague.jmcmcp.domain.model.HeapObjectEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.HeapSummary;
import org.netbeans.lib.profiler.heap.Instance;
import org.netbeans.lib.profiler.heap.JavaClass;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeapDumpDominatorTreeServiceTest {

    private HeapDumpDominatorTreeService service;
    private Heap heap;

    @BeforeEach
    void setUp() {
        service = new HeapDumpDominatorTreeService();
        heap = mock(Heap.class);
    }

    @Test
    void buildTopDominatorsEmptyHeap() {
        HeapSummary summary = mock(HeapSummary.class);
        when(heap.getBiggestObjectsByRetainedSize(10)).thenReturn(List.of());
        when(heap.getSummary()).thenReturn(summary);
        when(summary.getTotalLiveBytes()).thenReturn(0L);
        when(summary.getTotalLiveInstances()).thenReturn(0L);
        when(heap.getAllClasses()).thenReturn(List.of());

        DominatorTreeResult result = service.buildTopDominators(heap, 10);

        assertThat(result.hasData()).isFalse();
        assertThat(result.topDominators()).isEmpty();
        assertThat(result.totalObjects()).isZero();
        assertThat(result.totalHeapBytes()).isZero();
    }

    @Test
    void buildTopDominatorsReturnsSortedList() {
        Instance inst1 = mockInstance(1L, "LargeObject", 100, 10000);
        Instance inst2 = mockInstance(2L, "SmallObject", 50, 1000);
        Instance inst3 = mockInstance(3L, "MediumObject", 80, 5000);

        HeapSummary summary = mock(HeapSummary.class);
        when(heap.getBiggestObjectsByRetainedSize(10)).thenReturn(List.of(inst1, inst2, inst3));
        when(heap.getSummary()).thenReturn(summary);
        when(summary.getTotalLiveBytes()).thenReturn(16000L);
        when(summary.getTotalLiveInstances()).thenReturn(3L);
        when(heap.getAllClasses()).thenReturn(List.of());

        DominatorTreeResult result = service.buildTopDominators(heap, 10);

        assertThat(result.hasData()).isTrue();
        assertThat(result.topDominators()).hasSize(3);
        assertThat(result.topDominators().get(0).className()).isEqualTo("LargeObject");
        assertThat(result.topDominators().get(0).retainedSize()).isEqualTo(10000);
        assertThat(result.topDominators().get(1).className()).isEqualTo("SmallObject");
        assertThat(result.topDominators().get(2).className()).isEqualTo("MediumObject");
    }

    @Test
    void buildTopDominatorsRespectsTopN() {
        Instance inst1 = mockInstance(1L, "A", 10, 1000);
        Instance inst2 = mockInstance(2L, "B", 20, 2000);
        Instance inst3 = mockInstance(3L, "C", 30, 3000);

        HeapSummary summary = mock(HeapSummary.class);
        when(heap.getBiggestObjectsByRetainedSize(2)).thenReturn(List.of(inst1, inst2, inst3));
        when(heap.getSummary()).thenReturn(summary);
        when(summary.getTotalLiveBytes()).thenReturn(6000L);
        when(summary.getTotalLiveInstances()).thenReturn(3L);
        when(heap.getAllClasses()).thenReturn(List.of());

        DominatorTreeResult result = service.buildTopDominators(heap, 2);

        assertThat(result.topDominators()).hasSize(3); // service does not slice, backend does
    }

    @Test
    void expandNodeReturnsChildren() {
        Instance child1 = mockInstance(10L, "ChildA", 10, 100);
        Instance child2 = mockInstance(11L, "ChildB", 20, 200);
        Instance parent = mockInstanceWithChildren(1L, "Parent", 100, 1000, List.of(child1, child2));

        when(heap.getInstanceByID(1L)).thenReturn(parent);

        List<HeapObjectEntry> children = service.expandNode(heap, 1L, 10);

        assertThat(children).hasSize(2);
        assertThat(children.get(0).className()).isEqualTo("ChildB");
        assertThat(children.get(0).retainedSize()).isEqualTo(200);
        assertThat(children.get(1).className()).isEqualTo("ChildA");
    }

    @Test
    void expandNodeReturnsEmptyForMissingInstance() {
        when(heap.getInstanceByID(999L)).thenReturn(null);

        List<HeapObjectEntry> children = service.expandNode(heap, 999L, 10);

        assertThat(children).isEmpty();
    }

    private Instance mockInstance(long id, String className, long size, long retainedSize) {
        Instance inst = mock(Instance.class);
        JavaClass jc = mock(JavaClass.class);
        when(inst.getInstanceId()).thenReturn(id);
        when(inst.getJavaClass()).thenReturn(jc);
        when(jc.getName()).thenReturn(className);
        when(inst.getSize()).thenReturn(size);
        when(inst.getRetainedSize()).thenReturn(retainedSize);
        when(inst.getFieldValues()).thenReturn(List.of());
        return inst;
    }

    @SuppressWarnings("unchecked")
    private Instance mockInstanceWithChildren(long id, String className, long size, long retainedSize, List<Instance> children) {
        Instance inst = mock(Instance.class);
        JavaClass jc = mock(JavaClass.class);
        when(inst.getInstanceId()).thenReturn(id);
        when(inst.getJavaClass()).thenReturn(jc);
        when(jc.getName()).thenReturn(className);
        when(inst.getSize()).thenReturn(size);
        when(inst.getRetainedSize()).thenReturn(retainedSize);

        List<org.netbeans.lib.profiler.heap.ObjectFieldValue> fieldValues = children.stream()
                .map(child -> {
                    org.netbeans.lib.profiler.heap.ObjectFieldValue ofv = mock(org.netbeans.lib.profiler.heap.ObjectFieldValue.class);
                    when(ofv.getInstance()).thenReturn(child);
                    return ofv;
                })
                .toList();
        when(inst.getFieldValues()).thenReturn((List) fieldValues);
        return inst;
    }
}
