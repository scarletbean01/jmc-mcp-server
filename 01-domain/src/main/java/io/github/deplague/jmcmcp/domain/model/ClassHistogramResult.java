package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of class allocation histogram analysis.
 */
public record ClassHistogramResult(
        List<ClassAllocEntry> entries,
        boolean hasData
) {
}
