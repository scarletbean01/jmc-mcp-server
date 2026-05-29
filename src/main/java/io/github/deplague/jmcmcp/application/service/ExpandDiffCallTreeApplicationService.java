package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.infrastructure.jfr.CallTreeCache;
import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import io.github.deplague.jmcmcp.domain.model.ExpandDiffCallTreeResult;
import io.github.deplague.jmcmcp.domain.service.ExpandDiffCallTreeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Application service that orchestrates diff call tree expansion.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ExpandDiffCallTreeApplicationService {

    private final CallTreeCache callTreeCache;
    private final ExpandDiffCallTreeService expandDiffCallTreeService;

    public ExpandDiffCallTreeResult expand(String treeId, String nodeId) {
        CallTreeCache.CachedDiffTree cached = callTreeCache.getDiffTree(treeId);
        if (cached == null) {
            throw new AnalysisFailedException(
                    "Diff tree not found or expired. Please call `get_diff_tree` again.");
        }

        CallTreeCache.DiffTreeNode targetNode = CallTreeCache.findDiffNode(cached.root(), nodeId);
        if (targetNode == null) {
            throw new AnalysisFailedException(
                    "Node `" + nodeId + "` not found in diff tree.");
        }

        return expandDiffCallTreeService.expand(
                nodeId,
                targetNode,
                cached.packageFilter(),
                cached.baselineTotalSamples(),
                cached.targetTotalSamples());
    }
}
