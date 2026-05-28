package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.LeakSuspectEntry;
import io.github.deplague.jmcmcp.domain.model.OomProjection;
import io.github.deplague.jmcmcp.domain.model.PredictiveLeakResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for predictive leak analysis using linear regression on post-GC heap usage.
 */
public final class PredictiveLeakAnalysisService {

    public PredictiveLeakResult analyze(IItemCollection events, double rSquaredThreshold) {
        IItemCollection heapEvents = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        IItemCollection heapConfigEvents = events.apply(ItemFilters.type("jdk.GCHeapConfiguration"));
        IItemCollection oldObjectSamples = events.apply(ItemFilters.type("jdk.OldObjectSample"));

        if (!heapEvents.hasItems()) {
            return nullResult("No GC heap summary events found. Cannot perform leak analysis.");
        }

        List<DataPoint> postGcPoints = extractPostGcHeapPoints(heapEvents);
        if (postGcPoints.size() < 3) {
            return nullResult("Insufficient post-GC data points (" + postGcPoints.size() + "). Need at least 3 for regression.");
        }

        long maxHeapBytes = extractMaxHeapSize(heapConfigEvents, events);

        double[] regression = linearRegression(postGcPoints);
        double slope = regression[0];
        double intercept = regression[1];
        double rSquared = regression[2];

        long firstTimeMs = postGcPoints.get(0).timeMs;
        long lastTimeMs = postGcPoints.get(postGcPoints.size() - 1).timeMs;
        long recordingDurationMs = lastTimeMs - firstTimeMs;
        double currentHeapMB = postGcPoints.get(postGcPoints.size() - 1).heapUsedBytes / (1024.0 * 1024.0);
        double maxHeapMB = maxHeapBytes / (1024.0 * 1024.0);
        double heapUtilizationPct = maxHeapBytes > 0 ? (currentHeapMB / maxHeapMB) * 100 : 0;

        String verdict;
        if (rSquared >= rSquaredThreshold && slope > 0) {
            verdict = "⚠️ MEMORY LEAK DETECTED";
        } else if (rSquared >= 0.6 && slope > 0) {
            verdict = "⚡ POSSIBLE MEMORY LEAK (weak correlation)";
        } else if (slope <= 0) {
            verdict = "✅ NO MEMORY LEAK DETECTED";
        } else {
            verdict = "❓ INCONCLUSIVE";
        }

        double growthRateKBPerMin = slope / 1024.0 * 60000.0;

        OomProjection oomProjection = null;
        if (rSquared >= rSquaredThreshold && slope > 0 && maxHeapBytes > 0) {
            double timeToOomMs = (maxHeapBytes - intercept) / slope;
            long oomTimeMs = firstTimeMs + (long) timeToOomMs;
            double minutesToOom = (timeToOomMs - (lastTimeMs - firstTimeMs)) / 60000.0;
            oomProjection = new OomProjection(oomTimeMs, minutesToOom > 0 ? minutesToOom : null, minutesToOom <= 0);
        }

        List<LeakSuspectEntry> leakSuspects = extractLeakSuspects(oldObjectSamples);

        return new PredictiveLeakResult(
                verdict,
                growthRateKBPerMin,
                rSquared,
                slope,
                intercept,
                currentHeapMB,
                maxHeapBytes > 0 ? maxHeapMB : null,
                maxHeapBytes > 0 ? heapUtilizationPct : null,
                postGcPoints.size(),
                recordingDurationMs,
                oomProjection,
                leakSuspects
        );
    }

    private PredictiveLeakResult nullResult(String verdict) {
        return new PredictiveLeakResult(
                verdict, 0, 0, 0, 0, 0, null, null, 0, null, null, List.of()
        );
    }

