package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.*;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import io.github.deplague.jmcmcp.domain.port.JfrQuantityAggregator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.JavaClass;
import org.openjdk.jmc.common.item.IItemCollection;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeapDumpCrossAnalysisServiceTest {

    private HeapDumpCrossAnalysisService service;
    private MemoryLeaksService memoryLeaksService;
    private HeapDumpClassHistogramService classHistogramService;
    private Heap heap;

    @BeforeEach
    void setUp() {
        JfrAccessorRepository accessorRepo = mock(JfrAccessorRepository.class);
        JfrQuantityAggregator quantityAgg = mock(JfrQuantityAggregator.class);
        memoryLeaksService = new MemoryLeaksService(accessorRepo, quantityAgg);
        classHistogramService = new HeapDumpClassHistogramService();
        service = new HeapDumpCrossAnalysisService(memoryLeaksService, classHistogramService);
        heap = mock(Heap.class);
    }

    @Test
    void analyzeEmptyData() {
        when(heap.getAllClasses()).thenReturn(List.of());

        CrossAnalysisResult result = service.analyze(mock(IItemCollection.class), heap, 50);

        assertThat(result.hasData()).isFalse();
        assertThat(result.classes()).isEmpty();
    }

    @Test
    void analyzeCorrelatesByClassName() {
        JavaClass leakClass = mockJavaClass("com.example.Leak", 100, 2_000_000);
        JavaClass normalClass = mockJavaClass("com.example.Normal", 50, 100_000);

        when(heap.getAllClasses()).thenReturn(List.of(leakClass, normalClass));
        when(heap.isRetainedSizeByClassComputed()).thenReturn(true);

        IItemCollection events = mock(IItemCollection.class);

        CrossAnalysisResult result = service.analyze(events, heap, 50);

        assertThat(result.hasData()).isTrue();
        assertThat(result.classes()).isNotEmpty();
        assertThat(result.summary().totalClasses()).isGreaterThanOrEqualTo(0);
        assertThat(result.recommendation()).isNotBlank();
    }

    @Test
    void analyzeLimitsToTopN() {
        JavaClass a = mockJavaClass("A", 10, 100_000);
        JavaClass b = mockJavaClass("B", 20, 200_000);
        JavaClass c = mockJavaClass("C", 30, 300_000);

        when(heap.getAllClasses()).thenReturn(List.of(a, b, c));
        when(heap.isRetainedSizeByClassComputed()).thenReturn(true);

        CrossAnalysisResult result = service.analyze(mock(IItemCollection.class), heap, 2);

        assertThat(result.classes()).hasSizeLessThanOrEqualTo(2);
    }

    private JavaClass mockJavaClass(String name, int instances, long size) {
        JavaClass jc = mock(JavaClass.class);
        when(jc.getName()).thenReturn(name);
        when(jc.getInstancesCount()).thenReturn(instances);
        when(jc.getAllInstancesSize()).thenReturn(size);
        when(jc.getRetainedSizeByClass()).thenReturn(size);
        return jc;
    }
}
