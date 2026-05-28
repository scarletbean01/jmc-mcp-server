package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ProcessEntry;
import io.github.deplague.jmcmcp.domain.model.ProcessInfoResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for extracting OS and environment context from JFR recordings.
 */
@Slf4j
public final class ProcessInfoService {

    public ProcessInfoResult analyze(IItemCollection events) {
        Optional<String> osName = Optional.empty();
        Optional<String> osVersion = Optional.empty();
        Optional<String> osArch = Optional.empty();

        IItemCollection osInfo = events.apply(ItemFilters.type("jdk.OSInformation"));
        if (osInfo.hasItems()) {
            Optional<IItem> first = osInfo.stream().flatMap(IItemIterable::stream).findFirst();
            if (first.isPresent()) {
                IItem item = first.get();
                osName = JfrItemUtils.getMember(item, "osName").map(Object::toString);
                osVersion = JfrItemUtils.getMember(item, "osVersion").map(Object::toString);
                osArch = JfrItemUtils.getMember(item, "osArch").map(Object::toString);
            }
        }

        Optional<String> virtTech = Optional.empty();
        IItemCollection virtInfo = events.apply(ItemFilters.type("jdk.VirtualizationInformation"));
        if (virtInfo.hasItems()) {
            Optional<IItem> first = virtInfo.stream().flatMap(IItemIterable::stream).findFirst();
            if (first.isPresent()) {
                IItem item = first.get();
                virtTech = JfrItemUtils.getMember(item, "name").map(Object::toString)
                        .or(() -> JfrItemUtils.getMember(item, "virtualizationName").map(Object::toString));
            }
        }

        List<ProcessEntry> processes = new ArrayList<>();
        IItemCollection procs = events.apply(ItemFilters.type("jdk.SystemProcess"));
        if (procs.hasItems()) {
            for (IItemIterable iterable : procs) {
                IMemberAccessor<Object, IItem> pidAcc = JfrItemUtils.getAccessor(iterable.getType(), "pid");
                IMemberAccessor<Object, IItem> cmdAcc = JfrItemUtils.getAccessor(iterable.getType(), "commandLine");
                if (pidAcc != null && cmdAcc != null) {
                    for (IItem item : iterable) {
                        Object pid = pidAcc.getMember(item);
                        Object cmd = cmdAcc.getMember(item);
                        if (pid != null && cmd != null) {
                            processes.add(new ProcessEntry(pid.toString(), cmd.toString()));
                        }
                    }
                }
            }
        }

        processes = processes.stream().distinct()
                .sorted(Comparator.comparing(p -> {
                    try {
                        return Integer.parseInt(p.pid());
                    } catch (Exception e) {
                        return Integer.MAX_VALUE;
                    }
                }))
                .limit(50)
                .toList();

        return new ProcessInfoResult(osName, osVersion, osArch, virtTech, processes);
    }
}
