package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.*;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.StackTraceFormatCache;
import static java.lang.Math.min;
import static java.util.Comparator.comparingLong;
import static java.util.List.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for cross-dimensional correlation analysis.
 * Contains no MCP-specific or UI formatting logic.
 */
@ApplicationScoped
public final class CorrelateService {

    public CorrelateResult analyze(IItemCollection events, String dimension, int topN) {
        boolean showLockIo = "all".equals(dimension) || "lock_io_db".equals(dimension);
        boolean showCpuGc = "all".equals(dimension) || "cpu_gc".equals(dimension);

        List<CorrelateLockSite> lockSites = showLockIo ? extractLockSites(events, topN) : of();
        List<CorrelateIoSite> ioSites = showLockIo ? extractIoSites(events, topN) : of();
        List<CorrelateHotMethod> hotMethods = showLockIo ? extractHotMethods(events, topN) : of();
        CpuGcMetrics cpuGcMetrics = showCpuGc ? computeCpuGcMetrics(events) : null;

        return new CorrelateResult(lockSites, ioSites, hotMethods, cpuGcMetrics, showLockIo, showCpuGc, topN);
    }

    private List<CorrelateLockSite> extractLockSites(IItemCollection events, int topN) {
        Map<String, CorrelateLockSite> sites = new LinkedHashMap<>();
        StackTraceFormatCache stCache = new StackTraceFormatCache();
        for (String typeId : of("jdk.JavaMonitorEnter", "jdk.JavaMonitorWait")) {
            IItemCollection typeEvents = events.apply(type(typeId));
            for (IItemIterable iterable : typeEvents) {
                IType<?> type1 = iterable.getType();
                IMemberAccessor<Object, IItem> monitorAccessor = getAccessor(type1, "monitorClass");
                IMemberAccessor<IQuantity, IItem> durationAccessor = DURATION.getAccessor(iterable.getType());
                IType<?> type = iterable.getType();
                IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type, "stackTrace");
                if (monitorAccessor == null || durationAccessor == null) {
                    continue;
                }

                for (IItem item : iterable) {
                    Object monitorObj = monitorAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (monitorObj == null || duration == null) {
                        continue;
                    }

                    String monitorClass = monitorObj.toString();
                    String topFrame = "";
                    if (stackAccessor != null) {
                        Object st = stackAccessor.getMember(item);
                        if (st != null) {
                            topFrame = stCache.format(st, 1).replace("at ", "").trim();
                        }
                    }

                    String key = monitorClass + "@" + topFrame;
                    CorrelateLockSite existing = sites.get(key);
                    long newDuration = duration.clampedLongValueIn(MILLISECOND);
                    long newCount = 1;
                    if (existing != null) {
                        newDuration += existing.totalDurationMs();
                        newCount += existing.count();
                    }
                    sites.put(key, new CorrelateLockSite(monitorClass, topFrame, newDuration, newCount));
                }
            }
        }

