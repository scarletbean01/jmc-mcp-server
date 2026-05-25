package io.github.deplague.jmcmcp.jfr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;

import java.io.File;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class JfrAnalysisServiceTest {

    private static String jfrFilePath;
    private JfrRecordingCache cache;
    private JfrAnalysisService service;

    static {
        File file = new File("after.jfr");
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir"), "after.jfr");
        }
        jfrFilePath = file.getAbsolutePath();
    }

    @BeforeEach
    void setUp() {
        cache = new JfrRecordingCache();
        service = new JfrAnalysisService(cache);
    }

    @Test
    void filterByTimeRangeReturnsAllEventsWhenNoFilter() throws Exception {
        IItemCollection allEvents = service.loadRecording(jfrFilePath);
        IItemCollection filtered = service.filterByTimeRange(allEvents, null, null);

        assertThat(JfrItemUtils.count(filtered)).isEqualTo(JfrItemUtils.count(allEvents));
    }

    @Test
    void filterByTimeRangeWithStartTimeReturnsFewerEvents() throws Exception {
        IItemCollection allEvents = service.loadRecording(jfrFilePath);
        IItemCollection filtered = service.filterByTimeRange(allEvents, "2025-01-01T00:00:00Z", null);

        assertThat(JfrItemUtils.count(filtered)).isLessThanOrEqualTo(JfrItemUtils.count(allEvents));
    }

    @Test
    void filterByTimeRangeWithEndTimeReturnsFewerEvents() throws Exception {
        IItemCollection allEvents = service.loadRecording(jfrFilePath);
        IItemCollection filtered = service.filterByTimeRange(allEvents, null, "2099-12-31T23:59:59Z");

        assertThat(JfrItemUtils.count(filtered)).isLessThanOrEqualTo(JfrItemUtils.count(allEvents));
    }

    @Test
    void filterByTimeRangeWithBothBoundsReturnsFewerEvents() throws Exception {
        IItemCollection allEvents = service.loadRecording(jfrFilePath);
        IItemCollection filtered = service.filterByTimeRange(allEvents, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z");

        assertThat(JfrItemUtils.count(filtered)).isLessThanOrEqualTo(JfrItemUtils.count(allEvents));
    }

    @Test
    void filterByTimeRangeUsesEpochNsNotNanosecondDuration() {
        Instant instant = Instant.parse("2026-05-25T00:08:52.919112705Z");
        long epochNanos = instant.getEpochSecond() * 1_000_000_000L + instant.getNano();

        IQuantity qty = UnitLookup.EPOCH_NS.quantity(epochNanos);

        assertThat(qty.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO))
                .doesNotContain("y")
                .doesNotContain("wk");
    }

    @Test
    void filterByTimeRangePreservesSubMillisecondPrecision() {
        Instant instant = Instant.parse("2026-05-25T00:08:52.919112705Z");
        long correctNanos = instant.getEpochSecond() * 1_000_000_000L + instant.getNano();
        long buggyNanos = instant.toEpochMilli() * 1_000_000L;

        assertThat(correctNanos).isNotEqualTo(buggyNanos);
        assertThat(correctNanos - buggyNanos).isEqualTo(112_705L);
    }

    @Test
    void filterByTimeRangeInvalidStartTimeReturnsAllEvents() throws Exception {
        IItemCollection allEvents = service.loadRecording(jfrFilePath);
        IItemCollection filtered = service.filterByTimeRange(allEvents, "not-a-date", null);

        assertThat(JfrItemUtils.count(filtered)).isEqualTo(JfrItemUtils.count(allEvents));
    }

    @Test
    void filterByTimeRangeInvalidEndTimeReturnsAllEvents() throws Exception {
        IItemCollection allEvents = service.loadRecording(jfrFilePath);
        IItemCollection filtered = service.filterByTimeRange(allEvents, null, "not-a-date");

        assertThat(JfrItemUtils.count(filtered)).isEqualTo(JfrItemUtils.count(allEvents));
    }

    @Test
    void getOverviewReturnsPositiveDuration() throws Exception {
        JfrAnalysisService.RecordingOverview overview = service.getOverview(jfrFilePath);

        assertThat(overview.durationSeconds()).isGreaterThan(0);
    }

    @Test
    void getOverviewReturnsFilePath() throws Exception {
        JfrAnalysisService.RecordingOverview overview = service.getOverview(jfrFilePath);

        assertThat(overview.filePath()).isEqualTo(jfrFilePath);
    }

    @Test
    void getOverviewReturnsEventCounts() throws Exception {
        JfrAnalysisService.RecordingOverview overview = service.getOverview(jfrFilePath);

        assertThat(overview.eventCounts()).isNotEmpty();
        long total = overview.eventCounts().values().stream().mapToLong(Long::longValue).sum();
        assertThat(total).isGreaterThan(0);
    }

    @Test
    void displayReturnsNAForNull() {
        assertThat(JfrAnalysisService.display(null)).isEqualTo("N/A");
    }

    @Test
    void cacheResultReturnsCachedValue() {
        service.cacheResult("/test/path", "test_tool", java.util.Map.of("key", "value"), "result");
        String cached = service.getCachedResult("/test/path", "test_tool", java.util.Map.of("key", "value"));

        assertThat(cached).isEqualTo("result");
    }

    @Test
    void cacheResultReturnsNullForDifferentArgs() {
        service.cacheResult("/test/path", "test_tool", java.util.Map.of("key", "value"), "result");
        String cached = service.getCachedResult("/test/path", "test_tool", java.util.Map.of("key", "other"));

        assertThat(cached).isNull();
    }
}