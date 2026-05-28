package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of recording settings analysis.
 */
public record RecordingSettingsResult(List<RecordingSetting> settings) {

    public boolean hasSettings() {
        return settings != null && !settings.isEmpty();
    }
}
