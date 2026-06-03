package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.HeapDumpProvider;
import io.github.deplague.jmcmcp.domain.model.DominatorTreeResult;
import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramResult;
import io.github.deplague.jmcmcp.domain.model.ReferencePathResult;
import io.github.deplague.jmcmcp.domain.service.HeapDumpClassHistogramService;
import io.github.deplague.jmcmcp.domain.service.HeapDumpDominatorTreeService;
import io.github.deplague.jmcmcp.domain.service.HeapDumpReferenceGraphService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.HeapSummary;
import org.netbeans.lib.profiler.heap.Instance;
import org.netbeans.lib.profiler.heap.JavaClass;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HeapDumpAnalysisApplicationServiceTest {

    private HeapDumpProvider provider;
    private HeapDumpAnalysisApplicationService appService;
    private Heap heap;

    @BeforeEach
    void setUp() {
        provider = mock(HeapDumpProvider.class);
        appService = new HeapDumpAnalysisApplicationService(
                provider,
                new HeapDumpClassHistogramService(),
                new HeapDumpDominatorTreeService(),
                new HeapDumpReferenceGraphService()
        );
        heap = mock(Heap.class);
    }

    @Test
    void classHistogramDelegatesToDomainService() throws IOException {
        JavaClass classA = mockJavaClass("java.lang.String", 100, 400);
        when(heap.getAllClasses()).thenReturn(List.of(classA));
        when(heap.isRetainedSizeByClassComputed()).thenReturn(false);
        when(provider.loadSnapshot("/path/to/heap.hprof")).thenReturn(heap);

        HeapDumpClassHistogramResult result = appService.classHistogram("/path/to/heap.hprof", 50);

        assertThat(result.hasData()).isTrue();
        assertThat(result.entries()).hasSize(1);
        assertThat(result.entries().get(0).className()).isEqualTo("java.lang.String");
    }

    @Test
    void dominatorTreeDelegatesToDomainService() throws IOException {
        Instance inst = mockInstance(1L, "BigObject", 100, 5000);
        HeapSummary summary = mock(HeapSummary.class);
        when(heap.getBiggestObjectsByRetainedSize(50)).thenReturn(List.of(inst));
        when(heap.getSummary()).thenReturn(summary);
        when(summary.getTotalLiveBytes()).thenReturn(5000L);
        when(summary.getTotalLiveInstances()).thenReturn(1L);
        when(heap.getAllClasses()).thenReturn(List.of());
        when(provider.loadSnapshot("/path/to/heap.hprof")).thenReturn(heap);

        DominatorTreeResult result = appService.dominatorTree("/path/to/heap.hprof", 50);

        assertThat(result.hasData()).isTrue();
        assertThat(result.topDominators()).hasSize(1);
        assertThat(result.topDominators().get(0).className()).isEqualTo("BigObject");
    }

    @Test
    void referenceGraphDelegatesToDomainService() throws IOException {
        Instance target = mockInstance(1L, "TargetClass", 100, 1000);
        when(heap.getInstanceByID(1L)).thenReturn(target);
        when(heap.getGCRoot(target)).thenReturn(null);
        when(target.getReferences()).thenReturn(List.of());
        when(provider.loadSnapshot("/path/to/heap.hprof")).thenReturn(heap);

        ReferencePathResult result = appService.referenceGraph("/path/to/heap.hprof", 1L, 5);

        assertThat(result.targetClassName()).isEqualTo("TargetClass");
    }

    @Test
    void expandDominatorNodeDelegatesToDomainService() throws IOException {
        when(provider.loadSnapshot("/path/to/heap.hprof")).thenReturn(heap);

        var result = appService.expandDominatorNode("/path/to/heap.hprof", 42L, 10);

        assertThat(result).isEmpty();
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

    private JavaClass mockJavaClass(String name, int instances, long size) {
        JavaClass jc = mock(JavaClass.class);
        when(jc.getName()).thenReturn(name);
        when(jc.getInstancesCount()).thenReturn(instances);
        when(jc.getAllInstancesSize()).thenReturn(size);
        return jc;
    }
}