    private List<DataPoint> extractPostGcHeapPoints(IItemCollection heapEvents) {
        List<DataPoint> points = new ArrayList<>();
        for (IItemIterable iterable : heapEvents) {
            IMemberAccessor<IQuantity, IItem> timeAccessor = JfrAttributes.START_TIME.getAccessor(iterable.getType());
            IMemberAccessor<IQuantity, IItem> usedAccessor = JfrItemUtils.getAccessor(iterable.getType(), "heapUsed");
            IMemberAccessor<Object, IItem> whenAccessor = JfrItemUtils.getAccessor(iterable.getType(), "when");

            if (timeAccessor != null && usedAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity timeQ = timeAccessor.getMember(item);
                    IQuantity usedQ = usedAccessor.getMember(item);
                    if (timeQ == null || usedQ == null) continue;

                    boolean isAfterGc = true;
                    if (whenAccessor != null) {
                        Object when = whenAccessor.getMember(item);
                        isAfterGc = when != null && when.toString().contains("After");
                    }

                    if (isAfterGc) {
                        long timeMs = timeQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
                        long usedBytes = usedQ.longValue();
                        points.add(new DataPoint(timeMs, usedBytes));
                    }
                }
            }
        }
        points.sort((a, b) -> Long.compare(a.timeMs, b.timeMs));
        return points;
    }

    private long extractMaxHeapSize(IItemCollection heapConfigEvents, IItemCollection allEvents) {
        for (IItemIterable iterable : heapConfigEvents) {
            IMemberAccessor<IQuantity, IItem> maxSizeAccessor = JfrItemUtils.getAccessor(iterable.getType(), "maxSize");
            if (maxSizeAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity maxSize = maxSizeAccessor.getMember(item);
                    if (maxSize != null) return maxSize.longValue();
                }
            }
        }

        IItemCollection propEvents = allEvents.apply(ItemFilters.type("jdk.InitialSystemProperty"));
        for (IItemIterable iterable : propEvents) {
            IMemberAccessor<Object, IItem> keyAccessor = JfrItemUtils.getAccessor(iterable.getType(), "key");
            IMemberAccessor<Object, IItem> valueAccessor = JfrItemUtils.getAccessor(iterable.getType(), "value");
            if (keyAccessor != null && valueAccessor != null) {
                for (IItem item : iterable) {
                    Object key = keyAccessor.getMember(item);
                    if (key != null && key.toString().equals("-XX:MaxHeapSize")) {
                        Object value = valueAccessor.getMember(item);
                        if (value != null) {
                            try {
                                return Long.parseLong(value.toString().trim());
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    private List<LeakSuspectEntry> extractLeakSuspects(IItemCollection oldObjectSamples) {
        Map<String, Long> classCounts = new HashMap<>();
        long totalCount = 0;

        for (IItemIterable iterable : oldObjectSamples) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "objectClass");
            if (classAccessor != null) {
                for (IItem item : iterable) {
                    Object clazz = classAccessor.getMember(item);
                    if (clazz != null) {
                        classCounts.merge(clazz.toString(), 1L, Long::sum);
                        totalCount++;
                    }
                }
            }
        }

        long finalTotal = totalCount;
        return classCounts.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(entry -> new LeakSuspectEntry(
                        entry.getKey(),
                        entry.getValue(),
                        finalTotal > 0 ? (entry.getValue() * 100.0 / finalTotal) : 0.0
                ))
                .toList();
    }

    private double[] linearRegression(List<DataPoint> points) {
        int n = points.size();
        if (n < 2) return new double[]{0, 0, 0};

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        for (DataPoint p : points) {
            sumX += p.timeMs;
            sumY += p.heapUsedBytes;
            sumXY += p.timeMs * p.heapUsedBytes;
            sumX2 += p.timeMs * p.timeMs;
        }

        double meanX = sumX / n;
        double meanY = sumY / n;

        double denominator = sumX2 - n * meanX * meanX;
        if (denominator == 0) return new double[]{0, meanY, 0};

        double slope = (sumXY - n * meanX * meanY) / denominator;
        double intercept = meanY - slope * meanX;

        double ssTot = 0, ssRes = 0;
        for (DataPoint p : points) {
            double predicted = slope * p.timeMs + intercept;
            ssRes += (p.heapUsedBytes - predicted) * (p.heapUsedBytes - predicted);
            ssTot += (p.heapUsedBytes - meanY) * (p.heapUsedBytes - meanY);
        }

        double rSquared = ssTot == 0 ? 0 : 1 - (ssRes / ssTot);
        return new double[]{slope, intercept, rSquared};
    }

    private record DataPoint(long timeMs, long heapUsedBytes) {
    }
}
