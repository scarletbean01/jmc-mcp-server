package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.service.CompareRecordingsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit;

import java.io.IOException;

/**
 * Application service that orchestrates loading two JFR recordings and
 * comparing them via the domain service.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class CompareRecordingsApplicationService {

    private final JfrProvider jfrProvider;
    private final CompareRecordingsService compareRecordingsService;

    public String analyze(String baselinePath, String targetPath) throws IOException {
        IItemCollection baselineEvents = jfrProvider.loadRecording(baselinePath);
        IItemCollection targetEvents = jfrProvider.loadRecording(targetPath);

        double baselineDurationSec = getDurationSeconds(baselineEvents);
        double targetDurationSec = getDurationSeconds(targetEvents);

        return compareRecordingsService.analyze(
                baselineEvents,
                baselineDurationSec,
                baselinePath,
                targetEvents,
                targetDurationSec,
                targetPath
        );
    }

    public io.github.deplague.jmcmcp.domain.model.RecordingComparisonResult analyzeStructured(String baselinePath, String targetPath) throws IOException {
        IItemCollection baselineEvents = jfrProvider.loadRecording(baselinePath);
        IItemCollection targetEvents = jfrProvider.loadRecording(targetPath);

        double baselineDurationSec = getDurationSeconds(baselineEvents);
        double targetDurationSec = getDurationSeconds(targetEvents);

        return compareRecordingsService.analyzeStructured(
                baselineEvents,
                baselineDurationSec,
                baselinePath,
                targetEvents,
                targetDurationSec,
                targetPath
        );
    }

    private double getDurationSeconds(IItemCollection events) {
        IQuantity start = RulesToolkit.getEarliestStartTime(events);
        IQuantity end = RulesToolkit.getLatestEndTime(events);
        if (start != null && end != null) {
            double durationSec = end.subtract(start).doubleValueIn(UnitLookup.SECOND);
            return durationSec > 0 ? durationSec : 1.0;
        }
        return 1.0;
    }
}
