package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.IoAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.IoSummary;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.Optional;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.sumQuantity;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.Aggregators.*;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for analyzing file and socket I/O events.
 */
@Slf4j
@ApplicationScoped
public final class IoAnalysisService {

    public IoAnalysisResult analyze(IItemCollection events, String ioType) {
        Optional<IoSummary> fileIo = empty();
        Optional<IoSummary> socketIo = empty();

        if ("all".equals(ioType) || "file".equals(ioType)) {
            var fileEvents = events.apply(type("jdk.FileRead", "jdk.FileWrite"));
            if (fileEvents.hasItems()) {
                IItemCollection items = events.apply(type("jdk.FileWrite"));
                IItemCollection items1 = events.apply(type("jdk.FileRead"));
                fileIo = of(new IoSummary(
                        displayOpt(fileEvents.getAggregate(count())),
                        displayOpt(fileEvents.getAggregate(sum(DURATION))),
                        displayOpt(fileEvents.getAggregate(avg(DURATION))),
                        displayOpt(sumQuantity(items1, "bytesRead")),
                        displayOpt(sumQuantity(items, "bytesWritten"))
                ));
            }
        }

        if ("all".equals(ioType) || "socket".equals(ioType)) {
            var socketEvents = events.apply(type("jdk.SocketRead", "jdk.SocketWrite"));
            if (socketEvents.hasItems()) {
                IItemCollection items = events.apply(type("jdk.SocketWrite"));
                IItemCollection items1 = events.apply(type("jdk.SocketRead"));
                socketIo = of(new IoSummary(
                        displayOpt(socketEvents.getAggregate(count())),
                        displayOpt(socketEvents.getAggregate(sum(DURATION))),
                        displayOpt(socketEvents.getAggregate(avg(DURATION))),
                        displayOpt(sumQuantity(items1, "bytesRead")),
                        displayOpt(sumQuantity(items, "bytesWritten"))
                ));
            }
        }

        return new IoAnalysisResult(fileIo, socketIo, fileIo.isPresent() || socketIo.isPresent());
    }

    private static Optional<String> displayOpt(IQuantity q) {
        return q != null ? of(q.displayUsing(AUTO)) : empty();
    }
}
