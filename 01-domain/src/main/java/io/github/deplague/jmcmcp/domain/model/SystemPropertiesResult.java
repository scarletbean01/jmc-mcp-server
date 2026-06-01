package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of system properties analysis.
 */
public record SystemPropertiesResult(List<SystemPropertyEntry> entries) {

    public boolean hasProperties() {
        return entries != null && !entries.isEmpty();
    }
}
