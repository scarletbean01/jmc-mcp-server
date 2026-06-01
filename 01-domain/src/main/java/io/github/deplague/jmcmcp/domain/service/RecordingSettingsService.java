package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.RecordingSetting;
import io.github.deplague.jmcmcp.domain.model.RecordingSettingsResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;

import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getMember;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for extracting JFR recording settings.
 */
@Slf4j
@ApplicationScoped
public final class RecordingSettingsService {

    public RecordingSettingsResult analyze(IItemCollection events) {
        IItemCollection settings = events.apply(type("jdk.ActiveSetting"));
        if (!settings.hasItems()) {
            return new RecordingSettingsResult(of());
        }

        List<RecordingSetting> entries = new ArrayList<>();
        for (IItemIterable iterable : settings) {
            for (IItem item : iterable) {
                Object name = getMember(item, "name").orElse(null);
                Object settingName = getMember(item, "settingName").orElse(null);
                Object settingValue = getMember(item, "settingValue").orElse(null);
                if (name != null) {
                    entries.add(new RecordingSetting(
                            name.toString(),
                            settingName != null ? settingName.toString() : "",
                            settingValue != null ? settingValue.toString() : ""
                    ));
                }
            }
        }

        entries.sort(comparing(RecordingSetting::event));
        return new RecordingSettingsResult(entries);
    }
}
