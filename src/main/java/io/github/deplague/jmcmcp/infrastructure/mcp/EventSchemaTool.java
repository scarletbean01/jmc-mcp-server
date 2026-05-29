package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.EventSchemaApplicationService;
import io.github.deplague.jmcmcp.domain.model.EventSchemaResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for discovering JFR event types and their field schemas.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class EventSchemaTool {

    private final EventSchemaApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Discover JFR event types and their field schemas in a recording. Lists all event types with counts, or shows detailed field schema for a specific event type. Use this before search_events to find available event type IDs and their fields.")
    public ToolResponse eventSchema(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "event_type", required = false, description = "Optional event type ID to show detailed field schema (e.g., 'jdk.GCPhasePause'). If omitted, lists all event types.") String eventType
    ) {
        try {
            EventSchemaResult result = appService.analyze(jfrFilePath, eventType);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(EventSchemaResult result) {
        if (result.isCatalog()) {
            StringBuilder sb = new StringBuilder();
            sb.append("# Event Type Catalog\n\n");
            sb.append("| Event Type ID | Display Name | Event Count | Field Count |\n");
            sb.append("|---------------|-------------|-------------|-------------|\n");

            for (var info : result.catalog()) {
                sb.append(String.format(
                        "| `%s` | %s | %d | %d |%n",
                        info.typeId(), info.displayName(), info.eventCount(), info.fieldCount()
                ));
            }

            sb.append("\n**Tip:** Use `event_type` parameter with an event type ID to see detailed field schema.\n");
            return sb.toString();
        }

        EventSchemaResult.EventTypeDetail detail = result.detail().orElseThrow();

        if (detail.eventCount() == 0 && detail.fields().isEmpty()) {
            return "# Event Schema\n\nEvent type `" + detail.typeId() + "` not found in the recording. "
                    + "Use event_schema without event_type parameter to list all available event types.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Event Schema: `").append(detail.typeId()).append("`\n\n");

        sb.append("## Event Type Details\n\n");
        sb.append("| Property | Value |\n");
        sb.append("|----------|-------|\n");
        sb.append("| Identifier | `").append(detail.typeId()).append("` |\n");
        sb.append("| Display Name | ").append(detail.displayName()).append(" |\n");
        sb.append("| Event Count | ").append(detail.eventCount()).append(" |\n");
        sb.append("| Field Count | ").append(detail.fieldCount()).append(" |\n\n");

        sb.append("## Field Schema\n\n");
        sb.append("| Field | Description |\n");
        sb.append("|-------|-------------|\n");
        for (var field : detail.fields()) {
            sb.append(String.format("| `%s` | %s |%n", field.fieldId(), field.description()));
        }

        sb.append("\n**Tip:** Use field identifiers in the `attributes` parameter of `search_events` to query specific fields.\n");
        return sb.toString();
    }
}
