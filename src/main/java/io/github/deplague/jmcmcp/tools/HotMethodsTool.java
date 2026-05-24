package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.*;

/**
 * MCP tool that extracts the top hot methods from CPU execution samples.
 */
public final class HotMethodsTool {

    private static final String NAME = "hot_methods";

    private final JfrAnalysisService service;

    public HotMethodsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Identify the hottest methods by CPU sample count from a JFR recording. " +
                                "Useful for finding performance bottlenecks in application code.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "top_n", SchemaUtil.intProp("Number of top hot methods to return (default 10)", 10)
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
        var execSamples = events.apply(ItemFilters.type("jdk.ExecutionSample"));

        if (!execSamples.hasItems()) {
            return "No CPU execution samples found in this recording. " +
                    "Ensure the recording was made with CPU profiling enabled.";
        }

        Map<String, Integer> methodCounts = new HashMap<>();

        for (var itemIterable : execSamples) {
            for (IItem item : itemIterable) {
                Object stackTraceObj = JfrAttributes.EVENT_STACKTRACE.getAccessor(itemIterable.getType()).getMember(item);
                if (stackTraceObj == null) {
                    continue;
                }
                String methodName = extractTopMethod(stackTraceObj);
                if (methodName != null) {
                    methodCounts.merge(methodName, 1, Integer::sum);
                }
            }
        }

        if (methodCounts.isEmpty()) {
            return "No stack traces available in execution samples.";
        }

        List<Map.Entry<String, Integer>> sorted = methodCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topN)
                .toList();

        int totalSamples = methodCounts.values().stream().mapToInt(Integer::intValue).sum();

        StringBuilder sb = new StringBuilder();
        sb.append("# Hot Methods (CPU Execution Samples)\n\n");
        sb.append(String.format("**Total samples:** %d%n%n", totalSamples));
        sb.append("| Rank | Method | Samples | Percentage |\n");
        sb.append("|------|--------|---------|-----------|\n");

        int rank = 1;
        for (var entry : sorted) {
            double pct = 100.0 * entry.getValue() / totalSamples;
            sb.append(String.format("| %d | `%s` | %d | %.2f%% |%n",
                    rank++, entry.getKey(), entry.getValue(), pct));
        }

        return sb.toString();
    }

    /**
     * Extract the top (innermost) method name from a JMC stack trace object.
     */
    private String extractTopMethod(Object stackTraceObj) {
        try {
            if (stackTraceObj instanceof Iterable<?> iterable) {
                Iterator<?> it = iterable.iterator();
                if (it.hasNext()) {
                    Object frame = it.next();
                    return extractMethodFromFrame(frame);
                }
            }
            java.lang.reflect.Method getFrames = stackTraceObj.getClass().getMethod("getFrames");
            Object frames = getFrames.invoke(stackTraceObj);
            if (frames instanceof List<?> list && !list.isEmpty()) {
                return extractMethodFromFrame(list.get(0));
            }
        } catch (Exception e) {
            // Fall through
        }
        return stackTraceObj.toString();
    }

    private String extractMethodFromFrame(Object frame) {
        try {
            java.lang.reflect.Method getMethod = frame.getClass().getMethod("getMethod");
            Object method = getMethod.invoke(frame);
            if (method != null) {
                java.lang.reflect.Method getType = method.getClass().getMethod("getType");
                Object type = getType.invoke(method);
                String typeName = type != null ? type.toString() : "";

                java.lang.reflect.Method getMethodName = method.getClass().getMethod("getMethodName");
                Object methodName = getMethodName.invoke(method);
                String name = methodName != null ? methodName.toString() : "";

                java.lang.reflect.Method getFormalDescriptor = method.getClass().getMethod("getFormalDescriptor");
                Object descriptor = getFormalDescriptor.invoke(method);
                String desc = descriptor != null ? descriptor.toString() : "()";

                return typeName + "." + name + desc;
            }
        } catch (Exception e) {
            // Fall through
        }
        return frame.toString();
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
