package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.*;

public final class DeadlockDetectionTool {

    private static final String NAME = "deadlock_detection";

    private final JfrAnalysisService service;

    public DeadlockDetectionTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Detect thread deadlocks by analyzing monitor ownership and wait-for relationships. " +
                                "Parses thread dump events to build a wait-for graph and detects cycles. " +
                                "Outputs Mermaid diagrams for visual deadlock rendering.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp()
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection threadDumps = events.apply(org.openjdk.jmc.common.item.ItemFilters.type("jdk.ThreadDump"));

        if (!threadDumps.hasItems()) {
            return analyzeFromMonitorEvents(events);
        }

        ThreadDumpData latestDump = extractLatestThreadDump(threadDumps);
        if (latestDump == null) {
            return "# Deadlock Detection\n\nNo parseable thread dump events found.";
        }

        List<DeadlockCycle> deadlocks = detectDeadlocks(latestDump);

        StringBuilder sb = new StringBuilder();
        sb.append("# Deadlock Detection\n\n");

        if (deadlocks.isEmpty()) {
            sb.append("## Verdict: ✅ NO DEADLOCKS DETECTED\n\n");
            sb.append("Analyzed ").append(latestDump.threads.size()).append(" threads from the latest thread dump.\n");
            sb.append("No cycles found in the monitor wait-for graph.\n\n");
            appendMonitorSummary(sb, latestDump);
        } else {
            sb.append("## Verdict: ⛔ ").append(deadlocks.size()).append(" DEADLOCK").append(deadlocks.size() > 1 ? "S" : "").append(" DETECTED\n\n");
            for (int i = 0; i < deadlocks.size(); i++) {
                appendDeadlockCycle(sb, deadlocks.get(i), i + 1);
            }
            appendRecommendations(sb, deadlocks);
        }

