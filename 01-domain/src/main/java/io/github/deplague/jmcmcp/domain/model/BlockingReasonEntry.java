package io.github.deplague.jmcmcp.domain.model;

/**
 * A single blocking reason entry.
 */
public record BlockingReasonEntry(String category, String detail, String totalTime, long count) {
}
