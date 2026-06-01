package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.DiffStackTracesResult;
import io.github.deplague.jmcmcp.domain.service.DiffStackTracesService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Application service that orchestrates JFR loading and diff stack traces analysis.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class DiffStackTracesApplicationService {

    private final JfrProvider jfrProvider;
    private final DiffStackTracesService diffStackTracesService;

    public DiffStackTracesResult analyze(
            String baselinePath,
            String targetPath,
            String packagePrefix,
            int topN) throws IOException {

        IItemCollection baseline = jfrProvider.loadRecording(baselinePath);
        IItemCollection target = jfrProvider.loadRecording(targetPath);

        return diffStackTracesService.analyze(baseline, target, packagePrefix, topN);
    }
}
