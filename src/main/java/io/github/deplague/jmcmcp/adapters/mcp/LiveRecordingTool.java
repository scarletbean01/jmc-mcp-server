package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.LiveRecordingApplicationService;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingDumpResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingInfo;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingListResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingStartResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingStopResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for managing live JFR recordings on a running JVM via JMX.
 * Delegates to the application layer and formats results as Markdown.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class LiveRecordingTool implements McpTool {

    private static final String NAME = "live_recording";

    private final LiveRecordingApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Connect to a running JVM via JMX and manage JFR recordings. "
                                                + "Supports listing existing recordings, starting a new recording, "
                                                + "stopping a recording, and dumping a recording to a file. "
                                                + "Requires the target JVM to have JFR enabled and JMX accessible."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jmx_url",
                                                        SchemaUtil.stringProp(
                                                                "JMX service URL, e.g. service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi"
                                                        ),
                                                        "action",
                                                        SchemaUtil.stringProp(
                                                                "Action to perform: list, start, stop, dump",
                                                                List.of("list", "start", "stop", "dump")
                                                        ),
                                                        "recording_name",
                                                        SchemaUtil.stringProp(
                                                                "Name for the recording (start action only)"
                                                        ),
                                                        "settings",
                                                        SchemaUtil.stringProp(
                                                                "Recording settings profile: default or profile (start action only)",
                                                                List.of("default", "profile")
                                                        ),
                                                        "duration_seconds",
                                                        SchemaUtil.intProp(
                                                                "Recording duration in seconds (start action only)",
                                                                60
                                                        ),
                                                        "recording_id",
                                                        SchemaUtil.intProp(
                                                                "Recording ID (stop and dump actions)",
                                                                null
                                                        ),
                                                        "output_path",
                                                        SchemaUtil.stringProp(
                                                                "Output file path for dump action"
                                                        )
                                                ),
                                                SchemaUtil.required("jmx_url", "action")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String jmxUrl = SchemaUtil.getString(
                                request.arguments(),
                                "jmx_url"
                        );
                        String action = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "action",
                                "list"
                        );

                        String result = execute(jmxUrl, action, request.arguments());
                        return CallToolResult.builder()
                                .addTextContent(result)
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

    private String execute(String jmxUrl, String action, Map<String, Object> args) throws Exception {
        return switch (action) {
            case "list" -> list(jmxUrl);
            case "start" -> start(jmxUrl, args);
            case "stop" -> stop(jmxUrl, args);
            case "dump" -> dump(jmxUrl, args);
            default -> "Unknown action: " + action + ". Supported: list, start, stop, dump.";
        };
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

    private String start(String jmxUrl, Map<String, Object> args) throws Exception {
        String name = SchemaUtil.getStringOrDefault(
                args,
                "recording_name",
                "mcp-recording-" + System.currentTimeMillis()
        );
        long durationSeconds = SchemaUtil.getLongOrDefault(
                args,
                "duration_seconds",
                60
        );

        LiveRecordingStartResult result = appService.start(jmxUrl, name, durationSeconds);
        return String.format(
                "Started JFR recording **%s** (ID: %d) for %d seconds.",
                result.name(),
                result.recordingId(),
                result.durationSeconds()
        );
    }

    private String stop(String jmxUrl, Map<String, Object> args) throws Exception {
        long recordingId = SchemaUtil.getLongOrDefault(args, "recording_id", -1);
        if (recordingId < 0) {
            return "Error: recording_id is required for stop action.";
        }

        LiveRecordingStopResult result = appService.stop(jmxUrl, recordingId);
        return String.format("Stopped JFR recording ID %d.", result.recordingId());
    }

    private String dump(String jmxUrl, Map<String, Object> args) throws Exception {
        long recordingId = SchemaUtil.getLongOrDefault(args, "recording_id", -1);
        String outputPath = SchemaUtil.getStringOrDefault(args, "output_path", "");
        if (recordingId < 0) {
            return "Error: recording_id is required for dump action.";
        }
        if (outputPath.isBlank()) {
            outputPath = "recording-" + recordingId + "-" + System.currentTimeMillis() + ".jfr";
        }

        LiveRecordingDumpResult result = appService.dump(jmxUrl, recordingId, outputPath);
        return String.format(
                "Dumped JFR recording ID %d to **%s**.",
                result.recordingId(),
                result.path()
        );
    }
}
