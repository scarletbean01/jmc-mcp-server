package io.github.deplague.jmcmcp.domain.model;

/**
 * Result of starting a new JFR recording on a remote JVM.
 */
public record LiveRecordingStartResult(long recordingId, String name, long durationSeconds) {
}
