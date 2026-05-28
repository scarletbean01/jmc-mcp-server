package io.github.deplague.jmcmcp.domain.model;

/**
 * Information about a JFR event type.
 */
public record EventTypeInfo(String typeId, String displayName, long eventCount, int fieldCount) {
}