        List<CorrelateLockSite> result = new ArrayList<>(sites.values());
        result.sort(comparingLong(CorrelateLockSite::totalDurationMs).reversed());
        return result.subList(0, min(topN, result.size()));
    }

    private List<CorrelateIoSite> extractIoSites(IItemCollection events, int topN) {
        Map<String, CorrelateIoSite> sites = new LinkedHashMap<>();
        StackTraceFormatCache stCache = new StackTraceFormatCache();
        for (String typeId : of("jdk.SocketRead", "jdk.SocketWrite", "jdk.FileRead", "jdk.FileWrite")) {
            IItemCollection typeEvents = events.apply(type(typeId));
            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<IQuantity, IItem> durationAccessor = DURATION.getAccessor(iterable.getType());
                IType<?> type2 = iterable.getType();
                IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type2, "stackTrace");
                if (durationAccessor == null) {
                    continue;
                }

                String targetAttr = typeId.contains("Socket") ? "host" : "path";
                String portAttr = typeId.contains("Socket") ? "port" : null;
                String bytesAttr = typeId.contains("Read") ? "bytesRead" : "bytesWritten";

                IType<?> type1 = iterable.getType();
                IMemberAccessor<Object, IItem> targetAccessor = getAccessor(type1, targetAttr);
                IMemberAccessor<IQuantity, IItem> portAccessor;
                if (portAttr != null) {
                    IType<?> type = iterable.getType();
                    portAccessor = getAccessor(type, portAttr);
                } else {
                    portAccessor = null;
                }
                IType<?> type = iterable.getType();
                IMemberAccessor<IQuantity, IItem> bytesAccessor = getAccessor(type, bytesAttr);

                for (IItem item : iterable) {
                    IQuantity duration = durationAccessor.getMember(item);
                    if (duration == null) {
                        continue;
                    }

                    String endpoint = "unknown";
                    if (targetAccessor != null) {
                        Object targetObj = targetAccessor.getMember(item);
                        if (targetObj != null) {
                            endpoint = targetObj.toString();
                            if (portAccessor != null) {
                                IQuantity portQ = portAccessor.getMember(item);
                                if (portQ != null) {
                                    endpoint += ":" + (int) portQ.longValue();
                                }
                            }
                        }
                    }

                    String topFrame = "";
                    if (stackAccessor != null) {
                        Object st = stackAccessor.getMember(item);
                        if (st != null) {
                            topFrame = stCache.format(st, 1).replace("at ", "").trim();
                        }
                    }

                    long bytes = 0;
                    if (bytesAccessor != null) {
                        IQuantity bytesQ = bytesAccessor.getMember(item);
                        if (bytesQ != null) {
                            bytes = bytesQ.longValue();
                        }
                    }

                    String key = typeId + "@" + endpoint;
                    CorrelateIoSite existing = sites.get(key);
                    long newDuration = duration.clampedLongValueIn(MILLISECOND);
                    long newCount = 1;
                    long newBytes = bytes;
                    if (existing != null) {
                        newDuration += existing.totalDurationMs();
                        newCount += existing.count();
                        newBytes += existing.totalBytes();
                    }
                    sites.put(key, new CorrelateIoSite(typeId, endpoint, topFrame, newDuration, newCount, newBytes));
                }
            }
        }

        List<CorrelateIoSite> result = new ArrayList<>(sites.values());
        result.sort(comparingLong(CorrelateIoSite::totalDurationMs).reversed());
        return result.subList(0, min(topN, result.size()));
    }

    private List<CorrelateHotMethod> extractHotMethods(IItemCollection events, int topN) {
        Map<String, CorrelateHotMethod> methods = new LinkedHashMap<>();
        StackTraceFormatCache stCache = new StackTraceFormatCache();
        IItemCollection samples = events.apply(type("jdk.ExecutionSample"));
        for (IItemIterable iterable : samples) {
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type, "stackTrace");
            if (stackAccessor == null) {
                continue;
            }

            for (IItem item : iterable) {
                Object st = stackAccessor.getMember(item);
                if (st == null) {
                    continue;
                }
                String topFrame = stCache.format(st, 1).replace("at ", "").trim();
                if (topFrame.isEmpty() || topFrame.startsWith("...") || topFrame.startsWith("No stack")) {
                    continue;
                }
                CorrelateHotMethod existing = methods.get(topFrame);
                long newCount = existing != null ? existing.sampleCount() + 1 : 1;
                methods.put(topFrame, new CorrelateHotMethod(topFrame, newCount));
            }
        }

        List<CorrelateHotMethod> result = new ArrayList<>(methods.values());
        result.sort(comparingLong(CorrelateHotMethod::sampleCount).reversed());
        return result.subList(0, min(topN, result.size()));
    }

    private CpuGcMetrics computeCpuGcMetrics(IItemCollection events) {
        IItemCollection cpuLoad = events.apply(type("jdk.CPULoad"));
        IItemCollection gcPauses = events.apply(type("jdk.GCPhasePause"));

        Double avgCpu = null;
        Double maxCpu = null;
        if (cpuLoad.hasItems()) {
            IQuantity avg = avgQuantity(cpuLoad, "machineTotal");
            IQuantity max = maxQuantity(cpuLoad, "machineTotal");
            if (avg != null) {
                avgCpu = avg.doubleValue() * 100;
            }
            if (max != null) {
                maxCpu = max.doubleValue() * 100;
            }
        }

        long gcCount = count(gcPauses);
        IQuantity totalGcPause = sumQuantity(gcPauses, DURATION.getIdentifier());
        String totalGcPauseStr = totalGcPause != null ? totalGcPause.displayUsing(AUTO) : "N/A";

        return new CpuGcMetrics(avgCpu, maxCpu, gcCount, totalGcPauseStr);
    }
}
