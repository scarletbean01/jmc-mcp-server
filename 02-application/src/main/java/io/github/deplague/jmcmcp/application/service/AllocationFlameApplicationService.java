package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.AllocationFlameResult;
import io.github.deplague.jmcmcp.domain.service.AllocationFlameService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Application service that orchestrates JFR loading and allocation flame graph analysis.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class AllocationFlameApplicationService {
    private final JfrProvider jfrProvider;
    private final AllocationFlameService allocationFlameService;

    public AllocationFlameResult analyze(
            String filePath,
            String startTime,
            String endTime,
            String packagePrefix,
            int topN) throws IOException {

        IItemCollection events = jfrProvider.loadRecording(filePath);
        IItemCollection filtered = jfrProvider.filterByTimeRange(events, startTime, endTime);

        return allocationFlameService.analyze(filtered, packagePrefix, topN);
    }
}