        return sb.toString();
    }

    private String analyzeFromMonitorEvents(IItemCollection events) {
        IItemCollection monitorEnter = events.apply(org.openjdk.jmc.common.item.ItemFilters.type("jdk.JavaMonitorEnter"));
        IItemCollection monitorWait = events.apply(org.openjdk.jmc.common.item.ItemFilters.type("jdk.JavaMonitorWait"));

        long enterCount = JfrItemUtils.count(monitorEnter);
        long waitCount = JfrItemUtils.count(monitorWait);

        if (enterCount == 0 && waitCount == 0) {
            return """
                    # Deadlock Detection
                    
                    No thread dump or monitor events found. \
                    Enable Thread Dump events or monitor events in your JFR recording configuration.""";
        }

        return "# Deadlock Detection\n\n" +
                "## ⚠️ Limited Analysis Mode\n\n" +
                "No `jdk.ThreadDump` events available. Deadlock detection requires thread dumps to reconstruct " +
                "monitor ownership graphs. Showing monitor contention summary instead.\n\n" +
                "- Monitor enter events: " + enterCount + "\n" +
                "- Monitor wait events: " + waitCount + "\n\n" +
                "**Recommendation:** Enable Thread Dump events in your JFR configuration " +
                "(`-XX:StartFlightRecording:settings=profile`) for full deadlock detection.\n";
    }

    private ThreadDumpData extractLatestThreadDump(IItemCollection threadDumps) {
        ThreadDumpData latest = null;
        IQuantity latestTime = null;

        for (IItemIterable iterable : threadDumps) {
            IMemberAccessor<Object, IItem> resultAccessor = JfrItemUtils.getAccessor(iterable.getType(), "result");
            IMemberAccessor<IQuantity, IItem> timeAccessor = JfrAttributes.START_TIME.getAccessor(iterable.getType());

            if (resultAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity time = timeAccessor != null ? timeAccessor.getMember(item) : null;
                    if (latestTime == null || (time != null && time.compareTo(latestTime) > 0)) {
                        Object result = resultAccessor.getMember(item);
                        if (result != null) {
                            latest = parseThreadDumpText(result.toString());
                            latestTime = time;
                        }
                    }
                }
            }
        }
        return latest;
    }

    private ThreadDumpData parseThreadDumpText(String dumpText) {
        ThreadDumpData data = new ThreadDumpData();
        ThreadInfo currentThread = null;

        for (String line : dumpText.split("\n")) {
            line = line.trim();
            if (line.startsWith("\"") && line.contains("-")) {
                String threadName = line.substring(1, line.indexOf("\"", 1));
                currentThread = new ThreadInfo(threadName);
                data.threads.add(currentThread);
            } else if (currentThread != null && line.contains("-")) {
                String trimmed = line.trim();
                if (trimmed.endsWith("- LOCKED")) {
                    String monitor = extractMonitor(trimmed.replace("- LOCKED", "").trim());
                    if (monitor != null) {
                        currentThread.heldMonitors.add(monitor);
                    }
                } else if (trimmed.endsWith("- WAITING") || trimmed.endsWith("- BLOCKED")) {
                    String monitor = extractMonitor(trimmed.replaceAll("- (WAITING|BLOCKED)$", "").trim());
                    if (monitor != null) {
                        currentThread.waitingFor = monitor;
                    }
                }
            }
        }
        return data;
    }

    private String extractMonitor(String text) {
        int atIndex = text.lastIndexOf('@');
        if (atIndex < 0) return text.trim();
        String afterAt = text.substring(atIndex + 1).trim();
        int spaceIndex = afterAt.indexOf(' ');
        return spaceIndex > 0 ? afterAt.substring(0, spaceIndex) : afterAt;
    }

    private List<DeadlockCycle> detectDeadlocks(ThreadDumpData data) {
        Map<String, String> monitorOwner = new HashMap<>();
        for (ThreadInfo thread : data.threads) {
            for (String monitor : thread.heldMonitors) {
                monitorOwner.put(monitor, thread.name);
            }
        }

        Map<String, String> waitFor = new HashMap<>();
        for (ThreadInfo thread : data.threads) {
            if (thread.waitingFor != null) {
                waitFor.put(thread.name, thread.waitingFor);
            }
        }

        List<DeadlockCycle> cycles = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (ThreadInfo thread : data.threads) {
            if (thread.waitingFor == null) continue;

            List<String> path = new ArrayList<>();
            String current = thread.name;
            Set<String> pathSet = new HashSet<>();

            while (current != null && !pathSet.contains(current)) {
                pathSet.add(current);
                path.add(current);

                String monitor = waitFor.get(current);
                if (monitor == null) break;

                String owner = monitorOwner.get(monitor);
                if (owner == null) break;

                if (pathSet.contains(owner)) {
                    int cycleStart = path.indexOf(owner);
                    List<String> cycleThreads = path.subList(cycleStart, path.size());
                    List<String> cycleMonitors = new ArrayList<>();
                    for (String t : cycleThreads) {
                        String m = waitFor.get(t);
                        if (m != null) cycleMonitors.add(m);
                    }
                    String cycleKey = String.join("->", cycleThreads);
                    if (!visited.contains(cycleKey)) {
                        visited.add(cycleKey);
                        cycles.add(new DeadlockCycle(new ArrayList<>(cycleThreads), cycleMonitors));
                    }
                    break;
                }
                current = owner;
            }
        }
        return cycles;
    }

    private void appendDeadlockCycle(StringBuilder sb, DeadlockCycle cycle, int index) {
        sb.append("### Deadlock Cycle ").append(index).append("\n\n");
        sb.append("```mermaid\ngraph TD\n");
        for (int i = 0; i < cycle.threads.size(); i++) {
            String thread = cycle.threads.get(i);
            String monitor = cycle.monitors.get(i);
            String nextThread = cycle.threads.get((i + 1) % cycle.threads.size());
            sb.append("  ").append(safeMermaid(thread)).append("[\"").append(escapeMermaid(thread)).append("\"]")
                    .append(" -->|waiting for| ").append(safeMermaid(monitor))
                    .append("[\"Monitor ").append(escapeMermaid(monitor)).append("\"]\n");
            sb.append("  ").append(safeMermaid(monitor)).append(" -->|held by| ")
                    .append(safeMermaid(nextThread)).append("\n");
        }
        sb.append("```\n\n");

        sb.append("**Threads involved:** ").append(String.join(", ", cycle.threads)).append("\n\n");

        sb.append("| Thread | Waits For | Held By |\n");
        sb.append("|--------|-----------|--------|\n");
        for (int i = 0; i < cycle.threads.size(); i++) {
            String thread = cycle.threads.get(i);
            String monitor = cycle.monitors.get(i);
            String nextThread = cycle.threads.get((i + 1) % cycle.threads.size());
            sb.append(String.format("| %s | Monitor %s | %s |\n", thread, monitor, nextThread));
        }
        sb.append("\n");
    }

    private void appendRecommendations(StringBuilder sb, List<DeadlockCycle> deadlocks) {
        sb.append("## Recommendations\n\n");
        sb.append("To resolve deadlocks:\n\n");
        sb.append("1. **Lock ordering:** Always acquire locks in a consistent global order\n");
        sb.append("2. **Use `tryLock()` with timeouts:** `java.util.concurrent.locks.Lock.tryLock(timeout, unit)` prevents indefinite blocking\n");
        sb.append("3. **Reduce lock scope:** Minimize the time holding locks by moving non-critical work outside synchronized blocks\n");
        sb.append("4. **Lock ordering by identity:** Use `System.identityHashCode()` to determine lock acquisition order\n\n");
        if (deadlocks.size() > 1) {
            sb.append("⚠️ Multiple deadlocks detected. Investigate each cycle independently.\n");
        }
    }

    private void appendMonitorSummary(StringBuilder sb, ThreadDumpData data) {
        int threadsWithLocks = 0;
        int threadsWaiting = 0;
        int totalMonitors = 0;
        for (ThreadInfo t : data.threads) {
            if (!t.heldMonitors.isEmpty()) {
                threadsWithLocks++;
                totalMonitors += t.heldMonitors.size();
            }
            if (t.waitingFor != null) threadsWaiting++;
        }
        sb.append("## Monitor Summary\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append(String.format("| Threads Analyzed | %d |\n", data.threads.size()));
        sb.append(String.format("| Threads Holding Locks | %d |\n", threadsWithLocks));
        sb.append(String.format("| Threads Waiting for Locks | %d |\n", threadsWaiting));
        sb.append(String.format("| Total Monitors | %d |\n", totalMonitors));
    }

    private String safeMermaid(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "_").replaceAll("_+", "_");
    }

    private String escapeMermaid(String s) {
        return s.replace("\"", "'");
    }

    private static class ThreadDumpData {
        List<ThreadInfo> threads = new ArrayList<>();
    }

    private static class ThreadInfo {
        String name;
        List<String> heldMonitors = new ArrayList<>();
        String waitingFor;

        ThreadInfo(String name) {
            this.name = name;
        }
    }

    private static class DeadlockCycle {
        List<String> threads;
        List<String> monitors;

        DeadlockCycle(List<String> threads, List<String> monitors) {
            this.threads = threads;
            this.monitors = monitors;
        }
    }
}