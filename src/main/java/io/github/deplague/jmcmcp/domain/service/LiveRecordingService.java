package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.LiveRecordingDumpResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingInfo;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingListResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingStartResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingStopResult;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pure domain service for managing live JFR recordings on a remote JVM via JMX.
 * Contains no MCP-specific or framework logic.
 */
public final class LiveRecordingService {

    private static final String FLIGHT_RECORDER_OBJECT_NAME = "jdk.management.jfr:type=FlightRecorder";

    public LiveRecordingListResult listRecordings(String jmxUrl) throws Exception {
        return withConnection(jmxUrl, (conn, flightRecorder) -> {
            List<Long> recordingIds = (List<Long>) conn.invoke(
                    flightRecorder, "getRecordingIds", new Object[0], new String[0]);

            if (recordingIds.isEmpty()) {
                return new LiveRecordingListResult(List.of());
            }

            List<LiveRecordingInfo> recordings = new ArrayList<>();
            for (Long id : recordingIds) {
                Map<String, String> options = (Map<String, String>) conn.invoke(
                        flightRecorder, "getRecordingOptions",
                        new Object[]{id}, new String[]{"long"});

                String name = options.getOrDefault("name", "Unnamed");
                String state = options.getOrDefault("state", "UNKNOWN");
                String duration = options.getOrDefault("duration", "unlimited");

                recordings.add(new LiveRecordingInfo(id, name, state, duration));
            }

            return new LiveRecordingListResult(recordings);
        });
    }

    public LiveRecordingStartResult startRecording(String jmxUrl, String name, long durationSeconds) throws Exception {
        return withConnection(jmxUrl, (conn, flightRecorder) -> {
            Map<String, String> recordingOptions = new HashMap<>();
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

            return new LiveRecordingStartResult(recordingId, name, durationSeconds);
        });
    }

    public LiveRecordingStopResult stopRecording(String jmxUrl, long recordingId) throws Exception {
        return withConnection(jmxUrl, (conn, flightRecorder) -> {
            conn.invoke(flightRecorder, "stopRecording",
                    new Object[]{recordingId}, new String[]{"long"});

            return new LiveRecordingStopResult(recordingId);
        });
    }

    public LiveRecordingDumpResult dumpRecording(String jmxUrl, long recordingId, String outputPath) throws Exception {
        return withConnection(jmxUrl, (conn, flightRecorder) -> {
            Path path = Path.of(outputPath).toAbsolutePath();

            conn.invoke(flightRecorder, "copyTo",
                    new Object[]{recordingId, path.toString()},
                    new String[]{"long", "java.lang.String"});

            return new LiveRecordingDumpResult(recordingId, path.toString());
        });
    }

    private <T> T withConnection(String jmxUrl, JmxAction<T> action) throws Exception {
        JMXServiceURL url = new JMXServiceURL(jmxUrl);
        try (JMXConnector connector = JMXConnectorFactory.connect(url)) {
            MBeanServerConnection conn = connector.getMBeanServerConnection();
            ObjectName flightRecorder = new ObjectName(FLIGHT_RECORDER_OBJECT_NAME);
            return action.execute(conn, flightRecorder);
        }
    }

    @FunctionalInterface
    private interface JmxAction<T> {
        T execute(MBeanServerConnection conn, ObjectName flightRecorder) throws Exception;
    }
}
