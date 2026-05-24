package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;

import java.io.IOException;

/**
 * MCP tool for searching specific JFR events.
 */
public final class SearchEventsTool {

    private static final String NAME = "search_events";

    private final JfrAnalysisService service;

    public SearchEventsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Search for specific JFR events by type ID.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "event_type", SchemaUtil.stringProp("JFR event type ID (e.g., jdk.CPULoad)"),
                                        "limit", SchemaUtil.intProp("Maximum number of events to return (default 20)", 20)
                                ),
                                SchemaUtil.required("jfr_file_path", "event_type")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String eventType = SchemaUtil.getString(request.arguments(), "event_type");
                        int limit = SchemaUtil.getIntOrDefault(request.arguments(), "limit", 20);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = search(filePath, startTimeStr, endTimeStr, eventType, limit);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String search(String filePath, String startTimeStr, String endTimeStr, String eventType, int limit) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);
        var filtered = events.apply(ItemFilters.type(eventType));

        if (!filtered.hasItems()) {
            return "No events of type '" + eventType + "' found in this recording range.";
        }

        StringBuilder sb = new StringBuilder();
        String displayName = "Unknown";
        for (var iter : filtered) {
            displayName = iter.getType().getName();
            break;
        }
        sb.append("# Search Results: ").append(eventType).append(" (").append(displayName).append(")\n\n");

        int count = 0;
        for (var itemIterable : filtered) {
            var type = itemIterable.getType();
            var keyMap = type.getAccessorKeys();

            for (IItem item : itemIterable) {
                if (count >= limit) break;

                sb.append("### Event ").append(count + 1).append("\n");
                for (var entry : keyMap.entrySet()) {
                    var key = entry.getKey();
                    Object val = type.getAccessor(key).getMember(item);
                    if (val != null) {
                        String valStr = val.toString();
                        if (valStr.length() > 500) {
                            valStr = valStr.substring(0, 500) + "... [truncated]";
                        }
                        sb.append("- **").append(key.getIdentifier()).append(":** ").append(valStr).append("\n");
                    }
                }
                sb.append("\n");
                count++;
            }
            if (count >= limit) break;
        }

        return sb.toString();
    }
}
