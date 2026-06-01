package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.domain.model.ExpandDiffCallTreeResult;
import io.github.deplague.jmcmcp.domain.service.CallTreeCache;
import io.github.deplague.jmcmcp.domain.service.ExpandDiffCallTreeService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Application service that orchestrates differential call tree expansion.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ExpandDiffCallTreeApplicationService {

    private final CallTreeCache callTreeCache;
    private final ExpandDiffCallTreeService expandService;

    public ExpandDiffCallTreeResult expand(String treeId, String nodeId) {
        CallTreeCache.CachedDiffTree cached = callTreeCache.getDiffTree(treeId);
        if (cached == null) {
            throw new IllegalArgumentException("Cached diff tree not found: " + treeId);
        }

        CallTreeCache.DiffTreeNode parentNode = CallTreeCache.findDiffNode(cached.root(), nodeId);
        if (parentNode == null) {
            throw new IllegalArgumentException("Node not found in diff tree: " + nodeId);
        }

        return expandService.expand(
                parentNode,
                treeId,
                nodeId,
                cached.packageFilter(),
                cached.baselineTotalSamples(),
                cached.targetTotalSamples()
        );
    }
}
