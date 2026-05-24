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
import java.util.Map;

/**
 * MCP tool for extracting periodic thread dumps from a JFR recording.
 */
public final class ThreadDumpTool {

    private static final String NAME = "thread_dumps";

    private final JfrAnalysisService service;

    public ThreadDumpTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Extract periodic thread dumps from a JFR recording. " +
                                "Returns the text of thread dumps captured at various points during the recording.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "max_dumps", SchemaUtil.intProp("Maximum number of thread dumps to return (default 5)", 5)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        int maxDumps = getIntOrDefault(request.arguments(), "max_dumps", 5);
                        String result = analyze(filePath, maxDumps);
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

    private String analyze(String filePath, int maxDumps) throws IOException {
        IItemCollection events = service.loadRecording(filePath);
        var threadDumps = events.apply(ItemFilters.type("jdk.ThreadDump"));

        if (!threadDumps.hasItems()) {
            return "No periodic thread dumps found in this recording. " +
                    "Ensure 'Thread Dump' events were enabled in the recording settings.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Dumps\n\n");

        int count = 0;
        for (var itemIterable : threadDumps) {
            for (IItem item : itemIterable) {
                if (count >= maxDumps) break;

                Object result = JfrItemUtils.getMember(item, "result");
                Object startTime = JfrAttributes.START_TIME.getAccessor(itemIterable.getType()).getMember(item);

                if (result != null) {
                    sb.append("## Dump at ").append(startTime).append("\n\n");
                    sb.append("```\n");
                    sb.append(result);
                    sb.append("\n```\n\n");
                    count++;
                }
            }
            if (count >= maxDumps) break;
        }

        return sb.toString();
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
