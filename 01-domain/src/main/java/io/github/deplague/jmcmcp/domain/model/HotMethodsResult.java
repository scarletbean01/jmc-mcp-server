package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of hot methods analysis containing ranked entries and the top method name.
 */
public record HotMethodsResult(List<HotMethodEntry> entries, String topMethod) {

    public boolean hasResults() {
        return entries != null && !entries.isEmpty();
    }
}
