package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PredictiveLeakAnalysisTool {

    private static final String NAME = "predictive_leak_analysis";

    private final JfrAnalysisService service;

    public PredictiveLeakAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Mathematically detect memory leaks using linear regression on post-GC heap usage. " +
                                "Projects time to OutOfMemoryError and cross-references Old Object Samples to identify leaking classes.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "r_squared_threshold", SchemaUtil.numberProp("Minimum R² correlation to confirm a leak (default 0.85)", 0.85),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> service.execute(NAME, request.arguments(), () -> {
                    String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                    String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                    String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                    double rSquaredThreshold = SchemaUtil.getNumberOrDefault(request.arguments(), "r_squared_threshold", 0.85);
                    return analyze(filePath, startTimeStr, endTimeStr, rSquaredThreshold);
                }))
                .build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr, double rSquaredThreshold) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection heapEvents = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        IItemCollection heapConfigEvents = events.apply(ItemFilters.type("jdk.GCHeapConfiguration"));
        IItemCollection oldObjectSamples = events.apply(ItemFilters.type("jdk.OldObjectSample"));

        if (!heapEvents.hasItems()) {
            return "# Predictive Leak Analysis\n\nNo GC heap summary events found. Cannot perform leak analysis.";
        }

        List<DataPoint> postGcPoints = extractPostGcHeapPoints(heapEvents);
        if (postGcPoints.size() < 3) {
            return "# Predictive Leak Analysis\n\nInsufficient post-GC data points (" + postGcPoints.size() + "). Need at least 3 for regression.";
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

        StringBuilder sb = new StringBuilder();
        sb.append("# Predictive Leak Analysis\n\n");

        if (rSquared >= rSquaredThreshold && slope > 0) {
            sb.append("## Verdict: ⚠️ MEMORY LEAK DETECTED\n\n");
        } else if (rSquared >= 0.6 && slope > 0) {
            sb.append("## Verdict: ⚡ POSSIBLE MEMORY LEAK (weak correlation)\n\n");
        } else if (slope <= 0) {
            sb.append("## Verdict: ✅ NO MEMORY LEAK DETECTED\n\n");
            sb.append("Post-GC heap usage is stable or declining. No leak pattern found.\n\n");
        } else {
            sb.append("## Verdict: ❓ INCONCLUSIVE\n\n");
            sb.append("Heap growth does not follow a linear pattern. Possible causes: bursty allocation, GC tuning needed.\n\n");
        }

        double growthRateKBPerMin = slope / 1024.0 * 60000.0;
        sb.append("## Leak Metrics\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append(String.format("| Post-GC Heap Growth Rate | %.2f KB/min |\n", growthRateKBPerMin));
        sb.append(String.format("| R² Correlation | %.4f |\n", rSquared));
        sb.append(String.format("| Current Post-GC Heap | %.1f MB |\n", currentHeapMB));
        if (maxHeapBytes > 0) {
            sb.append(String.format("| Max Heap Size | %.1f MB |\n", maxHeapMB));
            sb.append(String.format("| Heap Utilization | %.1f%% |\n", heapUtilizationPct));
        }
        sb.append(String.format("| Data Points | %d (post-GC samples) |\n", postGcPoints.size()));
        sb.append("\n");

        if (rSquared >= rSquaredThreshold && slope > 0 && maxHeapBytes > 0) {
            double timeToOomMs = (maxHeapBytes - intercept) / slope;
            long oomTimeMs = firstTimeMs + (long) timeToOomMs;
            double minutesToOom = (timeToOomMs - (lastTimeMs - firstTimeMs)) / 60000.0;

            sb.append("## OutOfMemoryError Projection\n\n");
            sb.append(String.format("| Metric | Value |\n"));
            sb.append(String.format("|--------|-------|\n"));
            sb.append(String.format("| Projected OOM Time | %s |\n", java.time.Instant.ofEpochMilli(oomTimeMs)));
            if (minutesToOom > 0) {
                sb.append(String.format("| Time to OOM from End of Recording | ~%.1f minutes |\n", minutesToOom));
            } else {
                sb.append("| Time to OOM from End of Recording | **ALREADY EXCEEDED** ⛔ |\n");
            }
            sb.append("\n");
        }

        sb.append("## Regression Details\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append(String.format("| Slope (bytes/ms) | %.2f |\n", slope));
        sb.append(String.format("| Intercept (bytes) | %.1f |\n", intercept));
        sb.append(String.format("| R² | %.4f |\n", rSquared));
        sb.append(String.format("| Recording Duration | %s |\n", SchemaUtil.formatDuration(recordingDurationMs)));
        sb.append("\n");

        if (oldObjectSamples.hasItems()) {
            appendLeakSuspects(sb, oldObjectSamples);
        } else {
            sb.append("## Leak Suspects\n\n");
            sb.append("No `jdk.OldObjectSample` events found. Enable Old Object Sampling with ");
            sb.append("`-XX:StartFlightRecording:settings=profile` for leak suspect identification.\n\n");
        }

        return sb.toString();
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
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
            }
        }
        return 0;
    }

    private void appendLeakSuspects(StringBuilder sb, IItemCollection oldObjectSamples) {
        Map<String, Long> classCounts = new HashMap<>();
        long[] totalCountRef = {0};

        for (IItemIterable iterable : oldObjectSamples) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "objectClass");
            if (classAccessor != null) {
                for (IItem item : iterable) {
                    Object clazz = classAccessor.getMember(item);
                    if (clazz != null) {
                        classCounts.merge(clazz.toString(), 1L, Long::sum);
                        totalCountRef[0]++;
                    }
                }
            }
        }

        long totalCount = totalCountRef[0];

        sb.append("## Leak Suspects (Old Object Samples)\n\n");
        sb.append(String.format("Total sampled objects: %d\n\n", totalCount));
        sb.append("| Class | Sample Count | % of Total |\n");
        sb.append("|-------|-------------|------------|\n");

        final long finalTotal = totalCount;
        classCounts.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(10)
                .forEach(entry -> {
                    double pct = finalTotal > 0 ? (entry.getValue() * 100.0 / finalTotal) : 0;
                    sb.append(String.format("| `%s` | %d | %.1f%% |\n", entry.getKey(), entry.getValue(), pct));
                });
        sb.append("\n");
    }

    private double[] linearRegression(List<DataPoint> points) {
        int n = points.size();
        if (n < 2) return new double[]{0, 0, 0};

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        for (DataPoint p : points) {
            double x = p.timeMs;
            double y = p.heapUsedBytes;
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double meanX = sumX / n;
        double meanY = sumY / n;

        double slope = (sumXY - n * meanX * meanY) / (sumX2 - n * meanX * meanX);
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

    private record DataPoint(long timeMs, long heapUsedBytes) {}
}