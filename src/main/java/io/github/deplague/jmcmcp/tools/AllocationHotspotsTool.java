package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.Aggregators;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

import java.io.IOException;
import java.util.*;

/**
 * MCP tool for memory allocation hotspot analysis.
 */
public final class AllocationHotspotsTool {

    private static final String NAME = "allocation_hotspots";

    private final JfrAnalysisService service;

    public AllocationHotspotsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Find memory allocation hotspots in a JFR recording. " +
                                "Analyzes TLAB and outside-TLAB allocations to identify classes and code sites " +
                                "responsible for the most allocations.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "top_n", SchemaUtil.intProp("Number of top allocated classes to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        int topN = getIntOrDefault(request.arguments(), "top_n", 10);
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
        StringBuilder sb = new StringBuilder();
        sb.append("# Allocation Hotspots\n\n");

        // ObjectAllocationInNewTLAB
        var newTLAB = events.apply(ItemFilters.type("jdk.ObjectAllocationInNewTLAB"));
        if (newTLAB.hasItems()) {
            sb.append("## Allocations in New TLAB\n");
            IQuantity count = newTLAB.getAggregate(Aggregators.count());
            double totalSize = JfrItemUtils.sumQuantity(newTLAB, "tlabSize");
            sb.append(String.format("- **Event Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Total TLAB Size:** %s%n", formatBytes((long) totalSize)));
            sb.append("\n");

            Map<String, Long> classAllocations = new HashMap<>();
            for (var itemIterable : newTLAB) {
                for (IItem item : itemIterable) {
                    Object classObj = JfrItemUtils.getMember(item, "objectClass");
                    IQuantity size = JfrItemUtils.getQuantity(item, "tlabSize");
                    if (classObj != null && size != null) {
                        String className = classObj.toString();
                        long bytes = size.clampedLongValueIn(size.getUnit());
                        classAllocations.merge(className, bytes, Long::sum);
                    }
                }
            }

            if (!classAllocations.isEmpty()) {
                sb.append("### Top Allocated Classes by TLAB Size\n");
                sb.append("| Class | Total Bytes |\n");
                sb.append("|-------|-------------|\n");
                classAllocations.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(topN)
                        .forEach(e -> sb.append(String.format("| `%s` | %s |%n",
                                e.getKey(), formatBytes(e.getValue()))));
                sb.append("\n");
            }
        }

        // ObjectAllocationOutsideTLAB
        var outsideTLAB = events.apply(ItemFilters.type("jdk.ObjectAllocationOutsideTLAB"));
        if (outsideTLAB.hasItems()) {
            sb.append("## Allocations Outside TLAB (Large Objects)\n");
            IQuantity count = outsideTLAB.getAggregate(Aggregators.count());
            double totalSize = JfrItemUtils.sumQuantity(outsideTLAB, "allocationSize");
            sb.append(String.format("- **Event Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Total Allocation Size:** %s%n", formatBytes((long) totalSize)));
            sb.append("\n");

            Map<String, Long> classAllocations = new HashMap<>();
            for (var itemIterable : outsideTLAB) {
                for (IItem item : itemIterable) {
                    Object classObj = JfrItemUtils.getMember(item, "objectClass");
                    IQuantity size = JfrItemUtils.getQuantity(item, "allocationSize");
                    if (classObj != null && size != null) {
                        String className = classObj.toString();
                        long bytes = size.clampedLongValueIn(size.getUnit());
                        classAllocations.merge(className, bytes, Long::sum);
                    }
                }
            }

            if (!classAllocations.isEmpty()) {
                sb.append("### Top Allocated Classes (Outside TLAB)\n");
                sb.append("| Class | Total Bytes |\n");
                sb.append("|-------|-------------|\n");
                classAllocations.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(topN)
                        .forEach(e -> sb.append(String.format("| `%s` | %s |%n",
                                e.getKey(), formatBytes(e.getValue()))));
                sb.append("\n");
            }
        }

        // ObjectAllocationSample (JDK 17+)
        var allocSample = events.apply(ItemFilters.type("jdk.ObjectAllocationSample"));
        if (allocSample.hasItems()) {
            sb.append("## Object Allocation Samples\n");
            IQuantity count = allocSample.getAggregate(Aggregators.count());
            sb.append(String.format("- **Sample Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append("\n");

            Map<String, Long> classAllocations = new HashMap<>();
            for (var itemIterable : allocSample) {
                for (IItem item : itemIterable) {
                    Object classObj = JfrItemUtils.getMember(item, "objectClass");
                    IQuantity size = JfrItemUtils.getQuantity(item, "weight");
                    if (classObj != null && size != null) {
                        String className = classObj.toString();
                        long bytes = size.clampedLongValueIn(size.getUnit());
                        classAllocations.merge(className, bytes, Long::sum);
                    }
                }
            }

            if (!classAllocations.isEmpty()) {
                sb.append("### Top Allocated Classes by Sample Weight\n");
                sb.append("| Class | Estimated Bytes |\n");
                sb.append("|-------|----------------|\n");
                classAllocations.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(topN)
                        .forEach(e -> sb.append(String.format("| `%s` | %s |%n",
                                e.getKey(), formatBytes(e.getValue()))));
                sb.append("\n");
            }
        }

        if (!newTLAB.hasItems() && !outsideTLAB.hasItems() && !allocSample.hasItems()) {
            sb.append("No allocation events found in this recording. " +
                    "Enable allocation profiling when starting the recording.\n");
        }

        return sb.toString();
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024L * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }
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
