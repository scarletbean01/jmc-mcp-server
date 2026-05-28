package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single active JFR recording on a remote JVM.
 */
public record LiveRecordingInfo(long id, String name, String state, String duration) {
}
