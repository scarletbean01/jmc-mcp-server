package io.github.deplague.jmcmcp.infrastructure.jfr;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;

/**
 * Infrastructure adapter that implements {@link JfrProvider} using
 * {@link JfrRecordingCache} and {@link RecordingAccessController}.
 */
@ApplicationScoped
public class JfrProviderImpl implements JfrProvider {

    private static final Logger LOG = LoggerFactory.getLogger(
            JfrProviderImpl.class
    );

    /**
     * Epoch-second threshold: values below this are treated as relative-second
     * offsets from epoch-0 and are rejected (they almost certainly represent
     * relative seconds passed by callers, e.g. "0" and "30"). Values above
     * year-2000 in epoch-seconds (946_684_800) are treated as absolute epoch-seconds.
     */
    private static final long MIN_VALID_EPOCH_SECONDS = 946_684_800L; // 2000-01-01T00:00:00Z

    /**
     * If an integer value is larger than this it is likely epoch-milliseconds
     * rather than epoch-seconds.
     */
    private static final long EPOCH_MS_THRESHOLD = MIN_VALID_EPOCH_SECONDS * 1_000L;

    private final JfrRecordingCache cache;
    private final RecordingAccessController accessController;

    @Inject
    public JfrProviderImpl(
            JfrRecordingCache cache,
            RecordingAccessController accessController) {
        this.cache = cache;
        this.accessController = accessController;
    }

    @WithSpan("jfr.load-recording")
    @Override
    public IItemCollection loadRecording(String filePath) throws IOException {
        accessController.validate(filePath);
        return cache.load(filePath);
    }

    @WithSpan("jfr.filter-time-range")
    @Override
    public IItemCollection filterByTimeRange(
            IItemCollection events,
            String startTimeStr,
            String endTimeStr) {

        if (startTimeStr == null && endTimeStr == null) {
            return events;
        }

        IQuantity start = parseTimeQuantity(startTimeStr, "start_time");
        IQuantity end   = parseTimeQuantity(endTimeStr,   "end_time");

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

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------

    /**
     * Parse a time string into a JMC {@link IQuantity} expressed in epoch-nanoseconds.
     *
     * <p>Supported formats (tried in order):
     * <ol>
     *   <li>ISO-8601 instant string, e.g. {@code "2024-06-01T12:00:00Z"}</li>
     *   <li>Epoch-milliseconds integer (numeric value {@code >= 946_684_800_000})</li>
     *   <li>Epoch-seconds integer (numeric value {@code >= 946_684_800})</li>
     * </ol>
     *
     * <p>Values that look like small relative-second offsets (e.g. {@code "0"}, {@code "30"})
     * are intentionally ignored with a DEBUG-level message rather than a noisy WARN.
     *
     * @param raw       the raw string value supplied by the caller, may be {@code null}
     * @param fieldName used only for log messages
     * @return a JMC quantity or {@code null} if the value should be ignored
     */
    private IQuantity parseTimeQuantity(String raw, String fieldName) {
        if (raw == null || raw.isBlank()) {
            return null;
        }

        // --- Attempt 1: ISO-8601 ---
        try {
            Instant instant = Instant.parse(raw);
            long epochNanos = instant.getEpochSecond() * 1_000_000_000L + instant.getNano();
            return UnitLookup.EPOCH_NS.quantity(epochNanos);
        } catch (Exception ignored) {
            // Not ISO-8601; try numeric interpretation below.
        }

        // --- Attempt 2: numeric integer (epoch-ms or epoch-s) ---
        try {
            long numericValue = Long.parseLong(raw.strip());

            if (numericValue >= EPOCH_MS_THRESHOLD) {
                // Looks like epoch-milliseconds
                long epochNanos = numericValue * 1_000_000L;
                LOG.debug("Parsed {} as epoch-milliseconds: {} -> {} ns", fieldName, numericValue, epochNanos);
                return UnitLookup.EPOCH_NS.quantity(epochNanos);
            }

            if (numericValue >= MIN_VALID_EPOCH_SECONDS) {
                // Looks like epoch-seconds
                long epochNanos = numericValue * 1_000_000_000L;
                LOG.debug("Parsed {} as epoch-seconds: {} -> {} ns", fieldName, numericValue, epochNanos);
                return UnitLookup.EPOCH_NS.quantity(epochNanos);
            }

            // Small numeric value — almost certainly a relative-second offset
            // passed by an MCP tool or frontend. Silently ignore it so the
            // analysis runs over the full recording instead of filtering nothing.
            LOG.debug("Ignoring {} value '{}': looks like a relative-second offset, not an absolute timestamp. " +
                      "Supply an ISO-8601 string (e.g. 2024-06-01T12:00:00Z) for absolute time filtering.", fieldName, raw);
            return null;

        } catch (NumberFormatException ignored) {
            // Not a numeric value either.
        }

        // --- Unknown format ---
        LOG.warn("Cannot parse {} value '{}': expected ISO-8601 (e.g. 2024-06-01T12:00:00Z) or " +
                 "epoch-milliseconds/seconds integer. Time filter will be skipped for this bound.", fieldName, raw);
        return null;
    }
}
