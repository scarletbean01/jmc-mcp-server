package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ReferencePathResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.lib.profiler.heap.GCRoot;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.Instance;
import org.netbeans.lib.profiler.heap.JavaClass;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeapDumpReferenceGraphServiceTest {

    private HeapDumpReferenceGraphService service;
    private Heap heap;

    @BeforeEach
    void setUp() {
        service = new HeapDumpReferenceGraphService();
        heap = mock(Heap.class);
    }

    @Test
    void findPathsToGcRootsForRootInstance() {
        Instance target = mockInstance(1L, "TargetClass", 100, 1000);
        GCRoot gcRoot = mock(GCRoot.class);

        when(heap.getGCRoot(target)).thenReturn(gcRoot);
        when(target.getReferences()).thenReturn(List.of());

        ReferencePathResult result = service.findPathsToGcRoots(heap, 1L, 5);

        assertThat(result.targetClassName()).isEqualTo("TargetClass");
        assertThat(result.targetObjectId()).isEqualTo(1L);
        assertThat(result.targetRetainedSize()).isEqualTo(1000);
        assertThat(result.pathsToGcRoots()).hasSize(1);
        assertThat(result.pathsToGcRoots().get(0).referenceType()).isEqualTo("gc_root");
        assertThat(result.pathsToGcRoots().get(0).className()).isEqualTo("TargetClass");
    }

    @Test
    void findPathsToGcRootsWithReferenceChain() {
        Instance parent = mockInstance(2L, "ParentClass", 200, 2000);
        Instance target = mockInstance(1L, "TargetClass", 100, 1000);
        org.netbeans.lib.profiler.heap.ObjectFieldValue parentRef = mockObjectFieldValue(parent);

        when(heap.getGCRoot(target)).thenReturn(null);
        when(heap.getGCRoot(parent)).thenReturn(mock(GCRoot.class));
        when(target.getReferences()).thenReturn(List.of(parentRef));
        when(parent.getReferences()).thenReturn(List.of());

        ReferencePathResult result = service.findPathsToGcRoots(heap, 1L, 5);

        assertThat(result.pathsToGcRoots()).hasSize(1);
        assertThat(result.pathsToGcRoots().get(0).referenceType()).isEqualTo("target");
        assertThat(result.pathsToGcRoots().get(0).className()).isEqualTo("TargetClass");
        assertThat(result.pathsToGcRoots().get(0).children()).hasSize(1);
        assertThat(result.pathsToGcRoots().get(0).children().get(0).referenceType()).isEqualTo("gc_root");
        assertThat(result.pathsToGcRoots().get(0).children().get(0).className()).isEqualTo("ParentClass");
    }

    @Test
    void findPathsToGcRootsForMissingInstance() {
        when(heap.getInstanceByID(999L)).thenReturn(null);

        ReferencePathResult result = service.findPathsToGcRoots(heap, 999L, 5);

        assertThat(result.targetClassName()).isEqualTo("Unknown");
        assertThat(result.pathsToGcRoots()).isEmpty();
    }

    @Test
    void findPathsRespectsMaxPaths() {
        Instance parent1 = mockInstance(2L, "Parent1", 200, 2000);
        Instance parent2 = mockInstance(3L, "Parent2", 300, 3000);
        Instance target = mockInstance(1L, "Target", 100, 1000);
        org.netbeans.lib.profiler.heap.ObjectFieldValue ref1 = mockObjectFieldValue(parent1);
        org.netbeans.lib.profiler.heap.ObjectFieldValue ref2 = mockObjectFieldValue(parent2);

        when(heap.getGCRoot(target)).thenReturn(null);
        when(heap.getGCRoot(parent1)).thenReturn(mock(GCRoot.class));
        when(heap.getGCRoot(parent2)).thenReturn(mock(GCRoot.class));
        when(target.getReferences()).thenReturn(List.of(ref1, ref2));
        when(parent1.getReferences()).thenReturn(List.of());
        when(parent2.getReferences()).thenReturn(List.of());

        ReferencePathResult result = service.findPathsToGcRoots(heap, 1L, 1);

        assertThat(result.pathsToGcRoots()).hasSize(1);
    }

    private org.netbeans.lib.profiler.heap.ObjectFieldValue mockObjectFieldValue(Instance instance) {
        org.netbeans.lib.profiler.heap.ObjectFieldValue ofv = mock(org.netbeans.lib.profiler.heap.ObjectFieldValue.class);
        when(ofv.getInstance()).thenReturn(instance);
        return ofv;
    }

    private Instance mockInstance(long id, String className, long size, long retainedSize) {
        Instance inst = mock(Instance.class);
        JavaClass jc = mock(JavaClass.class);
        when(inst.getInstanceId()).thenReturn(id);
        when(inst.getJavaClass()).thenReturn(jc);
        when(jc.getName()).thenReturn(className);
        when(inst.getSize()).thenReturn(size);
        when(inst.getRetainedSize()).thenReturn(retainedSize);
        when(heap.getInstanceByID(id)).thenReturn(inst);
        return inst;
    }
}
