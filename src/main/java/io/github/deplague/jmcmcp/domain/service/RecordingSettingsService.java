package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.RecordingSetting;
import io.github.deplague.jmcmcp.domain.model.RecordingSettingsResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for extracting JFR recording settings.
 */
@Slf4j
public final class RecordingSettingsService {

    public RecordingSettingsResult analyze(IItemCollection events) {
        IItemCollection settings = events.apply(ItemFilters.type("jdk.ActiveSetting"));
        if (!settings.hasItems()) {
            return new RecordingSettingsResult(List.of());
        }

        List<RecordingSetting> entries = new ArrayList<>();
        for (IItemIterable iterable : settings) {
            for (IItem item : iterable) {
                Object name = JfrItemUtils.getMember(item, "name").orElse(null);
                Object settingName = JfrItemUtils.getMember(item, "settingName").orElse(null);
                Object settingValue = JfrItemUtils.getMember(item, "settingValue").orElse(null);
                if (name != null) {
                    entries.add(new RecordingSetting(
                            name.toString(),
                            settingName != null ? settingName.toString() : "",
                            settingValue != null ? settingValue.toString() : ""
                    ));
                }
            }
        }

        entries.sort(Comparator.comparing(RecordingSetting::event));
        return new RecordingSettingsResult(entries);
    }
}
