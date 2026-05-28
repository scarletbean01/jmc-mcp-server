package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ContainerConfig;
import io.github.deplague.jmcmcp.domain.model.ContainerCpuUsage;
import io.github.deplague.jmcmcp.domain.model.ContainerMemoryUsage;
import io.github.deplague.jmcmcp.domain.model.ContainerMetricsResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for container resource limits and usage analysis.
 */
@Slf4j
public final class ContainerMetricsService {

    public ContainerMetricsResult analyze(IItemCollection events) {
        IItemCollection configEvents = events.apply(ItemFilters.type("jdk.ContainerConfiguration"));
        IItemCollection cpuEvents = events.apply(ItemFilters.type("jdk.ContainerCPUUsage"));
        IItemCollection memEvents = events.apply(ItemFilters.type("jdk.ContainerMemoryUsage"));

        Optional<ContainerConfig> config = Optional.empty();
        if (configEvents.hasItems()) {
            Optional<IItem> itemOpt = configEvents.stream().flatMap(IItemIterable::stream).findFirst();
            if (itemOpt.isPresent()) {
                IItem item = itemOpt.get();
                config = Optional.of(new ContainerConfig(
                        JfrItemUtils.getMember(item, "cpuShares").map(Object::toString),
                        JfrItemUtils.getQuantity(item, "cpuPeriod")
                                .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)),
                        JfrItemUtils.getQuantity(item, "cpuQuota")
                                .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)),
                        JfrItemUtils.getQuantity(item, "memoryLimit")
                                .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)),
                        JfrItemUtils.getQuantity(item, "swapLimit")
                                .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)),
                        JfrItemUtils.getQuantity(item, "memorySoftLimit")
                                .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO))
                ));
            }
        }

        Optional<ContainerCpuUsage> cpu = Optional.empty();
        if (cpuEvents.hasItems()) {
            cpu = Optional.of(new ContainerCpuUsage(
                    Optional.ofNullable(JfrItemUtils.avgQuantity(cpuEvents, "cpuTime"))
                            .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)),
                    Optional.ofNullable(JfrItemUtils.maxQuantity(cpuEvents, "cpuTime"))
                            .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO))
            ));
        }

        Optional<ContainerMemoryUsage> mem = Optional.empty();
        if (memEvents.hasItems()) {
            mem = Optional.of(new ContainerMemoryUsage(
                    Optional.ofNullable(JfrItemUtils.avgQuantity(memEvents, "memoryUsage"))
                            .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)),
                    Optional.ofNullable(JfrItemUtils.maxQuantity(memEvents, "memoryUsage"))
                            .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)),
                    Optional.ofNullable(JfrItemUtils.avgQuantity(memEvents, "swapUsage"))
                            .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)),
                    Optional.ofNullable(JfrItemUtils.maxQuantity(memEvents, "swapUsage"))
                            .map(q -> q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO))
            ));
        }

        return new ContainerMetricsResult(config, cpu, mem);
    }
}
