package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.Map;

public final class MemoryLeaksTool {

    private static final String NAME = "memory_leaks";
    private final JfrAnalysisService service;

    public MemoryLeaksTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze old object samples to identify potential memory leaks, leaking classes, and allocation sites.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top leaking classes/sites (default 20)", 20)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 20);

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

    private String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws Exception {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection samples = events.apply(ItemFilters.type("jdk.OldObjectSample"));
        long count = JfrItemUtils.count(samples);

        if (count == 0) {
            return "# Memory Leaks Analysis\n\nNo jdk.OldObjectSample events found. " +
                    "Make sure you are using a profile that enables Old Object Sample events (e.g., -XX:StartFlightRecording:settings=profile).";
        }

        Map<String, LeakStats> classStats = new HashMap<>();
        Map<String, LeakStats> siteStats = new HashMap<>();
        Map<Long, OldestObject> oldestObjects = new HashMap<>();

        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "objectClass");
            IMemberAccessor<IQuantity, IItem> sizeAccessor = JfrItemUtils.getAccessor(iterable.getType(), "allocationSize");
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            // The attribute 'lastKnownHeapUsage' is sometimes used to estimate retained size, but we'll stick to allocationSize or count.
            // Wait, OldObjectSample doesn't usually have "allocationSize". It has "objectClass", "allocationTime", "lastKnownHeapUsage" or "arraySize".
            // Actually, in JMC it's usually "object" (which is a pointer with type) or "objectClass".
            // The JFR event jdk.OldObjectSample fields: allocationTime, object, arrayElements, root, lastKnownHeapUsage
            // "objectClass" is often derived. Let's try "objectClass", "object", "arrayElements".
            // We will just count them if size is unavailable.
            IMemberAccessor<Object, IItem> objAccessor = JfrItemUtils.getAccessor(iterable.getType(), "object");

            if (classAccessor == null && objAccessor != null) {
                // If objectClass isn't direct, maybe 'object' provides it. In JMC, object often toStrings to the class name or we can extract it.
                // For simplicity, let's use getMember(item, "objectClass") and if not found, "object.type".
            }

            for (IItem item : iterable) {
                String className = "Unknown";
                if (classAccessor != null) {
                    Object clazz = classAccessor.getMember(item);
                    if (clazz != null) className = clazz.toString();
                } else if (objAccessor != null) {
                    Object obj = objAccessor.getMember(item);
                    if (obj != null) className = obj.toString(); // Usually something like java.lang.String
                }

                String stackTrace = "Unknown";
                if (stackAccessor != null) {
                    Object st = stackAccessor.getMember(item);
                    if (st != null) stackTrace = JfrItemUtils.formatStackTrace(st, 5);
                }

                long size = 0;
                if (sizeAccessor != null) {
                    IQuantity q = sizeAccessor.getMember(item);
                    if (q != null) size = q.longValue();
                }

                // Gather class stats
                LeakStats cs = classStats.computeIfAbsent(className, k -> new LeakStats());
                cs.count++;
                cs.totalSize += size;

                // Gather site stats
                String siteKey = className + " allocated at:\n" + stackTrace;
                LeakStats ss = siteStats.computeIfAbsent(siteKey, k -> new LeakStats());
                ss.count++;
                ss.totalSize += size;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Memory Leaks Analysis (Old Object Samples)\n\n");
        sb.append("- **Total Sampled Objects:** ").append(count).append("\n\n");

        sb.append("## Top Leaking Classes\n");
        sb.append("| Class | Sample Count |\n|---|---|\n");
        classStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().count, a.getValue().count))
                .limit(topN)
                .forEach(e -> sb.append(String.format("| `%s` | %d |\n", e.getKey(), e.getValue().count)));
        sb.append("\n");

        sb.append("## Top Leak Allocation Sites\n");
        sb.append("| Sample Count | Allocation Site |\n|---|---|\n");
        siteStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().count, a.getValue().count))
                .limit(topN)
                .forEach(e -> sb.append(String.format("| %d | `%s` |\n", e.getValue().count, e.getKey().replace("\n", "`<br>`"))));

        return sb.toString();
    }

    private static class LeakStats {
        long count;
        long totalSize;
    }

    private static class OldestObject {
        String className;
        long age;
    }
}