package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * MCP tool for inspecting JFR recording settings.
 */
public final class RecordingSettingsTool {

    private static final String NAME = "recording_settings";

    private final JfrAnalysisService service;

    public RecordingSettingsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Inspect the settings of the JFR recording, " +
                                "including which events were enabled, their thresholds, and stack trace settings.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file")
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath);
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

    private String analyze(String filePath) throws IOException {
        IItemCollection events = service.loadRecording(filePath);
        var settings = events.apply(ItemFilters.type("jdk.ActiveRecording"));

        StringBuilder sb = new StringBuilder();
        sb.append("# Recording Settings\n\n");

        if (settings.hasItems()) {
            for (var iterable : settings) {
                for (var item : iterable) {
                    sb.append("## Active Recording Details\n");
                    sb.append("- **Name:** ").append(String.valueOf(JfrItemUtils.getMember(item, "name"))).append("\n");
                    sb.append("- **Recording Start:** ").append(String.valueOf(JfrItemUtils.getMember(item, "recordingStart"))).append("\n");
                    sb.append("- **Recording Duration:** ").append(String.valueOf(JfrItemUtils.getMember(item, "recordingDuration"))).append("\n");
                    sb.append("- **Destination:** ").append(String.valueOf(JfrItemUtils.getMember(item, "destination"))).append("\n");
                    sb.append("\n");
                    break;
                }
                break;
            }
        }

        var activeSettings = events.apply(ItemFilters.type("jdk.ActiveSetting"));
        if (activeSettings.hasItems()) {
            sb.append("## Event Settings\n");
            // Group settings by event type
            Map<String, Map<String, String>> eventToSettings = new TreeMap<>();
            for (var iterable : activeSettings) {
                for (var item : iterable) {
                    String eventName = JfrItemUtils.getMember(item, "id");
                    String settingName = JfrItemUtils.getMember(item, "name");
                    String settingValue = JfrItemUtils.getMember(item, "value");
                    if (eventName != null && settingName != null) {
                        eventToSettings.computeIfAbsent(eventName, k -> new TreeMap<>())
                                .put(settingName, settingValue);
                    }
                }
            }

            eventToSettings.forEach((event, props) -> {
                sb.append("### `").append(event).append("`\n");
                props.forEach((name, value) -> sb.append("- **").append(name).append(":** ").append(value).append("\n"));
                sb.append("\n");
            });
        }

        if (!settings.hasItems() && !activeSettings.hasItems()) {
            sb.append("No recording settings events found.\n");
        }

        return sb.toString();
    }


}
