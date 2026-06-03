package io.github.deplague.jmcmcp.infrastructure.heapdump;

import io.github.deplague.jmcmcp.application.service.HeapDumpAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.HeapObjectEntry;
import io.github.deplague.jmcmcp.infrastructure.api.model.AnalysisRequest;
import io.github.deplague.jmcmcp.infrastructure.jfr.AnalysisResultCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class HeapDumpAnalysisDispatcher {

    private final HeapDumpAnalysisApplicationService appService;
    private final AnalysisResultCache resultCache;
    private final Map<String, String> treeIdToPath = new ConcurrentHashMap<>();

    public Object dispatch(String analysisType, String heapDumpPath, AnalysisRequest request) {
        String paramsHash = buildParamsHash(request);
        String pathHash = hashPath(heapDumpPath);
        String cacheKey = AnalysisResultCache.buildKey(pathHash, analysisType, paramsHash);

        if ("dominator-tree".equals(analysisType)) {
            String treeId = pathHash + "|dominator-tree|" + paramsHash;
            treeIdToPath.put(treeId, heapDumpPath);
        }

        return resultCache.getOrCompute(cacheKey, () -> {
            try {
                Map<String, Object> p = request.getParams() != null ? request.getParams() : Map.of();
                return switch (analysisType) {
                    case "class-histogram" -> appService.classHistogram(
                            heapDumpPath, intParam(p, "topN", 50));
                    case "dominator-tree" -> appService.dominatorTree(
                            heapDumpPath, intParam(p, "topN", 50));
                    case "reference-graph" -> appService.referenceGraph(
                            heapDumpPath, longParam(p, "objectId", 0L), intParam(p, "maxPaths", 5));
                    default -> throw new IllegalArgumentException("Unknown heap dump analysis type: " + analysisType);
                };
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<HeapObjectEntry> expandDominatorNode(String treeId, String nodeId) {
        String heapDumpPath = treeIdToPath.get(treeId);
        if (heapDumpPath == null) {
            throw new IllegalArgumentException("Unknown dominator tree: " + treeId);
        }
        long objectId;
        try {
            objectId = Long.parseLong(nodeId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid nodeId (must be object ID): " + nodeId);
        }
        try {
            return appService.expandDominatorNode(heapDumpPath, objectId, 50);
        } catch (IOException e) {
            throw new RuntimeException("Failed to expand dominator node: " + e.getMessage(), e);
        }
    }

    private static int intParam(Map<String, Object> p, String key, int defaultValue) {
        Object v = p.get(key);
        if (v instanceof Number n) return n.intValue();
        if (v instanceof String s) {
            try { return Integer.parseInt(s); } catch (NumberFormatException ignored) {}
        }
        return defaultValue;
    }

    private static long longParam(Map<String, Object> p, String key, long defaultValue) {
        Object v = p.get(key);
        if (v instanceof Number n) return n.longValue();
        if (v instanceof String s) {
            try { return Long.parseLong(s); } catch (NumberFormatException ignored) {}
        }
        return defaultValue;
    }

    private static String hashPath(String path) {
        return String.valueOf(Objects.hash(path));
    }

    private static String buildParamsHash(AnalysisRequest request) {
        if (request.getParams() == null || request.getParams().isEmpty()) {
            return "default";
        }
        return String.valueOf(request.getParams().hashCode());
    }
}
