package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.DiffCallTreeData;
import io.github.deplague.jmcmcp.domain.model.DiffCallTreeNodeEntry;
import io.github.deplague.jmcmcp.domain.model.DiffCallTreeResult;
import io.github.deplague.jmcmcp.domain.service.DiffCallTreeService;
import io.github.deplague.jmcmcp.infrastructure.jfr.CallTreeCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Application service that orchestrates JFR loading and diff call tree analysis.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class DiffCallTreeApplicationService {

    private final JfrProvider jfrProvider;
    private final DiffCallTreeService diffCallTreeService;
    @Getter
    private final CallTreeCache callTreeCache;

    public DiffCallTreeResult analyze(
            String baselinePath,
            String targetPath,
            String subsystem,
            String packageFilter) throws IOException {

        IItemCollection baselineEvents = jfrProvider.loadRecording(baselinePath);
        IItemCollection targetEvents = jfrProvider.loadRecording(targetPath);

        DiffCallTreeData data = diffCallTreeService.analyze(baselineEvents, targetEvents, subsystem);

        if (!data.hasEvents()) {
            return new DiffCallTreeResult(null, subsystem, packageFilter, 0, 0, List.of(), false);
        }

        String treeId = callTreeCache.cacheDiffTree(
                data.root(), baselinePath, targetPath, subsystem, packageFilter,
                data.baselineTotal(), data.targetTotal());

        List<DiffCallTreeNodeEntry> nodes = new ArrayList<>();
        List<CallTreeCache.DiffTreeNode> visibleChildren = CallTreeCache.getVisibleDiffChildren(data.root(), packageFilter);
        for (CallTreeCache.DiffTreeNode child : visibleChildren) {
            double baselinePct = data.baselineTotal() > 0
                    ? (child.baselineCumulative() / data.baselineTotal()) * 100.0 : 0.0;
            double targetPct = data.targetTotal() > 0
                    ? (child.targetCumulative() / data.targetTotal()) * 100.0 : 0.0;
            boolean hasChildren = !CallTreeCache.getVisibleDiffChildren(child, null).isEmpty();
            nodes.add(new DiffCallTreeNodeEntry(
                    CallTreeCache.formatMethodName(child),
                    child.baselineCumulative(),
                    child.targetCumulative(),
                    child.delta(),
                    baselinePct,
                    targetPct,
                    child.changeType(),
                    hasChildren
            ));
        }

        return new DiffCallTreeResult(treeId, subsystem, packageFilter,
                data.baselineTotal(), data.targetTotal(), nodes, true);
    }
}
