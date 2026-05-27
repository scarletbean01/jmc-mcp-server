package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.HashMap;
import java.util.Map;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

public final class AllocationFlameTool {

    private static final String NAME = "allocation_flame";
    private final JfrAnalysisService service;

    public AllocationFlameTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
            .tool(
                McpSchema.Tool.builder()
                    .name(NAME)
                    .description(
                        "Provide allocation flame graph data by aggregating object allocations by full stack trace."
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
                                "package_prefix",
                                SchemaUtil.stringProp(
                                    "Optional package prefix to filter stack traces (e.g., 'com.mycompany')"
                                ),
                                "top_n",
                                SchemaUtil.intProp(
                                    "Number of top call paths (default 20)",
                                    20
                                ),
                                "async",
                                SchemaUtil.boolProp(
                                    "Run analysis asynchronously and return a job ID",
                                    false
                                )
                            ),
                            SchemaUtil.required("jfr_file_path")
                        )
                    )
                    .build()
            )
            .callHandler((exchange, request) ->
                service.execute(NAME, request.arguments(), () -> {
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
                    String packagePrefix = SchemaUtil.getStringOrDefault(
                        request.arguments(),
                        "package_prefix",
                        null
                    );
                    int topN = SchemaUtil.getIntOrDefault(
                        request.arguments(),
                        "top_n",
                        20
                    );
                    return analyze(
                        filePath,
                        startTimeStr,
                        endTimeStr,
                        packagePrefix,
                        topN
                    );
                })
            )
            .build();
    }

    public String analyze(
        String filePath,
        String startTimeStr,
        String endTimeStr,
        String packagePrefix,
        int topN
    ) throws Exception {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(
            allEvents,
            startTimeStr,
            endTimeStr
        );

        Map<String, Long> pathDist = new HashMap<>();
        JfrItemUtils.StackTraceFormatCache stCache =
            JfrItemUtils.newStackTraceFormatCache();
        long totalBytes = 0;

        for (String typeId : new String[] {
            "jdk.ObjectAllocationInNewTLAB",
            "jdk.ObjectAllocationOutsideTLAB",
        }) {
            IItemCollection allocs = events.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : allocs) {
                IMemberAccessor<Object, IItem> stackAccessor =
                    JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                IMemberAccessor<IQuantity, IItem> sizeAccessor =
                    JfrItemUtils.getAccessor(
                        iterable.getType(),
                        typeId.contains("Outside")
                            ? "allocationSize"
                            : "tlabSize"
                    );

                if (stackAccessor != null && sizeAccessor != null) {
                    for (IItem item : iterable) {
                        Object stackObj = stackAccessor.getMember(item);
                        IQuantity size = sizeAccessor.getMember(item);
                        if (stackObj != null && size != null) {
                            long bytes = size.longValue();
                            totalBytes += bytes;
                            String path = stCache.formatFocusingOn(
                                stackObj,
                                10,
                                packagePrefix
                            );
                            pathDist.merge(path, bytes, Long::sum);
                        }
                    }
                }
            }
        }

        if (totalBytes == 0) {
            return "# Allocation Flame Data\n\nNo allocation events found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Allocation Flame Graph Data\n\n");
        sb.append("- **Total Allocated Bytes:** ")
            .append(SchemaUtil.formatBytes(totalBytes))
            .append("\n\n");

        sb.append("## Top Allocation Call Paths (Max 10 frames)\n");
        sb.append(
            "| Allocated Bytes | Percentage | Call Path |\n|---|---|---|\n"
        );
        long finalTotal = totalBytes;
        pathDist
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(topN)
            .forEach(e ->
                sb.append(
                    String.format(
                        "| %s | %.1f%% | `%s` |\n",
                        SchemaUtil.formatBytes(e.getValue()),
                        (e.getValue() * 100.0) / finalTotal,
                        e.getKey().replace("\n", "`<br>`")
                    )
                )
            );

        return sb.toString();
    }
}
