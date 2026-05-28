package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.CorrelateHotMethod;
import io.github.deplague.jmcmcp.domain.model.CorrelateIoSite;
import io.github.deplague.jmcmcp.domain.model.CorrelateLockSite;
import io.github.deplague.jmcmcp.domain.model.CorrelateResult;
import io.github.deplague.jmcmcp.domain.model.CpuGcMetrics;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for cross-dimensional correlation analysis.
 * Contains no MCP-specific or UI formatting logic.
 */
public final class CorrelateService {

    public CorrelateResult analyze(IItemCollection events, String dimension, int topN) {
        boolean showLockIo = "all".equals(dimension) || "lock_io_db".equals(dimension);
        boolean showCpuGc = "all".equals(dimension) || "cpu_gc".equals(dimension);

        List<CorrelateLockSite> lockSites = showLockIo ? extractLockSites(events, topN) : List.of();
        List<CorrelateIoSite> ioSites = showLockIo ? extractIoSites(events, topN) : List.of();
        List<CorrelateHotMethod> hotMethods = showLockIo ? extractHotMethods(events, topN) : List.of();
        CpuGcMetrics cpuGcMetrics = showCpuGc ? computeCpuGcMetrics(events) : null;

        return new CorrelateResult(lockSites, ioSites, hotMethods, cpuGcMetrics, showLockIo, showCpuGc, topN);
    }

    private List<CorrelateLockSite> extractLockSites(IItemCollection events, int topN) {
        Map<String, CorrelateLockSite> sites = new LinkedHashMap<>();
        JfrItemUtils.StackTraceFormatCache stCache = JfrItemUtils.newStackTraceFormatCache();
        for (String typeId : List.of("jdk.JavaMonitorEnter", "jdk.JavaMonitorWait")) {
            IItemCollection typeEvents = events.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<Object, IItem> monitorAccessor = JfrItemUtils.getAccessor(iterable.getType(), "monitorClass");
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
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
                    long newDuration = duration.clampedLongValueIn(UnitLookup.MILLISECOND);
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
        result.sort(Comparator.comparingLong(CorrelateLockSite::totalDurationMs).reversed());
        return result.subList(0, Math.min(topN, result.size()));
    }

    private List<CorrelateIoSite> extractIoSites(IItemCollection events, int topN) {
        Map<String, CorrelateIoSite> sites = new LinkedHashMap<>();
        JfrItemUtils.StackTraceFormatCache stCache = JfrItemUtils.newStackTraceFormatCache();
        for (String typeId : List.of("jdk.SocketRead", "jdk.SocketWrite", "jdk.FileRead", "jdk.FileWrite")) {
            IItemCollection typeEvents = events.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                if (durationAccessor == null) {
                    continue;
                }

                String targetAttr = typeId.contains("Socket") ? "host" : "path";
                String portAttr = typeId.contains("Socket") ? "port" : null;
                String bytesAttr = typeId.contains("Read") ? "bytesRead" : "bytesWritten";

                IMemberAccessor<Object, IItem> targetAccessor = JfrItemUtils.getAccessor(iterable.getType(), targetAttr);
                IMemberAccessor<IQuantity, IItem> portAccessor = portAttr != null ? JfrItemUtils.getAccessor(iterable.getType(), portAttr) : null;
                IMemberAccessor<IQuantity, IItem> bytesAccessor = JfrItemUtils.getAccessor(iterable.getType(), bytesAttr);

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
                    long newDuration = duration.clampedLongValueIn(UnitLookup.MILLISECOND);
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
        result.sort(Comparator.comparingLong(CorrelateIoSite::totalDurationMs).reversed());
        return result.subList(0, Math.min(topN, result.size()));
    }

    private List<CorrelateHotMethod> extractHotMethods(IItemCollection events, int topN) {
        Map<String, CorrelateHotMethod> methods = new LinkedHashMap<>();
        JfrItemUtils.StackTraceFormatCache stCache = JfrItemUtils.newStackTraceFormatCache();
        IItemCollection samples = events.apply(ItemFilters.type("jdk.ExecutionSample"));
        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
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
        result.sort(Comparator.comparingLong(CorrelateHotMethod::sampleCount).reversed());
        return result.subList(0, Math.min(topN, result.size()));
    }

    private CpuGcMetrics computeCpuGcMetrics(IItemCollection events) {
        IItemCollection cpuLoad = events.apply(ItemFilters.type("jdk.CPULoad"));
        IItemCollection gcPauses = events.apply(ItemFilters.type("jdk.GCPhasePause"));

        Double avgCpu = null;
        Double maxCpu = null;
        if (cpuLoad.hasItems()) {
            IQuantity avg = JfrItemUtils.avgQuantity(cpuLoad, "machineTotal");
            IQuantity max = JfrItemUtils.maxQuantity(cpuLoad, "machineTotal");
            if (avg != null) {
                avgCpu = avg.doubleValue() * 100;
            }
            if (max != null) {
                maxCpu = max.doubleValue() * 100;
            }
        }

        long gcCount = JfrItemUtils.count(gcPauses);
        IQuantity totalGcPause = JfrItemUtils.sumQuantity(gcPauses, JfrAttributes.DURATION.getIdentifier());
        String totalGcPauseStr = totalGcPause != null ? totalGcPause.displayUsing(IDisplayable.AUTO) : "N/A";

        return new CpuGcMetrics(avgCpu, maxCpu, gcCount, totalGcPauseStr);
    }
}
