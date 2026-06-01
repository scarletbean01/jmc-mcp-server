package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.*;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import io.github.deplague.jmcmcp.domain.port.JfrQuantityAggregator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.Optional;

import static java.lang.Double.parseDouble;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class SystemHealthService {

    private final JfrAccessorRepository jfrAccessorRepository;
    private final JfrQuantityAggregator jfrQuantityAggregator;

    public SystemHealthResult analyze(IItemCollection events) {
        Optional<CpuLoad> cpuLoad = analyzeCpuLoad(events);
        Optional<PhysicalMemory> physicalMemory = analyzePhysicalMemory(events);
        Optional<CpuInfo> cpuInfo = analyzeCpuInfo(events);
        Optional<SystemContainerConfig> containerConfig = analyzeContainerConfig(events);

        boolean hasData = cpuLoad.isPresent() || physicalMemory.isPresent()
                || cpuInfo.isPresent() || containerConfig.isPresent();
        boolean highCpuDetected = cpuLoad.map(this::isHighCpu).orElse(false);

        return new SystemHealthResult(cpuLoad, physicalMemory, cpuInfo, containerConfig, hasData, highCpuDetected);
    }

    private Optional<CpuLoad> analyzeCpuLoad(IItemCollection events) {
        var cpuLoad = events.apply(type("jdk.CPULoad"));
        if (!cpuLoad.hasItems()) {
            return empty();
        }

        var machineStats = jfrQuantityAggregator.batchStats(cpuLoad, "machineTotal");
        var userStats = jfrQuantityAggregator.batchStats(cpuLoad, "jvmUser");
        var sysStats = jfrQuantityAggregator.batchStats(cpuLoad, "jvmSystem");

        return of(new CpuLoad(
                formatPercent(machineStats.get("avg")),
                formatPercent(machineStats.get("max")),
                formatPercent(userStats.get("avg")),
                formatPercent(sysStats.get("avg"))
        ));
    }

    private Optional<PhysicalMemory> analyzePhysicalMemory(IItemCollection events) {
        var physicalMem = events.apply(type("jdk.PhysicalMemory"));
        if (!physicalMem.hasItems()) {
            return empty();
        }

        IQuantity totalSize = jfrQuantityAggregator.maxQuantity(physicalMem, "totalSize");
        var usedStats = jfrQuantityAggregator.batchStats(physicalMem, "usedSize");

        return of(new PhysicalMemory(
                display(totalSize),
                display(usedStats.get("min")),
                display(usedStats.get("max")),
                display(usedStats.get("avg"))
        ));
    }

    private Optional<CpuInfo> analyzeCpuInfo(IItemCollection events) {
        var cpuInfo = events.apply(type("jdk.CPUInformation"));
        if (!cpuInfo.hasItems()) {
            return empty();
        }

        Optional<IItem> firstItem = cpuInfo.stream().flatMap(IItemIterable::stream).findFirst();
        String cpu = firstItem.flatMap(item -> jfrAccessorRepository.getMember(item, "cpu").map(Object::toString)).orElse(null);
        IQuantity cores = jfrQuantityAggregator.maxQuantity(cpuInfo, "cores");
        IQuantity sockets = jfrQuantityAggregator.maxQuantity(cpuInfo, "sockets");

        return of(new CpuInfo(
                cpu,
                cores != null ? (int) cores.doubleValue() : 0,
                sockets != null ? (int) sockets.doubleValue() : 0
        ));
    }

    private Optional<SystemContainerConfig> analyzeContainerConfig(IItemCollection events) {
        var containerConfig = events.apply(type("jdk.ContainerConfiguration"));
        if (!containerConfig.hasItems()) {
            return empty();
        }

        Optional<IItem> itemOpt = containerConfig.stream().flatMap(IItemIterable::stream).findFirst();
        if (itemOpt.isEmpty()) {
            return empty();
        }

        IItem item = itemOpt.get();
        return of(new SystemContainerConfig(
                jfrAccessorRepository.getMember(item, "cpuShares").map(Object::toString).orElse("N/A"),
                display(jfrAccessorRepository.<IQuantity>getQuantity(item, "cpuPeriod").orElse(null)),
                display(jfrAccessorRepository.<IQuantity>getQuantity(item, "cpuQuota").orElse(null)),
                display(jfrAccessorRepository.<IQuantity>getQuantity(item, "memoryLimit").orElse(null)),
                display(jfrAccessorRepository.<IQuantity>getQuantity(item, "swapLimit").orElse(null))
        ));
    }

    private boolean isHighCpu(CpuLoad cpuLoad) {
        if (cpuLoad.avgMachineTotal() != null) {
            try {
                double val = parseDouble(cpuLoad.avgMachineTotal().replace("%", ""));
                if (val > 80) {
                    return true;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        if (cpuLoad.maxMachineTotal() != null) {
            try {
                double val = parseDouble(cpuLoad.maxMachineTotal().replace("%", ""));
                if (val > 90) {
                    return true;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return false;
    }

    private static String formatPercent(IQuantity q) {
        if (q == null) {
            return null;
        }
        return format("%.2f%%", q.doubleValue() * 100);
    }

    private static String display(IQuantity q) {
        if (q == null) {
            return "N/A";
        }
        return q.displayUsing(AUTO);
    }
}
