package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * MCP tool for OS and environment context analysis.
 */
public final class ProcessInfoTool {

    private static final String NAME = "process_info";

    private final JfrAnalysisService service;

    public ProcessInfoTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Gather OS version, virtualization details, and running processes context.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp()
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath);
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

    private String analyze(String filePath) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);

        StringBuilder sb = new StringBuilder();
        sb.append("# OS & Environment Context\n\n");

        // 1. Operating System
        IItemCollection osInfo = allEvents.apply(ItemFilters.type("jdk.OSInformation"));
        if (osInfo.hasItems()) {
            sb.append("## Operating System\n\n");
            Optional<IItem> itemOpt = osInfo.stream().flatMap(IItemIterable::stream).findFirst();
            itemOpt.ifPresent(item -> {
                sb.append("- **OS Name:** ").append(JfrItemUtils.getMember(item, "osName").orElse("Unknown")).append("\n");
                sb.append("- **OS Version:** ").append(JfrItemUtils.getMember(item, "osVersion").orElse("Unknown")).append("\n");
                sb.append("- **Architecture:** ").append(JfrItemUtils.getMember(item, "osArch").orElse("Unknown")).append("\n");
            });
            sb.append("\n");
        }

        // 2. Virtualization
        IItemCollection virtInfo = allEvents.apply(ItemFilters.type("jdk.VirtualizationInformation"));
        if (virtInfo.hasItems()) {
            sb.append("## Virtualization\n\n");
            Optional<IItem> itemOpt = virtInfo.stream().flatMap(IItemIterable::stream).findFirst();
            itemOpt.ifPresent(item -> {
                String virtName = JfrItemUtils.getMember(item, "name").map(Object::toString)
                        .orElseGet(() -> JfrItemUtils.getMember(item, "virtualizationName").map(Object::toString).orElse("Unknown"));
                sb.append("- **Technology:** ").append(virtName).append("\n");
            });
            sb.append("\n");
        } else {
            sb.append("## Virtualization\n\n- **Technology:** Unknown or Bare Metal\n\n");
        }

        // 3. System Processes
        IItemCollection procs = allEvents.apply(ItemFilters.type("jdk.SystemProcess"));
        if (procs.hasItems()) {
            sb.append("## Running Processes (Top 50)\n\n");
            sb.append("| PID | Command Line |\n");
            sb.append("|-----|--------------|\n");

            List<ProcInfo> procList = new ArrayList<>();
            for (IItemIterable iterable : procs) {
                var pidAcc = JfrItemUtils.getAccessor(iterable.getType(), "pid");
                var cmdAcc = JfrItemUtils.getAccessor(iterable.getType(), "commandLine");
                if (pidAcc != null && cmdAcc != null) {
                    for (IItem item : iterable) {
                        Object pid = pidAcc.getMember(item);
                        Object cmd = cmdAcc.getMember(item);
                        if (pid != null && cmd != null) {
                            procList.add(new ProcInfo(pid.toString(), cmd.toString()));
                        }
                    }
                }
            }

            // Remove duplicates (multiple snapshots might exist)
            procList.stream().distinct()
                    .sorted(Comparator.comparing(p -> {
                        try { return Integer.parseInt(p.pid); } catch (Exception e) { return Integer.MAX_VALUE; }
                    }))
                    .limit(50)
                    .forEach(p -> {
                        String cmd = p.cmd;
                        if (cmd.length() > 200) cmd = cmd.substring(0, 200) + "...";
                        sb.append("| ").append(p.pid).append(" | `").append(cmd).append("` |\n");
                    });
            sb.append("\n");
        }

        if (!osInfo.hasItems() && !virtInfo.hasItems() && !procs.hasItems()) {
            return "# OS & Environment Context\n\nNo environment context events found in the recording.";
        }

        return sb.toString();
    }

    private record ProcInfo(String pid, String cmd) {}
}
