package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.domain.model.ExpandCallTreeResult;
import io.github.deplague.jmcmcp.domain.service.ExpandCallTreeService;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.CallTreeCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Application service that orchestrates call tree node expansion.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ExpandCallTreeApplicationService {

    private final CallTreeCache callTreeCache;
    private final ExpandCallTreeService expandCallTreeService;

    public ExpandCallTreeResult expand(String treeId, String nodeId) {
        CallTreeCache.CachedTree cached = callTreeCache.getTree(treeId);
        if (cached == null) {
            throw new IllegalArgumentException(
                    "Tree not found or expired. Please call `get_call_tree` again."
            );
        }
        return expandCallTreeService.expand(
                cached.tree().getRoot(),
                nodeId,
                cached.packageFilter(),
                cached.totalSamples()
        );
    }
}
