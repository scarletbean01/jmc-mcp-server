package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ProcessEntry;
import io.github.deplague.jmcmcp.domain.model.ProcessInfoResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getMember;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparing;
import static java.util.Optional.empty;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for extracting OS and environment context from JFR recordings.
 */
@Slf4j
@ApplicationScoped
public final class ProcessInfoService {

    public ProcessInfoResult analyze(IItemCollection events) {
        Optional<String> osName = empty();
        Optional<String> osVersion = empty();
        Optional<String> osArch = empty();

        IItemCollection osInfo = events.apply(type("jdk.OSInformation"));
        if (osInfo.hasItems()) {
            Optional<IItem> first = osInfo.stream().flatMap(IItemIterable::stream).findFirst();
            if (first.isPresent()) {
                IItem item = first.get();
                osName = getMember(item, "osName").map(Object::toString);
                osVersion = getMember(item, "osVersion").map(Object::toString);
                osArch = getMember(item, "osArch").map(Object::toString);
            }
        }

        Optional<String> virtTech = empty();
        IItemCollection virtInfo = events.apply(type("jdk.VirtualizationInformation"));
        if (virtInfo.hasItems()) {
            Optional<IItem> first = virtInfo.stream().flatMap(IItemIterable::stream).findFirst();
            if (first.isPresent()) {
                IItem item = first.get();
                virtTech = getMember(item, "name").map(Object::toString)
                        .or(() -> getMember(item, "virtualizationName").map(Object::toString));
            }
        }

        List<ProcessEntry> processes = new ArrayList<>();
        IItemCollection procs = events.apply(type("jdk.SystemProcess"));
        if (procs.hasItems()) {
            for (IItemIterable iterable : procs) {
                IType<?> type1 = iterable.getType();
                IMemberAccessor<Object, IItem> pidAcc = getAccessor(type1, "pid");
                IType<?> type = iterable.getType();
                IMemberAccessor<Object, IItem> cmdAcc = getAccessor(type, "commandLine");
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
                .sorted(comparing(p -> {
                    try {
                        return parseInt(p.pid());
                    } catch (Exception e) {
                        return MAX_VALUE;
                    }
                }))
                .limit(50)
                .toList();

        return new ProcessInfoResult(osName, osVersion, osArch, virtTech, processes);
    }
}
