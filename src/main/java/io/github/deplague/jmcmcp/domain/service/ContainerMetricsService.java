package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ContainerConfig;
import io.github.deplague.jmcmcp.domain.model.ContainerCpuUsage;
import io.github.deplague.jmcmcp.domain.model.ContainerMemoryUsage;
import io.github.deplague.jmcmcp.domain.model.ContainerMetricsResult;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.Optional;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getMember;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.batchStats;
import static java.util.Optional.*;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for container resource limits and usage analysis.
 */
@Slf4j
@ApplicationScoped
public final class ContainerMetricsService {

    public ContainerMetricsResult analyze(IItemCollection events) {
        IItemCollection configEvents = events.apply(type("jdk.ContainerConfiguration"));
        IItemCollection cpuEvents = events.apply(type("jdk.ContainerCPUUsage"));
        IItemCollection memEvents = events.apply(type("jdk.ContainerMemoryUsage"));

        Optional<ContainerConfig> config = empty();
        if (configEvents.hasItems()) {
            Optional<IItem> itemOpt = configEvents.stream().flatMap(IItemIterable::stream).findFirst();
            if (itemOpt.isPresent()) {
                IItem item = itemOpt.get();
                config = of(new ContainerConfig(
                        getMember(item, "cpuShares").map(Object::toString),
                        JfrAccessorRepository.<IQuantity>getQuantity(item, "cpuPeriod")
                                .map(q -> q.displayUsing(AUTO)),
                        JfrAccessorRepository.<IQuantity>getQuantity(item, "cpuQuota")
                                .map(q -> q.displayUsing(AUTO)),
                        JfrAccessorRepository.<IQuantity>getQuantity(item, "memoryLimit")
                                .map(q -> q.displayUsing(AUTO)),
                        JfrAccessorRepository.<IQuantity>getQuantity(item, "swapLimit")
                                .map(q -> q.displayUsing(AUTO)),
                        JfrAccessorRepository.<IQuantity>getQuantity(item, "memorySoftLimit")
                                .map(q -> q.displayUsing(AUTO))
                ));
            }
        }

        Optional<ContainerCpuUsage> cpu = empty();
        if (cpuEvents.hasItems()) {
            var stats = batchStats(cpuEvents, "cpuTime");
            cpu = of(new ContainerCpuUsage(
                    ofNullable(stats.get("avg"))
                            .map(q -> q.displayUsing(AUTO)),
                    ofNullable(stats.get("max"))
                            .map(q -> q.displayUsing(AUTO))
            ));
        }

        Optional<ContainerMemoryUsage> mem = empty();
        if (memEvents.hasItems()) {
            var memStats = batchStats(memEvents, "memoryUsage");
            var swapStats = batchStats(memEvents, "swapUsage");
            mem = of(new ContainerMemoryUsage(
                    ofNullable(memStats.get("avg"))
                            .map(q -> q.displayUsing(AUTO)),
                    ofNullable(memStats.get("max"))
                            .map(q -> q.displayUsing(AUTO)),
                    ofNullable(swapStats.get("avg"))
                            .map(q -> q.displayUsing(AUTO)),
                    ofNullable(swapStats.get("max"))
                            .map(q -> q.displayUsing(AUTO))
            ));
        }

        return new ContainerMetricsResult(config, cpu, mem);
    }
}
