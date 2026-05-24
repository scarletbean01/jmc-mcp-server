package io.github.deplague.jmcmcp.tools;

import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * MCP tool for managing live JFR recordings on a running JVM via JMX.
 *
 * Uses {@code jdk.jfr} MBeans exposed by the JVM's FlightRecorderMXBean.
 */
public final class LiveRecordingTool {

    private static final String NAME = "live_recording";

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Connect to a running JVM via JMX and manage JFR recordings. " +
                                "Supports listing existing recordings, starting a new recording, " +
                                "stopping a recording, and dumping a recording to a file. " +
                                "Requires the target JVM to have JFR enabled and JMX accessible.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jmx_url", SchemaUtil.stringProp(
                                                "JMX service URL, e.g. service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi"),
                                        "action", SchemaUtil.stringProp(
                                                "Action to perform: list, start, stop, dump",
                                                List.of("list", "start", "stop", "dump")),
                                        "recording_name", SchemaUtil.stringProp(
                                                "Name for the recording (start action only)"),
                                        "settings", SchemaUtil.stringProp(
                                                "Recording settings profile: default or profile (start action only)",
                                                List.of("default", "profile")),
                                        "duration_seconds", SchemaUtil.intProp(
                                                "Recording duration in seconds (start action only)", 60),
                                        "recording_id", SchemaUtil.intProp(
                                                "Recording ID (stop and dump actions)", null),
                                        "output_path", SchemaUtil.stringProp(
                                                "Output file path for dump action")
                                ),
                                SchemaUtil.required("jmx_url", "action")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String jmxUrl = getString(request.arguments(), "jmx_url");
                        String action = getStringOrDefault(request.arguments(), "action", "list");
                        String result = execute(jmxUrl, action, request.arguments());
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

    private String execute(String jmxUrl, String action, Map<String, Object> args) throws Exception {
        JMXServiceURL url = new JMXServiceURL(jmxUrl);
        try (JMXConnector connector = JMXConnectorFactory.connect(url)) {
            MBeanServerConnection conn = connector.getMBeanServerConnection();
            ObjectName flightRecorder = new ObjectName("jdk.management.jfr:type=FlightRecorder");

            return switch (action) {
                case "list" -> listRecordings(conn, flightRecorder);
                case "start" -> startRecording(conn, flightRecorder, args);
                case "stop" -> stopRecording(conn, flightRecorder, args);
                case "dump" -> dumpRecording(conn, flightRecorder, args);
                default -> "Unknown action: " + action + ". Supported: list, start, stop, dump.";
            };
        }
    }

    @SuppressWarnings("unchecked")
    private String listRecordings(MBeanServerConnection conn, ObjectName flightRecorder) throws Exception {
        List<Long> recordingIds = (List<Long>) conn.invoke(
                flightRecorder, "getRecordingIds", new Object[0], new String[0]);

        if (recordingIds.isEmpty()) {
            return "No active JFR recordings found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Active JFR Recordings\n\n");
        sb.append("| ID | Name | State | Duration |\n");
        sb.append("|----|------|-------|----------|\n");

        for (Long id : recordingIds) {
            Map<String, String> options = (Map<String, String>) conn.invoke(
                    flightRecorder, "getRecordingOptions",
                    new Object[]{id}, new String[]{"long"});

            String name = options.getOrDefault("name", "Unnamed");
            String state = options.getOrDefault("state", "UNKNOWN");
            String duration = options.getOrDefault("duration", "unlimited");

            sb.append(String.format("| %d | %s | %s | %s |%n", id, name, state, duration));
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private String startRecording(MBeanServerConnection conn, ObjectName flightRecorder,
                                   Map<String, Object> args) throws Exception {
        String name = getStringOrDefault(args, "recording_name", "mcp-recording-" + System.currentTimeMillis());
        String settings = getStringOrDefault(args, "settings", "profile");
        long durationSeconds = getLongOrDefault(args, "duration_seconds", 60);

        Map<String, String> recordingOptions = new java.util.HashMap<>();
        recordingOptions.put("name", name);
        recordingOptions.put("duration", durationSeconds + "s");
        recordingOptions.put("destination", "");

        long recordingId = (Long) conn.invoke(
                flightRecorder, "newRecording",
                new Object[0], new String[0]);

        conn.invoke(flightRecorder, "setRecordingOptions",
                new Object[]{recordingId, recordingOptions},
                new String[]{"long", "java.util.Map"});

        conn.invoke(flightRecorder, "startRecording",
                new Object[]{recordingId}, new String[]{"long"});

        return String.format("Started JFR recording **%s** (ID: %d) for %d seconds.", name, recordingId, durationSeconds);
    }

    private String stopRecording(MBeanServerConnection conn, ObjectName flightRecorder,
                                  Map<String, Object> args) throws Exception {
        long recordingId = getLongOrDefault(args, "recording_id", -1);
        if (recordingId < 0) {
            return "Error: recording_id is required for stop action.";
        }

        conn.invoke(flightRecorder, "stopRecording",
                new Object[]{recordingId}, new String[]{"long"});

        return String.format("Stopped JFR recording ID %d.", recordingId);
    }

    private String dumpRecording(MBeanServerConnection conn, ObjectName flightRecorder,
                                  Map<String, Object> args) throws Exception {
        long recordingId = getLongOrDefault(args, "recording_id", -1);
        String outputPath = getStringOrDefault(args, "output_path", "");
        if (recordingId < 0) {
            return "Error: recording_id is required for dump action.";
        }
        if (outputPath.isBlank()) {
            outputPath = "recording-" + recordingId + "-" + System.currentTimeMillis() + ".jfr";
        }

        Path path = Path.of(outputPath).toAbsolutePath();

        conn.invoke(flightRecorder, "copyTo",
                new Object[]{recordingId, path.toString()},
                new String[]{"long", "java.lang.String"});

        return String.format("Dumped JFR recording ID %d to **%s**.", recordingId, path);
    }

    @SuppressWarnings("unchecked")
    private static String getString(Map<String, Object> args, String key) {
        Object val = args.get(key);
        if (val == null) {
            throw new IllegalArgumentException("Missing required argument: " + key);
        }
        return val.toString();
    }

    private static String getStringOrDefault(Map<String, Object> args, String key, String defaultValue) {
        Object val = args.get(key);
        return val != null ? val.toString() : defaultValue;
    }

    private static long getLongOrDefault(Map<String, Object> args, String key, long defaultValue) {
        Object val = args.get(key);
        if (val instanceof Number n) {
            return n.longValue();
        }
        if (val instanceof String s) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
