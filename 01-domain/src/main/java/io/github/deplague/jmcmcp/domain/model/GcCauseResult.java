package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of GC cause analysis.
 */
public record GcCauseResult(
        List<GcCauseEntry> overall,
        List<GcCauseEntry> youngGen,
        List<GcCauseEntry> oldGen,
        boolean hasData
) {
}
