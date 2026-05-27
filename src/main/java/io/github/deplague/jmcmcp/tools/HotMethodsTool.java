package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.openjdk.jmc.common.item.*;

/**
 * MCP tool for identifying hot methods from execution samples.
 */
public final class HotMethodsTool {

    private static final String NAME = "hot_methods";

    private final JfrAnalysisService service;

    public HotMethodsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
            .tool(
                McpSchema.Tool.builder()
                    .name(NAME)
                    .description(
                        "Identify hot methods and call paths in a JFR recording based on execution samples."
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
                                "thread_name",
                                SchemaUtil.stringProp(
                                    "Optional thread name to filter execution samples by"
                                ),
                                "package_prefix",
                                SchemaUtil.stringProp(
                                    "Optional package prefix to filter stack traces (e.g., 'com.mycompany')"
                                ),
                                "top_n",
                                SchemaUtil.intProp(
                                    "Number of top hot methods to return (default 10)",
                                    10
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
                    String threadName = SchemaUtil.getStringOrDefault(
                        request.arguments(),
                        "thread_name",
                        null
                    );
                    String packagePrefix = SchemaUtil.getStringOrDefault(
                        request.arguments(),
                        "package_prefix",
                        null
                    );
                    int topN = SchemaUtil.getIntOrDefault(
                        request.arguments(),
                        "top_n",
                        10
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
                        threadName,
                        packagePrefix,
                        topN
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

    public String analyze(
        String filePath,
        String startTimeStr,
        String endTimeStr,
        String threadName,
        String packagePrefix,
        int topN
    ) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(
            allEvents,
            startTimeStr,
            endTimeStr
        );

        IItemCollection samples = events.apply(
            ItemFilters.type("jdk.ExecutionSample")
        );
        if (!samples.hasItems()) {
            return "# Hot Methods\n\nNo execution samples found in the recording.";
        }

        Map<String, Long> traceCounts = new HashMap<>();
        JfrItemUtils.StackTraceFormatCache stCache =
            JfrItemUtils.newStackTraceFormatCache();
        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> stackAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            IMemberAccessor<Object, IItem> threadAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "sampledThread");
            if (stackAccessor != null) {
                for (IItem item : iterable) {
                    if (threadName != null && threadAccessor != null) {
                        Object threadObj = threadAccessor.getMember(item);
                        if (
                            threadObj == null ||
                            !threadObj.toString().contains(threadName)
                        ) {
                            continue;
                        }
                    }

                    Object st = stackAccessor.getMember(item);
                    if (st != null) {
                        String formatted = stCache.formatFocusingOn(
                            st,
                            5,
                            packagePrefix
                        );
                        traceCounts.merge(formatted, 1L, Long::sum);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Hot Methods & Call Paths\n\n");
        sb.append("| Samples | Stack Trace (top 5 frames) |\n");
        sb.append("|---------|----------------------------|\n");

        traceCounts
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(topN)
            .forEach(entry -> {
                sb.append("| ").append(entry.getValue()).append(" | ");
                sb.append("`")
                    .append(entry.getKey().replace("\n", "`<br>`"))
                    .append("` |\n");
            });

        String topMethod = traceCounts
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .map(entry ->
                entry.getKey().split("\n")[0].trim().replace("at ", "")
            )
            .orElse("unknown");
        sb.append("\n<agent_hint>Top hot method is `")
            .append(topMethod)
            .append(
                "`. Consider `thread_cpu` to see which threads consume the most CPU, `stack_trace_search` with `class_pattern` to find all events involving this class, or `correlate` to see if this method is associated with lock contention or I/O.</agent_hint>\n"
            );

        return sb.toString();
    }
}
