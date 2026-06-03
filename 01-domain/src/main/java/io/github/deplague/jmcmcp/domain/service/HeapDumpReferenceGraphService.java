package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ReferenceLink;
import io.github.deplague.jmcmcp.domain.model.ReferencePathResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.netbeans.lib.profiler.heap.FieldValue;
import org.netbeans.lib.profiler.heap.GCRoot;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.Instance;
import org.netbeans.lib.profiler.heap.ObjectFieldValue;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public final class HeapDumpReferenceGraphService {

    public ReferencePathResult findPathsToGcRoots(Heap heap, long objectId, int maxPaths) {
        Instance target = heap.getInstanceByID(objectId);
        if (target == null) {
            return new ReferencePathResult("Unknown", objectId, 0, List.of());
        }

        List<ReferenceLink> paths = new ArrayList<>();

        // Trace the nearest-GC-root path for the target itself
        ReferenceLink targetLink = new ReferenceLink(
                "target",
                "",
                target.getJavaClass().getName(),
                target.getInstanceId(),
                target.getSize(),
                target.getRetainedSize(),
                List.of()
        );

        // Follow nearest GC root pointer chain
        List<ReferenceLink> chain = new ArrayList<>();
        chain.add(targetLink);

        Instance current = target;
        while (current != null && paths.size() < maxPaths) {
            GCRoot gcRoot = heap.getGCRoot(current);
            if (gcRoot != null) {
                // Found a GC root - complete the chain
                ReferenceLink rootLink = new ReferenceLink(
                        "gc_root",
                        gcRoot.getKind(),
                        current.getJavaClass().getName(),
                        current.getInstanceId(),
                        current.getSize(),
                        current.getRetainedSize(),
                        List.of()
                );
                chain.add(rootLink);

                // Build the path from root to target (reverse the chain)
                List<ReferenceLink> path = buildPathFromChain(chain);
                if (!path.isEmpty()) {
                    paths.add(path.get(0));
                }
                break;
            }

            Instance next = current.getNearestGCRootPointer();
            if (next == null || next.getInstanceId() == current.getInstanceId()) {
                break;
            }

            // Find the field name that references current from next
            String fieldName = findReferencingField(next, current);

            ReferenceLink link = new ReferenceLink(
                    "field",
                    fieldName,
                    next.getJavaClass().getName(),
                    next.getInstanceId(),
                    next.getSize(),
                    next.getRetainedSize(),
                    List.of()
            );
            chain.add(link);
            current = next;
        }

        if (paths.isEmpty()) {
            // Fallback: try inbound references
            paths.addAll(findPathsViaInboundReferences(heap, target, maxPaths));
        }

        return new ReferencePathResult(
                target.getJavaClass().getName(),
                target.getInstanceId(),
                target.getRetainedSize(),
                paths
        );
    }

    @SuppressWarnings("unchecked")
    private List<ReferenceLink> findPathsViaInboundReferences(Heap heap, Instance target, int maxPaths) {
        List<ReferenceLink> paths = new ArrayList<>();
        List<ObjectFieldValue> inbound = target.getReferences();
        if (inbound == null || inbound.isEmpty()) {
            return paths;
        }

        int count = 0;
        for (ObjectFieldValue ref : inbound) {
            if (count >= maxPaths) break;
            Instance referrer = ref.getInstance();
            if (referrer == null) continue;

            GCRoot gcRoot = heap.getGCRoot(referrer);
            String refType = gcRoot != null ? "gc_root" : "field";
            String kind = gcRoot != null ? gcRoot.getKind() : (ref.getField() != null ? ref.getField().getName() : "<unknown>");

            ReferenceLink referrerLink = new ReferenceLink(
                    refType,
                    kind,
                    referrer.getJavaClass().getName(),
                    referrer.getInstanceId(),
                    referrer.getSize(),
                    referrer.getRetainedSize(),
                    List.of()
            );

            ReferenceLink targetLink = new ReferenceLink(
                    "target",
                    ref.getField() != null ? ref.getField().getName() : "<unknown>",
                    target.getJavaClass().getName(),
                    target.getInstanceId(),
                    target.getSize(),
                    target.getRetainedSize(),
                    List.of(referrerLink)
            );

            paths.add(targetLink);
            count++;
        }

        return paths;
    }

    private String findReferencingField(Instance referrer, Instance target) {
        for (Object fvObj : referrer.getFieldValues()) {
            if (fvObj instanceof ObjectFieldValue ofv) {
                if (ofv.getInstance() != null && ofv.getInstance().getInstanceId() == target.getInstanceId()) {
                    return ofv.getField() != null ? ofv.getField().getName() : "<unknown>";
                }
            }
        }
        return "<unknown>";
    }

    private List<ReferenceLink> buildPathFromChain(List<ReferenceLink> chain) {
        if (chain.isEmpty()) {
            return List.of();
        }

        // Chain is [target, ..., root]. We want root -> ... -> target
        // Build nested structure: root has child ..., which has child target
        ReferenceLink current = chain.get(chain.size() - 1); // root
        for (int i = chain.size() - 2; i >= 0; i--) {
            current = new ReferenceLink(
                    current.referenceType(),
                    current.fieldName(),
                    current.className(),
                    current.objectId(),
                    current.shallowSize(),
                    current.retainedSize(),
                    List.of(chain.get(i))
            );
        }
        return List.of(current);
    }
}
