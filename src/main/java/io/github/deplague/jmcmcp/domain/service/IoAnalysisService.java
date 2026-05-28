package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.IoAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.IoSummary;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.Aggregators;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for analyzing file and socket I/O events.
 */
@Slf4j
public final class IoAnalysisService {

    public IoAnalysisResult analyze(IItemCollection events, String ioType) {
        Optional<IoSummary> fileIo = Optional.empty();
        Optional<IoSummary> socketIo = Optional.empty();

        if ("all".equals(ioType) || "file".equals(ioType)) {
            var fileEvents = events.apply(ItemFilters.type("jdk.FileRead", "jdk.FileWrite"));
            if (fileEvents.hasItems()) {
                fileIo = Optional.of(new IoSummary(
                        displayOpt(fileEvents.getAggregate(Aggregators.count())),
                        displayOpt(fileEvents.getAggregate(Aggregators.sum(JfrAttributes.DURATION))),
                        displayOpt(fileEvents.getAggregate(Aggregators.avg(JfrAttributes.DURATION))),
                        displayOpt(JfrItemUtils.sumQuantity(
                                events.apply(ItemFilters.type("jdk.FileRead")), "bytesRead"
                        )),
                        displayOpt(JfrItemUtils.sumQuantity(
                                events.apply(ItemFilters.type("jdk.FileWrite")), "bytesWritten"
                        ))
                ));
            }
        }

        if ("all".equals(ioType) || "socket".equals(ioType)) {
            var socketEvents = events.apply(ItemFilters.type("jdk.SocketRead", "jdk.SocketWrite"));
            if (socketEvents.hasItems()) {
                socketIo = Optional.of(new IoSummary(
                        displayOpt(socketEvents.getAggregate(Aggregators.count())),
                        displayOpt(socketEvents.getAggregate(Aggregators.sum(JfrAttributes.DURATION))),
                        displayOpt(socketEvents.getAggregate(Aggregators.avg(JfrAttributes.DURATION))),
                        displayOpt(JfrItemUtils.sumQuantity(
                                events.apply(ItemFilters.type("jdk.SocketRead")), "bytesRead"
                        )),
                        displayOpt(JfrItemUtils.sumQuantity(
                                events.apply(ItemFilters.type("jdk.SocketWrite")), "bytesWritten"
                        ))
                ));
            }
        }

        return new IoAnalysisResult(fileIo, socketIo, fileIo.isPresent() || socketIo.isPresent());
    }

    private static Optional<String> displayOpt(IQuantity q) {
        return q != null ? Optional.of(q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)) : Optional.empty();
    }
}
