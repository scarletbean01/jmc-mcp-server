package io.github.deplague.jmcmcp.domain.model;

import java.util.Map;

/**
 * A single search event result.
 */
public record SearchEventEntry(int index, Map<String, String> fields) {
}
