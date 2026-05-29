package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.LiveRecordingApplicationService;
import io.github.deplague.jmcmcp.domain.model.*;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for managing live JFR recordings on a running JVM via JMX.
 * Delegates to the application layer and formats results as Markdown.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class LiveRecordingTool {

    private final LiveRecordingApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Connect to a running JVM via JMX and manage JFR recordings. Supports listing existing recordings, starting a new recording, stopping a recording, and dumping a recording to a file. Requires the target JVM to have JFR enabled and JMX accessible.")
    public ToolResponse liveRecording(
            @ToolArg(name = "jmx_url", description = "JMX service URL, e.g. service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi") String jmxUrl,
            @ToolArg(name = "action", description = "Action to perform: list, start, stop, dump") String action,
            @ToolArg(name = "recording_name", required = false, description = "Name for the recording (start action only)") String recordingName,
            @ToolArg(name = "settings", required = false, description = "Recording settings profile: default or profile (start action only)") String settings,
            @ToolArg(name = "duration_seconds", required = false, description = "Recording duration in seconds (start action only, default 60)") Integer durationSeconds,
            @ToolArg(name = "recording_id", required = false, description = "Recording ID (stop and dump actions)") Long recordingId,
            @ToolArg(name = "output_path", required = false, description = "Output file path for dump action") String outputPath
    ) {
        try {
            String result = switch (action) {
                case "list" -> list(jmxUrl);
                case "start" -> start(jmxUrl, recordingName, durationSeconds);
                case "stop" -> stop(jmxUrl, recordingId);
                case "dump" -> dump(jmxUrl, recordingId, outputPath);
                default -> "Unknown action: " + action + ". Supported: list, start, stop, dump.";
            };
            return ToolResponse.success(result);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String list(String jmxUrl) throws Exception {
        LiveRecordingListResult result = appService.list(jmxUrl);

        if (result.isEmpty()) {
            return "No active JFR recordings found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Active JFR Recordings\n\n");
        sb.append("| ID | Name | State | Duration |\n");
        sb.append("|----|------|-------|----------|\n");

        for (LiveRecordingInfo info : result.recordings()) {
            sb.append(String.format(
                    "| %d | %s | %s | %s |%n",
                    info.id(), info.name(), info.state(), info.duration()
            ));
        }

        return sb.toString();
    }

    private String start(String jmxUrl, String recordingName, Integer durationSeconds) throws Exception {
        String name = recordingName != null ? recordingName : "mcp-recording-" + System.currentTimeMillis();
        long duration = durationSeconds != null ? durationSeconds : 60;

        LiveRecordingStartResult result = appService.start(jmxUrl, name, duration);
        return String.format(
                "Started JFR recording **%s** (ID: %d) for %d seconds.",
                result.name(),
                result.recordingId(),
                result.durationSeconds()
        );
    }

    private String stop(String jmxUrl, Long recordingId) throws Exception {
        if (recordingId == null || recordingId < 0) {
            return "Error: recording_id is required for stop action.";
        }

        LiveRecordingStopResult result = appService.stop(jmxUrl, recordingId);
        return String.format("Stopped JFR recording ID %d.", result.recordingId());
    }

    private String dump(String jmxUrl, Long recordingId, String outputPath) throws Exception {
        if (recordingId == null || recordingId < 0) {
            return "Error: recording_id is required for dump action.";
        }
        String path = (outputPath != null && !outputPath.isBlank()) ? outputPath : "recording-" + recordingId + "-" + System.currentTimeMillis() + ".jfr";

        LiveRecordingDumpResult result = appService.dump(jmxUrl, recordingId, path);
        return String.format(
                "Dumped JFR recording ID %d to **%s**.",
                result.recordingId(),
                result.path()
        );
    }
}
