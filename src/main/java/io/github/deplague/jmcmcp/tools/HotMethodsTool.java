package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.IMCFrame;
import org.openjdk.jmc.common.IMCMethod;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IMemberAccessor;
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
        var execSamples = events.apply(ItemFilters.type("jdk.ExecutionSample"));

        if (!execSamples.hasItems()) {
            return "No CPU execution samples found in this recording. " +
                    "Ensure the recording was made with CPU profiling enabled.";
        }

        Map<String, Integer> methodCounts = new HashMap<>();

        for (var itemIterable : execSamples) {
            IMemberAccessor<IMCStackTrace, IItem> stackTraceAccessor = JfrAttributes.EVENT_STACKTRACE.getAccessor(itemIterable.getType());
            for (IItem item : itemIterable) {
                IMCStackTrace stackTrace = stackTraceAccessor.getMember(item);
                if (stackTrace == null || stackTrace.getFrames() == null || stackTrace.getFrames().isEmpty()) {
                    continue;
                }
                String methodName = extractTopMethod(stackTrace);
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
    private String extractTopMethod(IMCStackTrace stackTrace) {
        List<? extends IMCFrame> frames = stackTrace.getFrames();
        if (frames != null && !frames.isEmpty()) {
            IMCFrame frame = frames.get(0);
            return formatFrame(frame);
        }
        return null;
    }

    private String formatFrame(IMCFrame frame) {
        IMCMethod method = frame.getMethod();
        if (method != null) {
            String typeName = method.getType().getFullName();
            String methodName = method.getMethodName();
            String descriptor = method.getFormalDescriptor();
            return typeName + "." + methodName + descriptor;
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
