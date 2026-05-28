package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.CallTreeResult;
import io.github.deplague.jmcmcp.domain.service.CallTreeService;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.CallTreeCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class CallTreeApplicationService {
    private final JfrProvider jfrProvider;
    private final CallTreeService callTreeService;
    private final CallTreeCache callTreeCache;

    public CallTreeCache getCallTreeCache() {
        return callTreeCache;
    }

    public CallTreeResult analyze(
            String filePath,
            String subsystem,
            String packageFilter,
            String startTimeStr,
            String endTimeStr) throws IOException {

        IItemCollection events = jfrProvider.loadRecording(filePath);
        IItemCollection filtered = jfrProvider.filterByTimeRange(events, startTimeStr, endTimeStr);

        CallTreeService.CallTreeAnalysis analysis = callTreeService.analyze(filtered, subsystem, packageFilter);

        if (analysis.treeModel() == null) {
            return new CallTreeResult(null, subsystem, packageFilter, 0, List.of());
        }

        String treeId = callTreeCache.cacheTree(analysis.treeModel(), filePath, subsystem, packageFilter);
        return new CallTreeResult(treeId, subsystem, packageFilter, analysis.totalSamples(), analysis.nodes());
    }
}
