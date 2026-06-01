package io.github.deplague.jmcmcp.domain.model;

/**
 * Result of stopping a JFR recording on a remote JVM.
 */
public record LiveRecordingStopResult(long recordingId) {
}
