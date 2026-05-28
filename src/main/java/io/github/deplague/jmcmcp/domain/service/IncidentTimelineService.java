package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.IncidentTimelineResult;
import io.github.deplague.jmcmcp.domain.model.TimelineEventEntry;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Domain service for incident timeline analysis.
 */
public class IncidentTimelineService {

    private static final Set<String> SIGNIFICANT_EVENTS = Set.of(
            "jdk.GCPhasePause",
            "jdk.ExecuteVMOperation",
            "jdk.SafepointBegin",
            "jdk.JavaExceptionThrow",
            "jdk.JavaErrorThrow",
            "jdk.SocketRead",
            "jdk.SocketWrite",
            "jdk.FileRead",
            "jdk.FileWrite",
            "jdk.JavaMonitorEnter",
            "jdk.ThreadPark",
            "jdk.Compilation",
            "jdk.Deoptimization"
    );

    public IncidentTimelineResult analyze(
            IItemCollection events,
            String anchorEvent,
            String anchorTimeStr,
            int windowMs
    ) {
        long anchorMillis = resolveAnchorMillis(events, anchorEvent, anchorTimeStr);
        if (anchorMillis < 0) {
            // Error cases encoded as special results
            if (anchorMillis == -1) {
                return new IncidentTimelineResult(
                        null, windowMs, false,
                        List.of()
                );
            }
            if (anchorMillis == -2) {
                return new IncidentTimelineResult(
                        null, windowMs, false,
                        List.of()
                );
            }
        }

        long startMillis = anchorMillis - windowMs;
        long endMillis = anchorMillis + windowMs;
        IQuantity startQ = UnitLookup.EPOCH_MS.quantity(startMillis);
        IQuantity endQ = UnitLookup.EPOCH_MS.quantity(endMillis);

        IItemCollection windowEvents = events.apply(
                ItemFilters.interval(JfrAttributes.START_TIME, startQ, true, endQ, true)
        );

        List<TimelineEventEntry> timeline = new ArrayList<>();

        for (IItemIterable iterable : windowEvents) {
            String typeId = iterable.getType().getIdentifier();
            if (!SIGNIFICANT_EVENTS.contains(typeId)) {
                continue;
            }

            var timeAcc = JfrAttributes.START_TIME.getAccessor(iterable.getType());
            var durAcc = JfrAttributes.DURATION.getAccessor(iterable.getType());
            var threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");

            if (timeAcc != null) {
                for (IItem item : iterable) {
                    IQuantity timeQ = timeAcc.getMember(item);
                    if (timeQ != null) {
                        long ts = timeQ.clampedLongValueIn(UnitLookup.EPOCH_MS);

                        StringBuilder desc = new StringBuilder();

                        if (durAcc != null) {
                            IQuantity dur = durAcc.getMember(item);
                            if (dur != null) {
                                desc.append(" [Dur: ")
                                        .append(dur.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO))
                                        .append("]");
                            }
                        }

                        if (threadAcc != null) {
                            Object t = threadAcc.getMember(item);
                            if (t != null) {
                                desc.append(" (").append(t.toString()).append(")");
                            }
                        }

                        String context = extractContext(item, typeId);
                        desc.append(" - **").append(typeId).append("** ").append(context);

                        boolean isAnchor = ts == anchorMillis && anchorEvent != null;
                        timeline.add(new TimelineEventEntry(
                                ts,
                                java.time.Instant.ofEpochMilli(ts).toString(),
                                desc.toString(),
                                typeId,
                                isAnchor
                        ));

                        if (timeline.size() > 1000) {
                            break;
                        }
                    }
                }
            }
            if (timeline.size() > 1000) {
                break;
            }
        }

        timeline.sort(Comparator.comparingLong(TimelineEventEntry::timestampMillis));

        return new IncidentTimelineResult(
                java.time.Instant.ofEpochMilli(anchorMillis).toString(),
                windowMs,
                timeline.size() > 1000,
                timeline
        );
    }

    private long resolveAnchorMillis(IItemCollection events, String anchorEvent, String anchorTimeStr) {
        if (anchorEvent != null && !anchorEvent.isEmpty()) {
            IItemCollection anchors = events.apply(ItemFilters.type(anchorEvent));
            for (IItemIterable iterable : anchors) {
                var timeAcc = JfrAttributes.START_TIME.getAccessor(iterable.getType());
                if (timeAcc != null && iterable.iterator().hasNext()) {
                    IItem first = iterable.iterator().next();
                    IQuantity time = timeAcc.getMember(first);
                    if (time != null) {
                        return time.clampedLongValueIn(UnitLookup.EPOCH_MS);
                    }
                }
            }
            return -1; // anchor event not found
        }

        if (anchorTimeStr != null && !anchorTimeStr.isEmpty()) {
            try {
                return Instant.parse(anchorTimeStr).toEpochMilli();
            } catch (Exception e) {
                return -2; // parse error
            }
        }

        return -3; // no anchor provided
    }

    private String extractContext(IItem item, String typeId) {
        if (typeId.contains("Exception") || typeId.contains("Error")) {
            return JfrItemUtils.getMember(item, "thrownClass").map(Object::toString).orElse("");
        }
        if (typeId.contains("MonitorEnter") || typeId.contains("ThreadPark")) {
            return JfrItemUtils.getMember(item, "monitorClass").map(Object::toString).orElse("");
        }
        if (typeId.contains("File") || typeId.contains("Socket")) {
            String path = JfrItemUtils.getMember(item, "path").map(Object::toString).orElse("");
            if (!path.isEmpty()) {
                return path;
            }
            return JfrItemUtils.getMember(item, "host").map(Object::toString).orElse("");
        }
        if (typeId.contains("PhasePause") || typeId.contains("ExecuteVMOperation") || typeId.contains("Safepoint")) {
            return JfrItemUtils.getMember(item, "name").map(Object::toString).orElse(
                    JfrItemUtils.getMember(item, "operation").map(Object::toString).orElse("")
            );
        }
        return "";
    }
}
