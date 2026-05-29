package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.RecordingSettingsResult;
import io.github.deplague.jmcmcp.domain.service.RecordingSettingsService;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * MCP tool adapter for extracting recording settings from a JFR file.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class RecordingSettingsTool {

    private final JfrProvider jfrProvider;
    private final RecordingSettingsService recordingSettingsService;

    @RunOnVirtualThread
    @Tool(description = "List the event settings and configurations used for a JFR recording.")
    public ToolResponse recordingSettings(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file")
            String jfrFilePath) {
        try {
            IItemCollection events = jfrProvider.loadRecording(jfrFilePath);
            RecordingSettingsResult result = recordingSettingsService.analyze(events);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
