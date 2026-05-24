package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MCP tool for extracting recording settings from a JFR file.
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
                        .description("List the event settings and configurations used for a JFR recording.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp()
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
        StringBuilder sb = new StringBuilder();
        sb.append("# JFR Recording Settings\n\n");

        IItemCollection settings = events.apply(org.openjdk.jmc.common.item.ItemFilters.type("jdk.ActiveSetting"));
        if (settings.hasItems()) {
            sb.append("| Event | Setting | Value |\n");
            sb.append("|-------|---------|-------|\n");

            List<IItem> sorted = new ArrayList<>();
            settings.forEach(iterable -> iterable.forEach(sorted::add));
            sorted.stream()
                    .sorted((a, b) -> {
                        String na = String.valueOf(JfrItemUtils.getMember(a, "name").orElse(null));
                        String nb = String.valueOf(JfrItemUtils.getMember(b, "name").orElse(null));
                        return na.compareTo(nb);
                    })
                    .forEach(item -> {
                        Object name = JfrItemUtils.getMember(item, "name").orElse(null);
                        Object settingName = JfrItemUtils.getMember(item, "settingName").orElse(null);
                        Object settingValue = JfrItemUtils.getMember(item, "settingValue").orElse(null);
                        sb.append(String.format("| %s | %s | %s |%n", name, settingName, settingValue));
                    });
        } else {
            sb.append("No active settings events found in the recording.\n");
        }

        return sb.toString();
    }
}
