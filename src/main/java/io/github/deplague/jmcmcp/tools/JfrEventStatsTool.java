package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP tool for per-event-type statistical summaries.
 */
public final class JfrEventStatsTool {

    private static final String NAME = "jfr_event_stats";
    private final JfrAnalysisService service;

    public JfrEventStatsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Provide statistical summaries for specific event types (e.g. jdk.GCPhasePause).")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "event_type", SchemaUtil.stringProp("Event type ID to analyze"),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp()
                                ),
                                SchemaUtil.required("jfr_file_path", "event_type")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String eventType = SchemaUtil.getString(request.arguments(), "event_type");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }
                        String result = analyze(filePath, eventType, startTimeStr, endTimeStr);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                }).build();
    }

    private String analyze(String filePath, String eventType, String startTimeStr, String endTimeStr) throws Exception {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection targetEvents = events.apply(ItemFilters.type(eventType));
        long count = JfrItemUtils.count(targetEvents);
        
        if (count == 0) {
            return "# Event Statistics: " + eventType + "\n\nNo events found of this type.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Event Statistics: ").append(eventType).append("\n\n");
        sb.append("- **Total Events:** ").append(count).append("\n\n");

        if (!targetEvents.iterator().hasNext()) return sb.toString();
        IType<IItem> type = targetEvents.iterator().next().getType();

        sb.append("## Numeric Field Statistics\n");
        sb.append("| Field | Min | Avg | Max | P95 |\n|---|---|---|---|---|\n");
        
        Map<String, IAccessorKey<?>> stringFields = new HashMap<>();

        for (IAccessorKey<?> key : type.getAccessorKeys().keySet()) {
            String identifier = key.getIdentifier();
            
            // Just check if it returns quantity by sampling the first value if possible
            // But we can just use JfrItemUtils.maxQuantity to see if it yields anything
            IQuantity max = JfrItemUtils.maxQuantity(targetEvents, identifier);
            if (max != null) {
                IQuantity min = JfrItemUtils.minQuantity(targetEvents, identifier);
                IQuantity avg = JfrItemUtils.avgQuantity(targetEvents, identifier);
                IQuantity p95 = JfrItemUtils.percentileQuantity(targetEvents, identifier, 95);
                sb.append(String.format("| `%s` | %s | %s | %s | %s |\n",
                        identifier,
                        JfrAnalysisService.display(min),
                        JfrAnalysisService.display(avg),
                        JfrAnalysisService.display(max),
                        JfrAnalysisService.display(p95)));
            } else {
                stringFields.put(identifier, key);
            }
        }
        sb.append("\n");

        sb.append("## Categorical Field Distribution (Top 5)\n");
        for (String identifier : stringFields.keySet()) {
            if ("startTime".equals(identifier) || "endTime".equals(identifier) || "duration".equals(identifier) || "stackTrace".equals(identifier)) {
                continue; // skip common non-categorical
            }
            Map<String, Long> dist = new HashMap<>();
            for (IItemIterable iterable : targetEvents) {
                IMemberAccessor<Object, IItem> acc = JfrItemUtils.getAccessor(iterable.getType(), identifier);
                if (acc != null) {
                    for (IItem item : iterable) {
                        Object val = acc.getMember(item);
                        if (val != null) {
                            dist.merge(val.toString(), 1L, Long::sum);
                        }
                    }
                }
            }
            if (!dist.isEmpty() && dist.size() < count) {
                sb.append("### Field: `").append(identifier).append("`\n");
                sb.append("| Value | Count |\n|---|---|\n");
                dist.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(5)
                        .forEach(e -> sb.append("| `").append(e.getKey()).append("` | ").append(e.getValue()).append(" |\n"));
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
