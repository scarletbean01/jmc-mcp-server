package io.github.deplague.jmcmcp.adapters.infrastructure;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.time.Instant;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter that implements {@link JfrProvider} using
 * {@link JfrRecordingCache} and {@link RecordingAccessController}.
 */
@ApplicationScoped
public class JfrProviderImpl implements JfrProvider {

    private static final Logger LOG = LoggerFactory.getLogger(
            JfrProviderImpl.class
    );

    private final JfrRecordingCache cache;
    private final RecordingAccessController accessController;

    @Inject
    public JfrProviderImpl(
            JfrRecordingCache cache,
            RecordingAccessController accessController) {
        this.cache = cache;
        this.accessController = accessController;
    }

    @Override
    public IItemCollection loadRecording(String filePath) throws IOException {
        accessController.validate(filePath);
        return cache.load(filePath);
    }

    @Override
    public IItemCollection filterByTimeRange(
            IItemCollection events,
            String startTimeStr,
            String endTimeStr) {

        if (startTimeStr == null && endTimeStr == null) {
            return events;
        }

        IQuantity start = null;
        IQuantity end = null;

        if (startTimeStr != null) {
            try {
                Instant instant = Instant.parse(startTimeStr);
                long epochNanos = instant.getEpochSecond() * 1_000_000_000L
                        + instant.getNano();
                start = UnitLookup.EPOCH_NS.quantity(epochNanos);
            } catch (Exception e) {
                LOG.warn("Failed to parse start_time: {}", startTimeStr);
            }
        }

        if (endTimeStr != null) {
            try {
                Instant instant = Instant.parse(endTimeStr);
                long epochNanos = instant.getEpochSecond() * 1_000_000_000L
                        + instant.getNano();
                end = UnitLookup.EPOCH_NS.quantity(epochNanos);
            } catch (Exception e) {
                LOG.warn("Failed to parse end_time: {}", endTimeStr);
            }
        }

        if (start != null && end != null) {
            return events.apply(
                    ItemFilters.interval(
                            JfrAttributes.START_TIME, start, true, end, true
                    )
            );
        } else if (start != null) {
            return events.apply(
                    ItemFilters.moreOrEqual(JfrAttributes.START_TIME, start)
            );
        } else if (end != null) {
            return events.apply(
                    ItemFilters.lessOrEqual(JfrAttributes.END_TIME, end)
            );
        }

        return events;
    }
}
