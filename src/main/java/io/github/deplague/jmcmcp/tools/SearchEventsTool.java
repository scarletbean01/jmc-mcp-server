package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;

import java.io.IOException;
import java.util.Map;

/**
 * MCP tool for searching specific JFR events by type and optionally filtering them.
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
                        .description("Search for specific JFR events by type ID. " +
                                "Returns a list of matching events with their attributes.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "event_type", SchemaUtil.stringProp("JFR event type ID (e.g., jdk.CPULoad)"),
                                        "limit", SchemaUtil.intProp("Maximum number of events to return (default 20)", 20)
                                ),
                                SchemaUtil.required("jfr_file_path", "event_type")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        String eventType = getString(request.arguments(), "event_type");
                        int limit = getIntOrDefault(request.arguments(), "limit", 20);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = search(filePath, eventType, limit);
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

    private String search(String filePath, String eventType, int limit) throws IOException {
        IItemCollection events = service.loadRecording(filePath);
        var filtered = events.apply(ItemFilters.type(eventType));

        if (!filtered.hasItems()) {
            return "No events of type '" + eventType + "' found in this recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Search Results: ").append(eventType).append("\n\n");

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
                        sb.append("- **").append(key.getIdentifier()).append(":** ").append(val).append("\n");
                    }
                }
                sb.append("\n");
                count++;
            }
            if (count >= limit) break;
        }

        return sb.toString();
    }

    private static String getString(Map<String, Object> args, String key) {
        Object val = args.get(key);
        if (val == null) {
            throw new IllegalArgumentException("Missing required argument: " + key);
        }
        return val.toString();
    }

    @SuppressWarnings("DuplicatedCode")
    private static int getIntOrDefault(Map<String, Object> args, String key, int defaultValue) {
        Object val = args.get(key);
        if (val instanceof Number n) {
            return n.intValue();
        }
        if (val instanceof String s) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
