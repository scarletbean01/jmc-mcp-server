package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.Aggregators;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.*;

/**
 * MCP tool for thread contention / lock analysis.
 */
public final class ThreadContentionTool {

    private static final String NAME = "thread_contention";

    private final JfrAnalysisService service;

    public ThreadContentionTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze thread contention, monitor blocking, and parking events in a JFR recording. " +
                                "Identifies the most contended locks and threads with highest blocking time.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "top_n", SchemaUtil.intProp("Number of top contended monitors to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        int topN = getIntOrDefault(request.arguments(), "top_n", 10);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, topN);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
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
        sb.append("# Thread Contention Analysis\n\n");

        // Java Monitor Enter (blocking on synchronized)
        var monitorEnter = events.apply(ItemFilters.type("jdk.JavaMonitorEnter"));
        if (monitorEnter.hasItems()) {
            sb.append("## Monitor Enter (Synchronized Blocking)\n");
            IQuantity count = monitorEnter.getAggregate(Aggregators.count());
            IQuantity avg = monitorEnter.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity max = monitorEnter.getAggregate(Aggregators.max(JfrAttributes.DURATION));
            IQuantity total = monitorEnter.getAggregate(Aggregators.sum(JfrAttributes.DURATION));

            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Average Block Time:** %s%n", JfrAnalysisService.display(avg)));
            sb.append(String.format("- **Max Block Time:** %s%n", JfrAnalysisService.display(max)));
            sb.append(String.format("- **Total Block Time:** %s%n", JfrAnalysisService.display(total)));
            sb.append("\n");

            // Top contended monitors by total duration
            Map<String, Long> monitorTimes = new HashMap<>();
            for (var itemIterable : monitorEnter) {
                IMemberAccessor<Object, IItem> monitorAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), "monitorClass");
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(itemIterable.getType());
                
                if (monitorAccessor != null && durationAccessor != null) {
                    for (IItem item : itemIterable) {
                        Object monitorObj = monitorAccessor.getMember(item);
                        IQuantity duration = durationAccessor.getMember(item);
                        if (monitorObj != null && duration != null) {
                            String monitorClass = monitorObj.toString();
                            long nanos = duration.clampedLongValueIn(duration.getUnit());
                            monitorTimes.merge(monitorClass, nanos, Long::sum);
                        }
                    }
                }
            }

            if (!monitorTimes.isEmpty()) {
                sb.append("### Top Contended Monitors by Block Time\n");
                sb.append("| Monitor Class | Total Block Time |\n");
                sb.append("|---------------|------------------|\n");
                monitorTimes.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(topN)
                        .forEach(e -> sb.append(String.format("| `%s` | %s |%n",
                                e.getKey(), formatNanos(e.getValue()))));
                sb.append("\n");
            }
        }

        // Java Monitor Wait (wait/notify)
        var monitorWait = events.apply(ItemFilters.type("jdk.JavaMonitorWait"));
        if (monitorWait.hasItems()) {
            sb.append("## Monitor Wait (wait/notify)\n");
            IQuantity count = monitorWait.getAggregate(Aggregators.count());
            IQuantity avg = monitorWait.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity max = monitorWait.getAggregate(Aggregators.max(JfrAttributes.DURATION));
            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Average Wait Time:** %s%n", JfrAnalysisService.display(avg)));
            sb.append(String.format("- **Max Wait Time:** %s%n", JfrAnalysisService.display(max)));
            sb.append("\n");
        }

        // Thread Park (LockSupport.park)
        var threadPark = events.apply(ItemFilters.type("jdk.ThreadPark"));
        if (threadPark.hasItems()) {
            sb.append("## Thread Park (LockSupport)\n");
            IQuantity count = threadPark.getAggregate(Aggregators.count());
            IQuantity avg = threadPark.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity max = threadPark.getAggregate(Aggregators.max(JfrAttributes.DURATION));
            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Average Park Time:** %s%n", JfrAnalysisService.display(avg)));
            sb.append(String.format("- **Max Park Time:** %s%n", JfrAnalysisService.display(max)));
            sb.append("\n");
        }

        // Thread Sleep
        var threadSleep = events.apply(ItemFilters.type("jdk.ThreadSleep"));
        if (threadSleep.hasItems()) {
            sb.append("## Thread Sleep\n");
            IQuantity count = threadSleep.getAggregate(Aggregators.count());
            IQuantity avg = threadSleep.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity total = threadSleep.getAggregate(Aggregators.sum(JfrAttributes.DURATION));
            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Average Sleep Time:** %s%n", JfrAnalysisService.display(avg)));
            sb.append(String.format("- **Total Sleep Time:** %s%n", JfrAnalysisService.display(total)));
            sb.append("\n");
        }

        if (!monitorEnter.hasItems() && !monitorWait.hasItems()
                && !threadPark.hasItems() && !threadSleep.hasItems()) {
            sb.append("No thread contention events found in this recording.\n");
        }

        return sb.toString();
    }

    private static String formatNanos(long nanos) {
        if (nanos < 1_000) {
            return nanos + " ns";
        } else if (nanos < 1_000_000) {
            return String.format("%.2f us", nanos / 1_000.0);
        } else if (nanos < 1_000_000_000) {
            return String.format("%.2f ms", nanos / 1_000_000.0);
        } else {
            return String.format("%.2f s", nanos / 1_000_000_000.0);
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
