package io.github.deplague.jmcmcp.domain.model;

/**
 * A single active JFR recording setting.
 */
public record RecordingSetting(String event, String settingName, String settingValue) {
}
