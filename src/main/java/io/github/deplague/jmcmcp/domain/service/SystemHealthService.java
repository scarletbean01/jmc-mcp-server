package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.CpuInfo;
import io.github.deplague.jmcmcp.domain.model.CpuLoad;
import io.github.deplague.jmcmcp.domain.model.PhysicalMemory;
import io.github.deplague.jmcmcp.domain.model.SystemContainerConfig;
import io.github.deplague.jmcmcp.domain.model.SystemHealthResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.Optional;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

/**
 * Pure domain service for system health analysis.
 */
public final class SystemHealthService {

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
        var cpuLoad = events.apply(ItemFilters.type("jdk.CPULoad"));
        if (!cpuLoad.hasItems()) {
            return Optional.empty();
        }

        IQuantity avgMachineTotal = JfrItemUtils.avgQuantity(cpuLoad, "machineTotal");
        IQuantity maxMachineTotal = JfrItemUtils.maxQuantity(cpuLoad, "machineTotal");
        IQuantity avgJvmUser = JfrItemUtils.avgQuantity(cpuLoad, "jvmUser");
        IQuantity avgJvmSystem = JfrItemUtils.avgQuantity(cpuLoad, "jvmSystem");

        return Optional.of(new CpuLoad(
                formatPercent(avgMachineTotal),
                formatPercent(maxMachineTotal),
                formatPercent(avgJvmUser),
                formatPercent(avgJvmSystem)
        ));
    }

    private Optional<PhysicalMemory> analyzePhysicalMemory(IItemCollection events) {
        var physicalMem = events.apply(ItemFilters.type("jdk.PhysicalMemory"));
        if (!physicalMem.hasItems()) {
            return Optional.empty();
        }

        IQuantity totalSize = JfrItemUtils.maxQuantity(physicalMem, "totalSize");
        IQuantity minUsed = JfrItemUtils.minQuantity(physicalMem, "usedSize");
        IQuantity maxUsed = JfrItemUtils.maxQuantity(physicalMem, "usedSize");
        IQuantity avgUsed = JfrItemUtils.avgQuantity(physicalMem, "usedSize");

        return Optional.of(new PhysicalMemory(
                display(totalSize),
                display(minUsed),
                display(maxUsed),
                display(avgUsed)
        ));
    }

    private Optional<CpuInfo> analyzeCpuInfo(IItemCollection events) {
        var cpuInfo = events.apply(ItemFilters.type("jdk.CPUInformation"));
        if (!cpuInfo.hasItems()) {
            return Optional.empty();
        }

        Optional<IItem> firstItem = cpuInfo.stream().flatMap(IItemIterable::stream).findFirst();
        String cpu = firstItem.flatMap(item -> JfrItemUtils.getMember(item, "cpu").map(Object::toString)).orElse(null);
        IQuantity cores = JfrItemUtils.maxQuantity(cpuInfo, "cores");
        IQuantity sockets = JfrItemUtils.maxQuantity(cpuInfo, "sockets");

        return Optional.of(new CpuInfo(
                cpu,
                cores != null ? (int) cores.doubleValue() : 0,
                sockets != null ? (int) sockets.doubleValue() : 0
        ));
    }

    private Optional<SystemContainerConfig> analyzeContainerConfig(IItemCollection events) {
        var containerConfig = events.apply(ItemFilters.type("jdk.ContainerConfiguration"));
        if (!containerConfig.hasItems()) {
            return Optional.empty();
        }

        Optional<IItem> itemOpt = containerConfig.stream().flatMap(IItemIterable::stream).findFirst();
        if (itemOpt.isEmpty()) {
            return Optional.empty();
        }

        IItem item = itemOpt.get();
        return Optional.of(new SystemContainerConfig(
                JfrItemUtils.getMember(item, "cpuShares").map(Object::toString).orElse("N/A"),
                display(JfrItemUtils.getQuantity(item, "cpuPeriod").orElse(null)),
                display(JfrItemUtils.getQuantity(item, "cpuQuota").orElse(null)),
                display(JfrItemUtils.getQuantity(item, "memoryLimit").orElse(null)),
                display(JfrItemUtils.getQuantity(item, "swapLimit").orElse(null))
        ));
    }

    private boolean isHighCpu(CpuLoad cpuLoad) {
        if (cpuLoad.avgMachineTotal() != null) {
            try {
                double val = Double.parseDouble(cpuLoad.avgMachineTotal().replace("%", ""));
                if (val > 80) {
                    return true;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        if (cpuLoad.maxMachineTotal() != null) {
            try {
                double val = Double.parseDouble(cpuLoad.maxMachineTotal().replace("%", ""));
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
        return String.format("%.2f%%", q.doubleValue() * 100);
    }

    private static String display(IQuantity q) {
        if (q == null) {
            return "N/A";
        }
        return q.displayUsing(IDisplayable.AUTO);
    }
}
