package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IAccessorKey;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IType;
import org.openjdk.jmc.common.item.ItemFilters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class EventSchemaTool {

    private static final String NAME = "event_schema";

    private final JfrAnalysisService service;

    public EventSchemaTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Discover JFR event types and their field schemas in a recording. " +
                                "Lists all event types with counts, or shows detailed field schema for a specific event type. " +
                                "Use this before search_events to find available event type IDs and their fields.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "event_type", SchemaUtil.stringProp("Optional event type ID to show detailed field schema (e.g., 'jdk.GCPhasePause'). If omitted, lists all event types.")
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String eventType = SchemaUtil.getStringOrDefault(request.arguments(), "event_type", null);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, eventType);
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

    private String analyze(String filePath, String eventType) throws IOException {
        IItemCollection events = service.loadRecording(filePath);

        if (eventType != null && !eventType.isEmpty()) {
            return showDetailedSchema(events, eventType);
        } else {
            return showEventCatalog(events);
        }
    }

    private String showEventCatalog(IItemCollection events) {
        List<EventTypeInfo> typeInfos = new ArrayList<>();

        for (IItemIterable iterable : events) {
            IType<?> type = iterable.getType();
            String typeId = type.getIdentifier();
            long count = iterable.stream().count();
            int fieldCount = type.getAccessorKeys().size();

            typeInfos.add(new EventTypeInfo(typeId, type.getName(), count, fieldCount));
        }

        typeInfos.sort(Comparator.comparing(EventTypeInfo::typeId));

        StringBuilder sb = new StringBuilder();
        sb.append("# Event Type Catalog\n\n");
        sb.append("| Event Type ID | Display Name | Event Count | Field Count |\n");
        sb.append("|---------------|-------------|-------------|-------------|\n");

        for (EventTypeInfo info : typeInfos) {
            sb.append(String.format("| `%s` | %s | %d | %d |%n",
                    info.typeId, info.displayName, info.eventCount, info.fieldCount));
        }

        sb.append("\n**Tip:** Use `event_type` parameter with an event type ID to see detailed field schema.\n");
        return sb.toString();
    }

    private String showDetailedSchema(IItemCollection events, String eventType) {
        IItemCollection filtered = events.apply(ItemFilters.type(eventType));
        if (!filtered.hasItems()) {
            return "# Event Schema\n\nEvent type `" + eventType + "` not found in the recording. " +
                    "Use event_schema without event_type parameter to list all available event types.";
        }

        IType<?> type = null;
        long eventCount = 0;
        for (IItemIterable iterable : filtered) {
            type = iterable.getType();
            eventCount += iterable.stream().count();
        }

        if (type == null) {
            return "# Event Schema\n\nNo items found for event type `" + eventType + "`.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Event Schema: `").append(eventType).append("`\n\n");

        sb.append("## Event Type Details\n\n");
        sb.append("| Property | Value |\n");
        sb.append("|----------|-------|\n");
        sb.append("| Identifier | `").append(type.getIdentifier()).append("` |\n");
        sb.append("| Display Name | ").append(type.getName()).append(" |\n");
        sb.append("| Event Count | ").append(eventCount).append(" |\n");
        sb.append("| Field Count | ").append(type.getAccessorKeys().size()).append(" |\n\n");

        sb.append("## Field Schema\n\n");
        sb.append("| Field | Description |\n");
        sb.append("|-------|-------------|\n");

        List<Map.Entry<IAccessorKey<?>, ? extends org.openjdk.jmc.common.IDescribable>> fields = new ArrayList<>(type.getAccessorKeys().entrySet());
        fields.sort(Comparator.comparing(e -> e.getKey().getIdentifier()));

        for (Map.Entry<IAccessorKey<?>, ? extends org.openjdk.jmc.common.IDescribable> entry : fields) {
            String fieldId = entry.getKey().getIdentifier();
            String description = entry.getValue().getDescription();
            sb.append(String.format("| `%s` | %s |%n",
                    fieldId, description != null ? description : ""));
        }

        sb.append("\n**Tip:** Use field identifiers in the `attributes` parameter of `search_events` to query specific fields.\n");
        return sb.toString();
    }

    private record EventTypeInfo(String typeId, String displayName, long eventCount, int fieldCount) {}
}