package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of JVM flags analysis.
 */
public record JvmFlagsResult(List<JvmFlagEntry> flags) {

    public boolean hasFlags() {
        return flags != null && !flags.isEmpty();
    }
}
