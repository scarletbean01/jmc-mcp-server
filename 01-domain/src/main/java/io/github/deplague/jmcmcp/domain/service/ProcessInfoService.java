package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ProcessEntry;
import io.github.deplague.jmcmcp.domain.model.ProcessInfoResult;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparing;
import static java.util.Optional.empty;
import static org.openjdk.jmc.common.item.ItemFilters.type;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class ProcessInfoService {

    private final JfrAccessorRepository jfrAccessorRepository;

    public ProcessInfoResult analyze(IItemCollection events) {
        Optional<String> osName = empty();
        Optional<String> osVersion = empty();
        Optional<String> osArch = empty();

        IItemCollection osInfo = events.apply(type("jdk.OSInformation"));
        if (osInfo.hasItems()) {
            Optional<IItem> first = osInfo.stream().flatMap(IItemIterable::stream).findFirst();
            if (first.isPresent()) {
                IItem item = first.get();
                osName = jfrAccessorRepository.getMember(item, "osName").map(Object::toString);
                osVersion = jfrAccessorRepository.getMember(item, "osVersion").map(Object::toString);
                osArch = jfrAccessorRepository.getMember(item, "osArch").map(Object::toString);
            }
        }

        Optional<String> virtTech = empty();
        IItemCollection virtInfo = events.apply(type("jdk.VirtualizationInformation"));
        if (virtInfo.hasItems()) {
            Optional<IItem> first = virtInfo.stream().flatMap(IItemIterable::stream).findFirst();
            if (first.isPresent()) {
                IItem item = first.get();
                virtTech = jfrAccessorRepository.getMember(item, "name").map(Object::toString)
                        .or(() -> jfrAccessorRepository.getMember(item, "virtualizationName").map(Object::toString));
            }
        }

        List<ProcessEntry> processes = new ArrayList<>();
        IItemCollection procs = events.apply(type("jdk.SystemProcess"));
        if (procs.hasItems()) {
            for (IItemIterable iterable : procs) {
                IType<?> type = iterable.getType();
                IMemberAccessor<Object, IItem> pidAcc = jfrAccessorRepository.getAccessor(type, "pid");
                IMemberAccessor<Object, IItem> cmdAcc = jfrAccessorRepository.getAccessor(type, "commandLine");
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
