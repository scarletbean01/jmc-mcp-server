package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import java.io.IOException;
import java.util.*;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * MCP tool for detailed GC analysis (phases, heap trends, configuration).
 */
public final class GcDetailTool {

    private static final String NAME = "gc_detail";

    private final JfrAnalysisService service;

    public GcDetailTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
            .tool(
                McpSchema.Tool.builder()
                    .name(NAME)
                    .description(
                        "Detailed GC analysis: per-phase pause breakdowns, GC cause distribution, heap trends, and configuration."
                    )
                    .inputSchema(
                        SchemaUtil.objectSchema(
                            SchemaUtil.props(
                                "jfr_file_path",
                                SchemaUtil.jfrFileProp(),
                                "start_time",
                                SchemaUtil.startTimeProp(),
                                "end_time",
                                SchemaUtil.endTimeProp(),
                                "detail_level",
                                SchemaUtil.stringProp(
                                    "Detail level",
                                    List.of(
                                        "summary",
                                        "phases",
                                        "heap_trends",
                                        "all"
                                    )
                                )
                            ),
                            SchemaUtil.required("jfr_file_path")
                        )
                    )
                    .build()
            )
            .callHandler((exchange, request) -> {
                try {
                    String filePath = SchemaUtil.getString(
                        request.arguments(),
                        "jfr_file_path"
                    );
                    String startTimeStr = SchemaUtil.getStringOrDefault(
                        request.arguments(),
                        "start_time",
                        null
                    );
                    String endTimeStr = SchemaUtil.getStringOrDefault(
                        request.arguments(),
                        "end_time",
                        null
                    );
                    String detailLevel = SchemaUtil.getStringOrDefault(
                        request.arguments(),
                        "detail_level",
                        "all"
                    );

                    String cached = service.getCachedResult(
                        filePath,
                        NAME,
                        request.arguments()
                    );
                    if (cached != null) {
                        return CallToolResult.builder()
                            .addTextContent(cached)
                            .isError(false)
                            .build();
                    }

                    String result = analyze(
                        filePath,
                        startTimeStr,
                        endTimeStr,
                        detailLevel
                    );
                    service.cacheResult(
                        filePath,
                        NAME,
                        request.arguments(),
                        result
                    );
                    return CallToolResult.builder()
                        .addTextContent(result)
                        .isError(false)
                        .build();
                } catch (Exception e) {
                    return CallToolResult.builder()
                        .addTextContent("Error: " + e.getMessage())
                        .isError(true)
                        .build();
                }
            })
            .build();
    }

    private String analyze(
        String filePath,
        String startTimeStr,
        String endTimeStr,
        String detailLevel
    ) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(
            allEvents,
            startTimeStr,
            endTimeStr
        );

        StringBuilder sb = new StringBuilder();
        sb.append("# Detailed GC Analysis\n\n");

        boolean showAll = "all".equals(detailLevel);

        if (showAll || "summary".equals(detailLevel)) {
            appendConfiguration(events, sb);
            appendGenerationalSummary(events, sb);
        }

        if (showAll || "phases".equals(detailLevel)) {
            appendPhaseBreakdown(events, sb);
        }

        if (showAll || "heap_trends".equals(detailLevel)) {
            appendHeapTrends(events, sb);
        }

        return sb.toString();
    }

    private void appendConfiguration(IItemCollection events, StringBuilder sb) {
        sb.append("## GC Configuration\n");
        IItemCollection config = events.apply(
            ItemFilters.type("jdk.GCConfiguration")
        );
        IItemCollection heapConfig = events.apply(
            ItemFilters.type("jdk.GCHeapConfiguration")
        );
        IItemCollection survivorConfig = events.apply(
            ItemFilters.type("jdk.GCSurvivorConfiguration")
        );

        Optional<IItem> configItem = config.hasItems()
            ? Optional.of(config.iterator().next().iterator().next())
            : Optional.empty();
        Optional<IItem> heapItem = heapConfig.hasItems()
            ? Optional.of(heapConfig.iterator().next().iterator().next())
            : Optional.empty();
        Optional<IItem> survivorItem = survivorConfig.hasItems()
            ? Optional.of(survivorConfig.iterator().next().iterator().next())
            : Optional.empty();

        configItem.ifPresent(item -> {
            sb.append("- **Young Collector:** ")
                .append(
                    JfrItemUtils.getMember(item, "youngCollector").orElse("N/A")
                )
                .append("\n");
            sb.append("- **Old Collector:** ")
                .append(
                    JfrItemUtils.getMember(item, "oldCollector").orElse("N/A")
                )
                .append("\n");
            sb.append("- **Parallel GC Threads:** ")
                .append(
                    JfrItemUtils.getMember(item, "parallelGCThreads").orElse(
                        "N/A"
                    )
                )
                .append("\n");
            sb.append("- **Concurrent GC Threads:** ")
                .append(
                    JfrItemUtils.getMember(item, "concurrentGCThreads").orElse(
                        "N/A"
                    )
                )
                .append("\n");
        });

        heapItem.ifPresent(item -> {
            sb.append("- **Min Heap Size:** ")
                .append(
                    JfrAnalysisService.display(
                        JfrItemUtils.getQuantity(item, "minSize").orElse(null)
                    )
                )
                .append("\n");
            sb.append("- **Max Heap Size:** ")
                .append(
                    JfrAnalysisService.display(
                        JfrItemUtils.getQuantity(item, "maxSize").orElse(null)
                    )
                )
                .append("\n");
            sb.append("- **Initial Heap Size:** ")
                .append(
                    JfrAnalysisService.display(
                        JfrItemUtils.getQuantity(item, "initialSize").orElse(
                            null
                        )
                    )
                )
                .append("\n");
        });

        survivorItem.ifPresent(item ->
            sb
                .append("- **Max Tenuring Threshold:** ")
                .append(
                    JfrItemUtils.getMember(item, "maxTenuringThreshold").orElse(
                        "N/A"
                    )
                )
                .append("\n")
        );
        sb.append("\n");
    }

    private void appendGenerationalSummary(
        IItemCollection events,
        StringBuilder sb
    ) {
        sb.append("## Generational Summary\n");
        IItemCollection young = events.apply(
            ItemFilters.type("jdk.YoungGarbageCollection")
        );
        IItemCollection old = events.apply(
            ItemFilters.type("jdk.OldGarbageCollection")
        );

        sb.append("| Generation | Count | Total Duration | Avg Duration |\n");
        sb.append("|------------|-------|----------------|--------------|\n");

        appendGenRow(sb, "Young", young);
        appendGenRow(sb, "Old/Full", old);
        sb.append("\n");

        appendReferenceStatistics(events, sb);
        appendCauseDistribution(events, sb);
    }

    private void appendReferenceStatistics(
        IItemCollection events,
        StringBuilder sb
    ) {
        IItemCollection refStats = events.apply(
            ItemFilters.type("jdk.GCReferenceStatistics")
        );
        IItemCollection refPhases = events.apply(
            ItemFilters.type("jdk.GCPhasePause")
        );

        if (!refStats.hasItems() && !refPhases.hasItems()) return;

        sb.append("### GC Reference Statistics & Processing\n");
        sb.append(
            "| Reference Type / Phase | Count | Total Processing Time |\n"
        );
        sb.append(
            "|------------------------|-------|-----------------------|\n"
        );

        Map<String, Long> refCounts = new HashMap<>();
        for (IItemIterable iterable : refStats) {
            IMemberAccessor<String, IItem> typeAcc = JfrItemUtils.getAccessor(
                iterable.getType(),
                "type"
            );
            IMemberAccessor<Object, IItem> countAcc = JfrItemUtils.getAccessor(
                iterable.getType(),
                "count"
            );
            if (typeAcc != null && countAcc != null) {
                for (IItem item : iterable) {
                    String type = typeAcc.getMember(item);
                    Object c = countAcc.getMember(item);
                    if (type != null && c != null) {
                        refCounts.merge(
                            type,
                            JfrItemUtils.toLong(c),
                            Long::sum
                        );
                    }
                }
            }
        }

        Map<String, IQuantity> phaseTimes = new HashMap<>();
        for (IItemIterable iterable : refPhases) {
            IMemberAccessor<String, IItem> nameAcc = JfrItemUtils.getAccessor(
                iterable.getType(),
                "name"
            );
            IMemberAccessor<IQuantity, IItem> durationAcc =
                JfrAttributes.DURATION.getAccessor(iterable.getType());
            if (nameAcc != null && durationAcc != null) {
                for (IItem item : iterable) {
                    String name = nameAcc.getMember(item);
                    if (
                        name != null &&
                        (name.contains("Reference") || name.contains("Ref "))
                    ) {
                        IQuantity d = durationAcc.getMember(item);
                        if (d != null) {
                            phaseTimes.merge(name, d, IQuantity::add);
                        }
                    }
                }
            }
        }

        // Output counts first
        refCounts
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(e -> {
                String type = e.getKey();
                // Try to find a matching phase time
                String match = phaseTimes
                    .keySet()
                    .stream()
                    .filter(k ->
                        k
                            .toLowerCase()
                            .contains(
                                type.toLowerCase().replace("reference", "")
                            )
                    )
                    .findFirst()
                    .orElse(null);
                String timeStr =
                    match != null
                        ? JfrAnalysisService.display(phaseTimes.get(match))
                        : "N/A";
                if (match != null) phaseTimes.remove(match);
                sb.append("| ")
                    .append(type)
                    .append(" | ")
                    .append(e.getValue())
                    .append(" | ")
                    .append(timeStr)
                    .append(" |\n");
            });

        // Output remaining phases that didn't match counts
        phaseTimes
            .entrySet()
            .stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .forEach(e ->
                sb
                    .append("| ")
                    .append(e.getKey())
                    .append(" | N/A | ")
                    .append(JfrAnalysisService.display(e.getValue()))
                    .append(" |\n")
            );

        // Reference Processing Overhead
        IItemCollection allGcPauses = events.apply(
            ItemFilters.type("jdk.GCPhasePause")
        );
        IQuantity totalGcPause = JfrItemUtils.sumQuantity(
            allGcPauses,
            JfrAttributes.DURATION.getIdentifier()
        );
        IQuantity totalRefPause = null;
        for (var entry : phaseTimes.entrySet()) {
            // phaseTimes was already consumed above, recalculate
        }
        // Recalculate reference phase times for overhead
        Map<String, IQuantity> refPhaseTimes = new HashMap<>();
        for (IItemIterable iterable : refPhases) {
            IMemberAccessor<String, IItem> nameAcc = JfrItemUtils.getAccessor(
                iterable.getType(),
                "name"
            );
            IMemberAccessor<IQuantity, IItem> durationAcc =
                JfrAttributes.DURATION.getAccessor(iterable.getType());
            if (nameAcc != null && durationAcc != null) {
                for (IItem item : iterable) {
                    String name = nameAcc.getMember(item);
                    if (
                        name != null &&
                        (name.contains("Reference") || name.contains("Ref "))
                    ) {
                        IQuantity d = durationAcc.getMember(item);
                        if (d != null) {
                            refPhaseTimes.merge(name, d, IQuantity::add);
                        }
                    }
                }
            }
        }
        IQuantity totalRefPauseTime = null;
        for (var qty : refPhaseTimes.values()) {
            if (totalRefPauseTime == null) totalRefPauseTime = qty;
            else totalRefPauseTime = totalRefPauseTime.add(qty);
        }
        double refOverheadPct = 0;
        if (
            totalGcPause != null &&
            totalRefPauseTime != null &&
            totalGcPause.doubleValue() > 0
        ) {
            refOverheadPct =
                (totalRefPauseTime.doubleValue() / totalGcPause.doubleValue()) *
                100;
            sb.append(
                String.format(
                    "%n**Reference Processing Overhead:** %.1f%% of total GC pause time%n",
                    refOverheadPct
                )
            );
        }

        sb.append("\n");
    }

    private void appendCauseDistribution(
        IItemCollection events,
        StringBuilder sb
    ) {
        sb.append("### GC Cause Distribution\n");
        Map<String, Integer> causeCounts = new HashMap<>();
        IItemCollection gcs = events.apply(
            ItemFilters.type("jdk.GarbageCollection")
        );
        for (IItemIterable iterable : gcs) {
            IMemberAccessor<String, IItem> causeAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "cause");
            if (causeAccessor != null) {
                for (IItem item : iterable) {
                    String cause = causeAccessor.getMember(item);
                    if (cause != null) {
                        causeCounts.merge(cause, 1, Integer::sum);
                    }
                }
            }
        }

        if (causeCounts.isEmpty()) {
            sb.append("No GC cause data available.\n\n");
            return;
        }

        sb.append("| Cause | Count |\n");
        sb.append("|-------|-------|\n");
        causeCounts
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(e ->
                sb
                    .append("| ")
                    .append(e.getKey())
                    .append(" | ")
                    .append(e.getValue())
                    .append(" |\n")
            );
        sb.append("\n");
    }

    private void appendGenRow(
        StringBuilder sb,
        String name,
        IItemCollection collection
    ) {
        long count = JfrItemUtils.count(collection);
        IQuantity total = JfrItemUtils.sumQuantity(
            collection,
            JfrAttributes.DURATION.getIdentifier()
        );
        IQuantity avg = JfrItemUtils.avgQuantity(
            collection,
            JfrAttributes.DURATION.getIdentifier()
        );
        sb.append("| ")
            .append(name)
            .append(" | ")
            .append(count)
            .append(" | ")
            .append(JfrAnalysisService.display(total))
            .append(" | ")
            .append(JfrAnalysisService.display(avg))
            .append(" |\n");
    }

    private void appendPhaseBreakdown(
        IItemCollection events,
        StringBuilder sb
    ) {
        sb.append("## Pause Phase Breakdown\n");
        IItemCollection phases = events.apply(
            ItemFilters.type("jdk.GCPhasePause")
        );

        if (!phases.hasItems()) {
            sb.append("No GC pause phase events found.\n\n");
            return;
        }

        Map<String, List<IQuantity>> phaseDurations = new HashMap<>();
        for (IItemIterable iterable : phases) {
            IMemberAccessor<String, IItem> nameAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "name");
            IMemberAccessor<IQuantity, IItem> durationAccessor =
                JfrItemUtils.getAccessor(
                    iterable.getType(),
                    JfrAttributes.DURATION.getIdentifier()
                );

            if (nameAccessor != null && durationAccessor != null) {
                for (IItem item : iterable) {
                    String name = nameAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (name != null && duration != null) {
                        phaseDurations
                            .computeIfAbsent(name, k -> new ArrayList<>())
                            .add(duration);
                    }
                }
            }
        }

        sb.append("| Phase Name | Count | Avg | P95 | P99 | Max |\n");
        sb.append("|------------|-------|-----|-----|-----|-----|\n");

        // Pre-compute sums for sorting to avoid O(m log m · n) stream reductions in comparator
        Map<String, IQuantity> phaseSums = new HashMap<>();
        for (var entry : phaseDurations.entrySet()) {
            double sumNs = 0;
            for (IQuantity q : entry.getValue()) {
                sumNs += q.doubleValueIn(UnitLookup.NANOSECOND);
            }
            phaseSums.put(
                entry.getKey(),
                UnitLookup.NANOSECOND.quantity(sumNs)
            );
        }

        phaseDurations
            .entrySet()
            .stream()
            .sorted((a, b) ->
                phaseSums.get(b.getKey()).compareTo(phaseSums.get(a.getKey()))
            )
            .forEach(entry -> {
                List<IQuantity> durations = entry.getValue();
                Collections.sort(durations);
                double sumNs = 0;
                for (IQuantity q : durations) {
                    sumNs += q.doubleValueIn(UnitLookup.NANOSECOND);
                }
                IQuantity sum = UnitLookup.NANOSECOND.quantity(sumNs);
                IQuantity avg = UnitLookup.NANOSECOND.quantity(
                    sumNs / durations.size()
                );
                IQuantity p95 = durations.get(
                    (int) Math.max(0, Math.ceil(0.95 * durations.size()) - 1)
                );
                IQuantity p99 = durations.get(
                    (int) Math.max(0, Math.ceil(0.99 * durations.size()) - 1)
                );
                IQuantity max = durations.getLast();

                sb.append("| ")
                    .append(entry.getKey())
                    .append(" | ")
                    .append(durations.size())
                    .append(" | ")
                    .append(JfrAnalysisService.display(avg))
                    .append(" | ")
                    .append(JfrAnalysisService.display(p95))
                    .append(" | ")
                    .append(JfrAnalysisService.display(p99))
                    .append(" | ")
                    .append(JfrAnalysisService.display(max))
                    .append(" |\n");
            });
        sb.append("\n");
    }

    private void appendHeapTrends(IItemCollection events, StringBuilder sb) {
        sb.append("## Heap Trends\n");
        IItemCollection heapSummary = events.apply(
            ItemFilters.type("jdk.GCHeapSummary")
        );

        if (!heapSummary.hasItems()) {
            sb.append("No heap summary events found.\n\n");
            return;
        }

        IQuantity minUsed = JfrItemUtils.minQuantity(heapSummary, "heapUsed");
        IQuantity maxUsed = JfrItemUtils.maxQuantity(heapSummary, "heapUsed");
        IQuantity avgUsed = JfrItemUtils.avgQuantity(heapSummary, "heapUsed");
        IQuantity p95Used = JfrItemUtils.percentileQuantity(
            heapSummary,
            "heapUsed",
            95
        );

        sb.append("- **Min Heap Used:** ")
            .append(JfrAnalysisService.display(minUsed))
            .append("\n");
        sb.append("- **Max Heap Used:** ")
            .append(JfrAnalysisService.display(maxUsed))
            .append("\n");
        sb.append("- **Avg Heap Used:** ")
            .append(JfrAnalysisService.display(avgUsed))
            .append("\n");
        sb.append("- **P95 Heap Used:** ")
            .append(JfrAnalysisService.display(p95Used))
            .append("\n\n");

        sb.append("### GC Cycle Heap Usage\n");
        sb.append("| GC ID | Heap Used | Heap Size |\n");
        sb.append("|-------|-----------|-----------|\n");

        Map<Long, Map<String, IQuantity>> cycleMap = new TreeMap<>();
        for (IItemIterable iterable : heapSummary) {
            IMemberAccessor<Object, IItem> gcIdAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "gcId");
            IMemberAccessor<IQuantity, IItem> usedAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "heapUsed");
            IMemberAccessor<IQuantity, IItem> sizeAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "heapSize");
            IMemberAccessor<String, IItem> whenAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "when");

            if (gcIdAccessor != null) {
                for (IItem item : iterable) {
                    Object gcIdObj = gcIdAccessor.getMember(item);
                    if (gcIdObj != null) {
                        long gcId = JfrItemUtils.toLong(gcIdObj);
                        String when =
                            whenAccessor != null
                                ? whenAccessor.getMember(item)
                                : "After GC";
                        // We prefer "After GC" for trend analysis if multiple samples exist per GC
                        if (
                            "After GC".equals(when) ||
                            !cycleMap.containsKey(gcId)
                        ) {
                            Map<String, IQuantity> data = new HashMap<>();
                            if (usedAccessor != null) data.put(
                                "used",
                                usedAccessor.getMember(item)
                            );
                            if (sizeAccessor != null) data.put(
                                "size",
                                sizeAccessor.getMember(item)
                            );
                            cycleMap.put(gcId, data);
                        }
                    }
                }
            }
        }

        cycleMap
            .entrySet()
            .stream()
            .limit(20)
            .forEach(entry ->
                sb
                    .append("| ")
                    .append(entry.getKey())
                    .append(" | ")
                    .append(
                        JfrAnalysisService.display(entry.getValue().get("used"))
                    )
                    .append(" | ")
                    .append(
                        JfrAnalysisService.display(entry.getValue().get("size"))
                    )
                    .append(" |\n")
            );
        if (cycleMap.size() > 20) {
            sb.append("| ... | ... | ... |\n");
        }
        sb.append("\n");
    }
}
