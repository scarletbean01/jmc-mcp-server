package io.github.deplague.jmcmcp.domain.model;

/**
 * Statistics for a numeric event field.
 */
public record EventFieldStats(String field, String min, String avg, String max, String p95) {
}
