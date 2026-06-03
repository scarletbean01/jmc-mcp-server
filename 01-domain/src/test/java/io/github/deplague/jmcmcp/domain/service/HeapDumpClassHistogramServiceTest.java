package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramResult;
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

class HeapDumpClassHistogramServiceTest {

    private HeapDumpClassHistogramService service;
    private Heap heap;

    @BeforeEach
    void setUp() {
        service = new HeapDumpClassHistogramService();
        heap = mock(Heap.class);
    }

    @Test
    void analyzeEmptyHeap() {
        when(heap.getAllClasses()).thenReturn(List.of());
        when(heap.isRetainedSizeByClassComputed()).thenReturn(false);

        HeapDumpClassHistogramResult result = service.analyze(heap, 50);

        assertThat(result.hasData()).isFalse();
        assertThat(result.entries()).isEmpty();
        assertThat(result.totalInstances()).isZero();
        assertThat(result.totalBytes()).isZero();
    }

    @Test
    void analyzeAggregatesByClass() {
        JavaClass classA = mockJavaClass("java.lang.String", 100, 400);
        JavaClass classB = mockJavaClass("java.util.ArrayList", 50, 800);

        when(heap.getAllClasses()).thenReturn(List.of(classA, classB));
        when(heap.isRetainedSizeByClassComputed()).thenReturn(true);

        HeapDumpClassHistogramResult result = service.analyze(heap, 50);

        assertThat(result.hasData()).isTrue();
        assertThat(result.entries()).hasSize(2);
        assertThat(result.totalInstances()).isEqualTo(150);
        assertThat(result.totalBytes()).isEqualTo(1200);

        assertThat(result.entries().get(0).className()).isEqualTo("java.util.ArrayList");
        assertThat(result.entries().get(0).instanceCount()).isEqualTo(50);
        assertThat(result.entries().get(0).totalRetainedSize()).isEqualTo(800);

        assertThat(result.entries().get(1).className()).isEqualTo("java.lang.String");
        assertThat(result.entries().get(1).instanceCount()).isEqualTo(100);
    }

    @Test
    void analyzeRespectsTopN() {
        JavaClass classA = mockJavaClass("A", 10, 100);
        JavaClass classB = mockJavaClass("B", 20, 400);
        JavaClass classC = mockJavaClass("C", 30, 900);

        when(heap.getAllClasses()).thenReturn(List.of(classA, classB, classC));
        when(heap.isRetainedSizeByClassComputed()).thenReturn(false);

        HeapDumpClassHistogramResult result = service.analyze(heap, 2);

        assertThat(result.entries()).hasSize(2);
        assertThat(result.entries().get(0).className()).isEqualTo("C");
        assertThat(result.entries().get(1).className()).isEqualTo("B");
    }

    @Test
    void analyzeSortsByRetainedSizeDescending() {
        JavaClass small = mockJavaClass("Small", 100, 100);
        JavaClass large = mockJavaClass("Large", 10, 10000);
        JavaClass medium = mockJavaClass("Medium", 50, 1000);

        when(heap.getAllClasses()).thenReturn(List.of(small, large, medium));
        when(heap.isRetainedSizeByClassComputed()).thenReturn(false);

        HeapDumpClassHistogramResult result = service.analyze(heap, 50);

        assertThat(result.entries().get(0).className()).isEqualTo("Large");
        assertThat(result.entries().get(1).className()).isEqualTo("Medium");
        assertThat(result.entries().get(2).className()).isEqualTo("Small");
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
