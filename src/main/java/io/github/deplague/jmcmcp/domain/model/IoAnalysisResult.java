package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * Result of I/O analysis.
 */
public record IoAnalysisResult(
        Optional<IoSummary> fileIo,
        Optional<IoSummary> socketIo,
        boolean hasData
) {
}
