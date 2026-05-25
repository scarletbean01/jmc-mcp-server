package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public final class IncidentTimelineTool {

    private static final String NAME = "smart_incident_timeline";
    private final JfrAnalysisService service;

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

    public IncidentTimelineTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Recreate an incident timeline by locating an anchor event (or timestamp) and dumping a chronological list of high-impact events surrounding it.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "anchor_event", SchemaUtil.stringProp("Optional event type to use as anchor (e.g., jdk.JavaErrorThrow)"),
                                        "anchor_time", SchemaUtil.stringProp("Optional ISO-8601 anchor time (used if anchor_event is omitted)"),
                                        "window_ms", SchemaUtil.intProp("Time window in milliseconds to inspect around the anchor (default 2000)", 2000)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String anchorEvent = SchemaUtil.getStringOrDefault(request.arguments(), "anchor_event", null);
                        String anchorTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "anchor_time", null);
                        int windowMs = SchemaUtil.getIntOrDefault(request.arguments(), "window_ms", 2000);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }
                        String result = analyze(filePath, anchorEvent, anchorTimeStr, windowMs);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                }).build();
    }

    private String analyze(String filePath, String anchorEvent, String anchorTimeStr, int windowMs) throws Exception {
        IItemCollection events = service.loadRecording(filePath);

        long anchorMillis = -1;

        if (anchorEvent != null && !anchorEvent.isEmpty()) {
            IItemCollection anchors = events.apply(ItemFilters.type(anchorEvent));
            for (IItemIterable iterable : anchors) {
                var timeAcc = JfrAttributes.START_TIME.getAccessor(iterable.getType());
                if (timeAcc != null && iterable.iterator().hasNext()) {
                    IItem first = iterable.iterator().next();
                    IQuantity time = timeAcc.getMember(first);
                    if (time != null) {
                        anchorMillis = time.clampedLongValueIn(UnitLookup.EPOCH_MS);
                        break;
                    }
                }
            }
            if (anchorMillis == -1) {
                return "# Incident Timeline\n\nCould not find anchor event: " + anchorEvent;
            }
        } else if (anchorTimeStr != null && !anchorTimeStr.isEmpty()) {
            try {
                anchorMillis = java.time.Instant.parse(anchorTimeStr).toEpochMilli();
            } catch (Exception e) {
                return "# Incident Timeline\n\nFailed to parse anchor_time. Use ISO-8601 format.";
            }
        } else {
            return "# Incident Timeline\n\nMust provide either anchor_event or anchor_time.";
        }

        long startMillis = anchorMillis - windowMs;
        long endMillis = anchorMillis + windowMs;
        IQuantity startQ = UnitLookup.EPOCH_MS.quantity(startMillis);
        IQuantity endQ = UnitLookup.EPOCH_MS.quantity(endMillis);

        IItemCollection windowEvents = events.apply(ItemFilters.interval(JfrAttributes.START_TIME, startQ, true, endQ, true));

        List<TimelineEvent> timeline = new ArrayList<>();

        for (IItemIterable iterable : windowEvents) {
            String typeId = iterable.getType().getIdentifier();
            if (!SIGNIFICANT_EVENTS.contains(typeId)) continue;

            var timeAcc = JfrAttributes.START_TIME.getAccessor(iterable.getType());
            var durAcc = JfrAttributes.DURATION.getAccessor(iterable.getType());
            var threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");

            if (timeAcc != null) {
                for (IItem item : iterable) {
                    IQuantity timeQ = timeAcc.getMember(item);
                    if (timeQ != null) {
                        long ts = timeQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
                        String durStr = "";
                        if (durAcc != null) {
                            IQuantity dur = durAcc.getMember(item);
                            if (dur != null) {
                                durStr = " [Dur: " + JfrAnalysisService.display(dur) + "]";
                            }
                        }
                        String threadName = "";
                        if (threadAcc != null) {
                            Object t = threadAcc.getMember(item);
                            if (t != null) threadName = " (" + t.toString() + ")";
                        }

                        // Get some context field
                        String context = "";
                        if (typeId.contains("Exception") || typeId.contains("Error")) {
                            context = JfrItemUtils.getMember(item, "thrownClass").map(Object::toString).orElse("");
                        } else if (typeId.contains("MonitorEnter") || typeId.contains("ThreadPark")) {
                            context = JfrItemUtils.getMember(item, "monitorClass").map(Object::toString).orElse("");
                        } else if (typeId.contains("File") || typeId.contains("Socket")) {
                            context = JfrItemUtils.getMember(item, "path").map(Object::toString).orElse("");
                            if (context.isEmpty()) context = JfrItemUtils.getMember(item, "host").map(Object::toString).orElse("");
                        } else if (typeId.contains("PhasePause") || typeId.contains("ExecuteVMOperation") || typeId.contains("Safepoint")) {
                            context = JfrItemUtils.getMember(item, "name").map(Object::toString).orElse(
                                    JfrItemUtils.getMember(item, "operation").map(Object::toString).orElse("")
                            );
                        }

                        timeline.add(new TimelineEvent(ts, SchemaUtil.formatTime(ts) + durStr + threadName + " - **" + typeId + "** " + context));
                        if (timeline.size() > 1000) break;
                    }
                }
            }
            if (timeline.size() > 1000) break;
        }

        timeline.sort(Comparator.comparingLong(t -> t.timestamp));

        StringBuilder sb = new StringBuilder();
        sb.append("# Incident Timeline\n\n");
        sb.append("**Anchor Time:** ").append(SchemaUtil.formatTime(anchorMillis)).append("\n");
        sb.append("**Window:** +/- ").append(windowMs).append("ms\n\n");

        if (timeline.isEmpty()) {
            sb.append("No significant events found in the window.\n");
        } else {
            if (timeline.size() >= 1000) sb.append("> **Note:** Timeline truncated to 1000 events.\n\n");
            for (TimelineEvent te : timeline) {
                if (te.timestamp == anchorMillis && anchorEvent != null) {
                    sb.append("- ").append(te.desc).append("  **<-- ANCHOR**\n");
                } else {
                    sb.append("- ").append(te.desc).append("\n");
                }
            }
        }

        return sb.toString();
    }

    private record TimelineEvent(long timestamp, String desc) {}
}