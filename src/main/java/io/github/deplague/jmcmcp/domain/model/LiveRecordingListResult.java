package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of listing active JFR recordings on a remote JVM.
 */
public record LiveRecordingListResult(List<LiveRecordingInfo> recordings) {

    public boolean isEmpty() {
        return recordings == null || recordings.isEmpty();
    }
}
