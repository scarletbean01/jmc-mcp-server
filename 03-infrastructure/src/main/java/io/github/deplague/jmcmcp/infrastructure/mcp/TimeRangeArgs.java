package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.EpochThresholds;

import java.time.Instant;

/**
 * Validation utility for time range arguments in MCP tools.
 * Rejects small relative offsets or malformed timestamp strings with clear, actionable errors.
 */
public final class TimeRangeArgs {

    private TimeRangeArgs() {}

    /**
     * Validate an optional start or end time string.
     *
     * @param timeStr   the user-supplied timestamp string (may be null/blank)
     * @param paramName the parameter name ("start_time" or "end_time") for error reporting
     * @throws IllegalArgumentException if the timestamp is relative or invalid
     */
    public static void validate(String timeStr, String paramName) {
        if (timeStr == null || timeStr.isBlank()) {
            return;
        }

        String stripped = timeStr.strip();

        // 1. Try ISO-8601
        try {
            Instant.parse(stripped);
            return;
        } catch (Exception ignored) {
        }

        // 2. Try numeric epoch-ms or epoch-s
        try {
            long numericValue = Long.parseLong(stripped);
            if (numericValue < EpochThresholds.MIN_VALID_EPOCH_SECONDS) {
                throw new IllegalArgumentException(String.format(
                        "Invalid %s: '%s'. Relative time offsets are not supported; provide an absolute ISO-8601 timestamp (e.g., '2024-01-01T12:00:00Z') or epoch milliseconds.",
                        paramName, timeStr));
            }
            return;
        } catch (NumberFormatException ignored) {
        }

        throw new IllegalArgumentException(String.format(
                "Invalid %s: '%s'. Expected an absolute ISO-8601 timestamp (e.g., '2024-01-01T12:00:00Z') or epoch milliseconds.",
                paramName, timeStr));
    }

    /**
     * Validate both start and end time parameters.
     */
    public static void validateRange(String startTime, String endTime) {
        validate(startTime, "start_time");
        validate(endTime, "end_time");
    }
}
