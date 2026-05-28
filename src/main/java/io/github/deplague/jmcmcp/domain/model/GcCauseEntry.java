package io.github.deplague.jmcmcp.domain.model;

/**
 * A single GC cause entry.
 */
public record GcCauseEntry(String cause, long count, String totalPause, String avgPause) {
}
