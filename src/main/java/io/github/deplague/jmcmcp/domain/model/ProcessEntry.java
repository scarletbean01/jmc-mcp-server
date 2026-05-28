package io.github.deplague.jmcmcp.domain.model;

/**
 * A single running process extracted from a JFR recording.
 */
public record ProcessEntry(String pid, String command) {
}
