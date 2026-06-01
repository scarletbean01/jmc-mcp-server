package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * A known JDK bug signature.
 */
public record BugSignature(
        String id,
        String pattern,
        List<String> affectedVersions,
        String fixedIn,
        String workaround,
        String severity,
        String category
) {
}
