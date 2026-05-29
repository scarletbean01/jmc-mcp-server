package io.github.deplague.jmcmcp.application.service;

import io.modelcontextprotocol.spec.McpSchema;

import java.time.Instant;
import java.util.List;

/**
 * Helper for building {@link McpSchema.JsonSchema} input schemas without external JSON libraries.
 */
public final class FormatUtil {

    private FormatUtil() {
    }

    public static List<String> required(String... fields) {
        return List.of(fields);
    }

    public static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    public static String formatTime(long millis) {
        return Instant.ofEpochMilli(millis).toString().substring(11, 19);
    }

    public static String formatDuration(long millis) {
        if (millis < 1000) return millis + "ms";
        return (millis / 1000.0) + "s";
    }
}
