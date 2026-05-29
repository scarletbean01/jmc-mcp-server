package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.LeakSuspectEntry;
import io.github.deplague.jmcmcp.domain.model.OomProjection;
import io.github.deplague.jmcmcp.domain.model.PredictiveLeakResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static java.lang.Long.compare;
import static java.lang.Long.parseLong;
import static java.util.List.of;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.START_TIME;

/**
 * Pure domain service for predictive leak analysis using linear regression on post-GC heap usage.
 */
@ApplicationScoped
public final class PredictiveLeakAnalysisService {

    public PredictiveLeakResult analyze(IItemCollection events, double rSquaredThreshold) {
        IItemCollection heapEvents = events.apply(type("jdk.GCHeapSummary"));
        IItemCollection heapConfigEvents = events.apply(type("jdk.GCHeapConfiguration"));
        IItemCollection oldObjectSamples = events.apply(type("jdk.OldObjectSample"));

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
                verdict, 0, 0, 0, 0, 0, null, null, 0, null, null, of()
        );
    }

    private List<DataPoint> extractPostGcHeapPoints(IItemCollection heapEvents) {
        List<DataPoint> points = new ArrayList<>();
        for (IItemIterable iterable : heapEvents) {
            IMemberAccessor<IQuantity, IItem> timeAccessor = START_TIME.getAccessor(iterable.getType());
            IType<?> type1 = iterable.getType();
            IMemberAccessor<IQuantity, IItem> usedAccessor = getAccessor(type1, "heapUsed");
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> whenAccessor = getAccessor(type, "when");

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
                        long timeMs = timeQ.clampedLongValueIn(EPOCH_MS);
                        long usedBytes = usedQ.longValue();
                        points.add(new DataPoint(timeMs, usedBytes));
                    }
                }
            }
        }
        points.sort((a, b) -> compare(a.timeMs, b.timeMs));
        return points;
    }

    private long extractMaxHeapSize(IItemCollection heapConfigEvents, IItemCollection allEvents) {
        for (IItemIterable iterable : heapConfigEvents) {
            IType<?> type = iterable.getType();
            IMemberAccessor<IQuantity, IItem> maxSizeAccessor = getAccessor(type, "maxSize");
            if (maxSizeAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity maxSize = maxSizeAccessor.getMember(item);
                    if (maxSize != null) return maxSize.longValue();
                }
            }
        }

        IItemCollection propEvents = allEvents.apply(type("jdk.InitialSystemProperty"));
        for (IItemIterable iterable : propEvents) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> keyAccessor = getAccessor(type1, "key");
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> valueAccessor = getAccessor(type, "value");
            if (keyAccessor != null && valueAccessor != null) {
                for (IItem item : iterable) {
                    Object key = keyAccessor.getMember(item);
                    if (key != null && key.toString().equals("-XX:MaxHeapSize")) {
                        Object value = valueAccessor.getMember(item);
                        if (value != null) {
                            try {
                                return parseLong(value.toString().trim());
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
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> classAccessor = getAccessor(type, "objectClass");
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
                .sorted((a, b) -> compare(b.getValue(), a.getValue()))
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
