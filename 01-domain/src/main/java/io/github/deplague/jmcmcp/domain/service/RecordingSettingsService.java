package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.RecordingSetting;
import io.github.deplague.jmcmcp.domain.model.RecordingSettingsResult;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.List.of;
import static org.openjdk.jmc.common.item.ItemFilters.type;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class RecordingSettingsService {

    private final JfrAccessorRepository jfrAccessorRepository;

    public RecordingSettingsResult analyze(IItemCollection events) {
        IItemCollection settings = events.apply(type("jdk.ActiveSetting"));
        if (!settings.hasItems()) {
            return new RecordingSettingsResult(of());
        }

        List<RecordingSetting> entries = new ArrayList<>();
        for (IItemIterable iterable : settings) {
            for (IItem item : iterable) {
                Object name = jfrAccessorRepository.getMember(item, "name").orElse(null);
                Object settingName = jfrAccessorRepository.getMember(item, "settingName").orElse(null);
                Object settingValue = jfrAccessorRepository.getMember(item, "settingValue").orElse(null);
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
