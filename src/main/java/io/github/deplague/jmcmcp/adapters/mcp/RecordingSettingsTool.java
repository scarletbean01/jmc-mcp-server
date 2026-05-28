package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.RecordingSettingsResult;
import io.github.deplague.jmcmcp.domain.service.RecordingSettingsService;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * MCP tool adapter for extracting recording settings from a JFR file.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class RecordingSettingsTool implements McpTool {

    private static final String NAME = "recording_settings";

    private final JfrProvider jfrProvider;
    private final RecordingSettingsService recordingSettingsService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description("List the event settings and configurations used for a JFR recording.")
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp()
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(
                                request.arguments(),
                                "jfr_file_path"
                        );

                        IItemCollection events = jfrProvider.loadRecording(filePath);
                        RecordingSettingsResult result = recordingSettingsService.analyze(events);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
                                .build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String formatMarkdown(RecordingSettingsResult result) {
        if (!result.hasSettings()) {
            return "# JFR Recording Settings\n\nNo active settings events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# JFR Recording Settings\n\n");
        sb.append("| Event | Setting | Value |\n");
        sb.append("|-------|---------|-------|\n");

        for (var entry : result.settings()) {
            sb.append(String.format(
                    "| %s | %s | %s |%n",
                    entry.event(), entry.settingName(), entry.settingValue()
            ));
        }

        return sb.toString();
    }
}
