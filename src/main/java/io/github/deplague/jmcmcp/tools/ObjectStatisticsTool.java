package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

import java.io.IOException;
import java.util.*;

/**
 * MCP tool for analyzing object statistics (heap occupancy by class).
 */
public final class ObjectStatisticsTool {

    private static final String NAME = "object_statistics";

    private final JfrAnalysisService service;

    public ObjectStatisticsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze object statistics in a JFR recording. " +
                                "Reports heap occupancy by class from jdk.ObjectCount events (usually captured during Full GC).")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "top_n", SchemaUtil.intProp("Number of top classes to return (default 20)", 20)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        int topN = getIntOrDefault(request.arguments(), "top_n", 20);
                        String result = analyze(filePath, topN);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String analyze(String filePath, int topN) throws IOException {
        IItemCollection events = service.loadRecording(filePath);
        var objectCounts = events.apply(ItemFilters.type("jdk.ObjectCount"));

        if (!objectCounts.hasItems()) {
            return "No object count events found in this recording. " +
                    "Ensure 'Object Count' (jdk.ObjectCount) events were enabled (usually requires Full GC or specific flags).";
        }

        // We usually want the latest snapshot of object counts
        // ObjectCount events often have a 'gcId' attribute.
        Map<String, Long> classToSize = new HashMap<>();
        Map<String, Long> classToCount = new HashMap<>();

        // Find the maximum GC ID to get the most recent snapshot
        long maxGcId = -1;
        for (var iterable : objectCounts) {
            for (IItem item : iterable) {
                Number gcId = JfrItemUtils.getMember(item, "gcId");
                if (gcId != null && gcId.longValue() > maxGcId) {
                    maxGcId = gcId.longValue();
                }
            }
        }

        for (var iterable : objectCounts) {
            for (IItem item : iterable) {
                Number gcId = JfrItemUtils.getMember(item, "gcId");
                if (gcId != null && gcId.longValue() == maxGcId) {
                    Object classObj = JfrItemUtils.getMember(item, "objectClass");
                    IQuantity size = JfrItemUtils.getQuantity(item, "totalSize");
                    IQuantity count = JfrItemUtils.getQuantity(item, "count");

                    if (classObj != null && size != null) {
                        String className = classObj.toString();
                        classToSize.put(className, size.clampedLongValueIn(size.getUnit()));
                        if (count != null) {
                            classToCount.put(className, count.clampedLongValueIn(count.getUnit()));
                        }
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Object Statistics (Latest Snapshot, GC ID: ").append(maxGcId).append(")\n\n");
        sb.append("| Class | Total Size | Instance Count |\n");
        sb.append("|-------|------------|----------------|\n");

        classToSize.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .forEach(e -> {
                    long size = e.getValue();
                    long count = classToCount.getOrDefault(e.getKey(), 0L);
                    sb.append(String.format("| `%s` | %s | %d |%n",
                            e.getKey(), formatBytes(size), count));
                });

        return sb.toString();
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    @SuppressWarnings("unchecked")
    private static String getString(Map<String, Object> args, String key) {
        Object val = args.get(key);
        if (val == null) {
            throw new IllegalArgumentException("Missing required argument: " + key);
        }
        return val.toString();
    }

    private static int getIntOrDefault(Map<String, Object> args, String key, int defaultValue) {
        Object val = args.get(key);
        if (val instanceof Number n) {
            return n.intValue();
        }
        if (val instanceof String s) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
