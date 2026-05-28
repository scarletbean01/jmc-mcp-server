package io.github.deplague.jmcmcp.domain.model;

/**
 * Result of dumping a JFR recording from a remote JVM to a local file.
 */
public record LiveRecordingDumpResult(long recordingId, String path) {
}
