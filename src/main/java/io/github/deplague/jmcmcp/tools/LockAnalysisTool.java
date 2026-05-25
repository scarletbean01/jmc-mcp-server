package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.util.HashMap;
import java.util.Map;

public final class LockAnalysisTool {

    private static final String NAME = "lock_analysis";
    private final JfrAnalysisService service;

    public LockAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze ThreadPark and Biased Lock Revocation events for advanced lock contention.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top sites to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }
                        String result = analyze(filePath, startTimeStr, endTimeStr, topN);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                }).build();
    }

    String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws Exception {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        StringBuilder sb = new StringBuilder();
        sb.append("# Advanced Lock Analysis\n\n");

        // 1. ThreadPark (LockSupport.park)
        IItemCollection parks = events.apply(ItemFilters.type("jdk.ThreadPark"));
        long parkCount = JfrItemUtils.count(parks);
        if (parkCount > 0) {
            IQuantity avgPark = JfrItemUtils.avgQuantity(parks, JfrAttributes.DURATION.getIdentifier());
            IQuantity maxPark = JfrItemUtils.maxQuantity(parks, JfrAttributes.DURATION.getIdentifier());
            
            sb.append("## Thread Park Summary (LockSupport.park)\n");
            sb.append("- **Total Park Events:** ").append(parkCount).append("\n");
            sb.append("- **Avg Park Duration:** ").append(JfrAnalysisService.display(avgPark)).append("\n");
            sb.append("- **Max Park Duration:** ").append(JfrAnalysisService.display(maxPark)).append("\n\n");

            Map<String, ParkStats> parkSites = new HashMap<>();
            for (IItemIterable iterable : parks) {
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
                if (stackAccessor != null && durationAccessor != null) {
                    for (IItem item : iterable) {
                        Object stack = stackAccessor.getMember(item);
                        IQuantity duration = durationAccessor.getMember(item);
                        if (stack != null && duration != null) {
                            String trace = JfrItemUtils.formatStackTrace(stack, 5);
                            ParkStats ps = parkSites.computeIfAbsent(trace, k -> new ParkStats());
                            ps.count++;
                            long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                            ps.totalNanos += nanos;
                            ps.maxNanos = Math.max(ps.maxNanos, nanos);
                        }
                    }
                }
            }

            sb.append("### Top Park Sites\n");
            sb.append("| Stack Trace | Count | Avg Duration | Max Duration |\n|---|---|---|---|\n");
            parkSites.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                    .limit(topN)
                    .forEach(e -> {
                        ParkStats s = e.getValue();
                        sb.append(String.format("| `%s` | %d | %s | %s |\n",
                                e.getKey().replace("\n", "`<br>`"),
                                s.count,
                                JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.totalNanos / s.count)),
                                JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.maxNanos))));
                    });
            sb.append("\n");
        } else {
            sb.append("No Thread Park events found.\n\n");
        }

        // 2. Biased Lock Revocations
        IItemCollection revocs = events.apply(ItemFilters.type("jdk.BiasedLockRevocation"));
        IItemCollection classRevocs = events.apply(ItemFilters.type("jdk.BiasedLockClassRevocation"));
        IItemCollection selfRevocs = events.apply(ItemFilters.type("jdk.BiasedLockSelfRevocation"));

        long rCount = JfrItemUtils.count(revocs);
        long crCount = JfrItemUtils.count(classRevocs);
        long srCount = JfrItemUtils.count(selfRevocs);

        if (rCount > 0 || crCount > 0 || srCount > 0) {
            sb.append("## Biased Lock Revocations\n");
            sb.append("- **Single Revocations:** ").append(rCount).append("\n");
            sb.append("- **Class/Bulk Revocations:** ").append(crCount).append("\n");
            sb.append("- **Self Revocations:** ").append(srCount).append("\n\n");

            Map<String, Long> classCounts = new HashMap<>();
            for (IItemCollection c : new IItemCollection[]{revocs, classRevocs, selfRevocs}) {
                for (IItemIterable iterable : c) {
                    IMemberAccessor<Object, IItem> lockClassAcc = JfrItemUtils.getAccessor(iterable.getType(), "lockClass");
                    if (lockClassAcc == null) lockClassAcc = JfrItemUtils.getAccessor(iterable.getType(), "revokedClass"); // for class revocations
                    if (lockClassAcc != null) {
                        for (IItem item : iterable) {
                            Object lockClass = lockClassAcc.getMember(item);
                            if (lockClass != null) {
                                classCounts.merge(lockClass.toString(), 1L, Long::sum);
                            }
                        }
                    }
                }
            }

            if (!classCounts.isEmpty()) {
                sb.append("### Revoked Lock Classes\n");
                sb.append("| Lock Class | Revocation Count |\n|---|---|\n");
                classCounts.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(topN)
                        .forEach(e -> sb.append(String.format("| `%s` | %d |\n", e.getKey(), e.getValue())));
            }
        } else {
            sb.append("No Biased Lock Revocation events found.\n");
        }

        sb.append("\n<agent_hint>Lock contention detected. Consider `correlate` to see if I/O is performed under contended locks (a critical anti-pattern), or `deadlock_detection` to check for deadlock cycles.</agent_hint>\n");

        return sb.toString();
    }

    private static class ParkStats {
        long count;
        long totalNanos;
        long maxNanos;
    }
}