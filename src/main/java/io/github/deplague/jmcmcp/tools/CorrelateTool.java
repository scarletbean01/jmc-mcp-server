package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.*;

/**
 * MCP tool for cross-dimensional correlation engine.
 * Automatically cross-references lock contention sites, I/O hotspots, and hot methods
 * to identify correlated request paths and bottleneck chains.
 */
public final class CorrelateTool {

    private static final String NAME = "correlate";

    private final JfrAnalysisService service;

    public CorrelateTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Cross-dimensional correlation engine that automatically links locks, I/O, and hot methods " +
                                "to identify correlated request paths and bottleneck chains.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "dimension", SchemaUtil.stringProp("Correlation dimension: lock_io_db, cpu_gc, or all (default)", List.of("lock_io_db", "cpu_gc", "all")),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top results per section (default 10)", 10),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> service.execute(NAME, request.arguments(), () -> {
                    String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                    String dimension = SchemaUtil.getStringOrDefault(request.arguments(), "dimension", "all");
                    String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                    String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                    int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);
                    return analyze(filePath, dimension, startTimeStr, endTimeStr, topN);
                }))
                .build();
    }

    public String analyze(String filePath, String dimension, String startTimeStr, String endTimeStr, int topN) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        StringBuilder sb = new StringBuilder();
        sb.append("# Cross-Dimensional Correlation Analysis\n\n");

        boolean showLockIo = "all".equals(dimension) || "lock_io_db".equals(dimension);
        boolean showCpuGc = "all".equals(dimension) || "cpu_gc".equals(dimension);

        if (showLockIo) {
            List<LockSite> lockSites = extractLockSites(events, topN);
            List<IoSite> ioSites = extractIoSites(events, topN);
            List<HotMethod> hotMethods = extractHotMethods(events, topN);

            appendLockIoCorrelation(sb, lockSites, ioSites, topN);
            appendHotMethodLockCorrelation(sb, hotMethods, lockSites, topN);
            appendHotMethodIoCorrelation(sb, hotMethods, ioSites, topN);
            appendBottleneckChain(sb, hotMethods, lockSites, ioSites);
        }

        if (showCpuGc) {
            appendCpuGcCorrelation(sb, events, topN);
        }

        sb.append("<agent_hint>Correlation analysis complete. ");
        sb.append("Use `request_waterfall` to trace specific threads, ");
        sb.append("`thread_contention` for lock details, or `io_hotspots` for I/O analysis.</agent_hint>\n");

        return sb.toString();
    }

    private List<LockSite> extractLockSites(IItemCollection events, int topN) {
        Map<String, LockSite> sites = new LinkedHashMap<>();
        for (String typeId : List.of("jdk.JavaMonitorEnter", "jdk.JavaMonitorWait")) {
            IItemCollection typeEvents = events.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<Object, IItem> monitorAccessor = JfrItemUtils.getAccessor(iterable.getType(), "monitorClass");
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                if (monitorAccessor == null || durationAccessor == null) continue;

                for (IItem item : iterable) {
                    Object monitorObj = monitorAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (monitorObj == null || duration == null) continue;

                    String monitorClass = monitorObj.toString();
                    String topFrame = "";
                    if (stackAccessor != null) {
                        Object st = stackAccessor.getMember(item);
                        if (st != null) topFrame = JfrItemUtils.formatStackTrace(st, 1).replace("at ", "").trim();
                    }

                    String key = monitorClass + "@" + topFrame;
                    String topFrameCopy = topFrame;
                    LockSite site = sites.computeIfAbsent(key, k -> new LockSite(monitorClass, topFrameCopy));
                    site.totalDurationMs += duration.clampedLongValueIn(UnitLookup.MILLISECOND);
                    site.count++;
                }
            }
        }

        List<LockSite> result = new ArrayList<>(sites.values());
        result.sort(Comparator.comparingLong(LockSite::getTotalDurationMs).reversed());
        return result.subList(0, Math.min(topN, result.size()));
    }

    private List<IoSite> extractIoSites(IItemCollection events, int topN) {
        Map<String, IoSite> sites = new LinkedHashMap<>();
        for (String typeId : List.of("jdk.SocketRead", "jdk.SocketWrite", "jdk.FileRead", "jdk.FileWrite")) {
            IItemCollection typeEvents = events.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                if (durationAccessor == null) continue;

                String targetAttr = typeId.contains("Socket") ? "host" : "path";
                String portAttr = typeId.contains("Socket") ? "port" : null;
                String bytesAttr = typeId.contains("Read") ? "bytesRead" : "bytesWritten";

                IMemberAccessor<Object, IItem> targetAccessor = JfrItemUtils.getAccessor(iterable.getType(), targetAttr);
                IMemberAccessor<IQuantity, IItem> portAccessor = portAttr != null ? JfrItemUtils.getAccessor(iterable.getType(), portAttr) : null;
                IMemberAccessor<IQuantity, IItem> bytesAccessor = JfrItemUtils.getAccessor(iterable.getType(), bytesAttr);

                for (IItem item : iterable) {
                    IQuantity duration = durationAccessor.getMember(item);
                    if (duration == null) continue;

                    String endpoint = "unknown";
                    if (targetAccessor != null) {
                        Object targetObj = targetAccessor.getMember(item);
                        if (targetObj != null) {
                            endpoint = targetObj.toString();
                            if (portAccessor != null) {
                                IQuantity portQ = portAccessor.getMember(item);
                                if (portQ != null) endpoint += ":" + (int) portQ.longValue();
                            }
                        }
                    }

                    String topFrame = "";
                    if (stackAccessor != null) {
                        Object st = stackAccessor.getMember(item);
                        if (st != null) topFrame = JfrItemUtils.formatStackTrace(st, 1).replace("at ", "").trim();
                    }

                    long bytes = 0;
                    if (bytesAccessor != null) {
                        IQuantity bytesQ = bytesAccessor.getMember(item);
                        if (bytesQ != null) bytes = bytesQ.longValue();
                    }

                    String key = typeId + "@" + endpoint;
                    String endpointCopy = endpoint;
                    String topFrameCopy = topFrame;
                    IoSite site = sites.computeIfAbsent(key, k -> new IoSite(typeId, endpointCopy, topFrameCopy));
                    site.totalDurationMs += duration.clampedLongValueIn(UnitLookup.MILLISECOND);
                    site.count++;
                    site.totalBytes += bytes;
                }
            }
        }

        List<IoSite> result = new ArrayList<>(sites.values());
        result.sort(Comparator.comparingLong(IoSite::getTotalDurationMs).reversed());
        return result.subList(0, Math.min(topN, result.size()));
    }

    private List<HotMethod> extractHotMethods(IItemCollection events, int topN) {
        Map<String, HotMethod> methods = new LinkedHashMap<>();
        IItemCollection samples = events.apply(ItemFilters.type("jdk.ExecutionSample"));
        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            if (stackAccessor == null) continue;

            for (IItem item : iterable) {
                Object st = stackAccessor.getMember(item);
                if (st == null) continue;
                String topFrame = JfrItemUtils.formatStackTrace(st, 1).replace("at ", "").trim();
                if (topFrame.isEmpty() || topFrame.startsWith("...") || topFrame.startsWith("No stack")) continue;
                methods.merge(topFrame, new HotMethod(topFrame, 1L), (a, b) -> new HotMethod(a.methodName, a.sampleCount + b.sampleCount));
            }
        }

        List<HotMethod> result = new ArrayList<>(methods.values());
        result.sort(Comparator.comparingLong(HotMethod::sampleCount).reversed());
        return result.subList(0, Math.min(topN, result.size()));
    }

    private void appendLockIoCorrelation(StringBuilder sb, List<LockSite> lockSites, List<IoSite> ioSites, int topN) {
        sb.append("## Lock ↔ I/O Correlation\n\n");
        if (lockSites.isEmpty() || ioSites.isEmpty()) {
            sb.append("Insufficient lock or I/O data for correlation.\n\n");
            return;
        }

        sb.append("| Lock | I/O Under Lock | Total Lock Time | I/O Time Under Lock | % I/O Under Lock |\n");
        sb.append("|------|---------------|-----------------|---------------------|------------------|\n");

        long totalLockDuration = lockSites.stream().mapToLong(LockSite::getTotalDurationMs).sum();
        long totalIoDuration = ioSites.stream().mapToLong(IoSite::getTotalDurationMs).sum();

        int count = 0;
        for (LockSite lock : lockSites) {
            if (count >= topN) break;
            double ioUnderLockPct = totalIoDuration > 0 ? (totalIoDuration * 100.0 / (totalLockDuration + totalIoDuration)) : 0;
            String ioEndpoints = ioSites.stream().limit(3).map(s -> s.endpoint).reduce((a, b) -> a + ", " + b).orElse("N/A");

            sb.append(String.format("| `%s` | %s | %s | %s | %.1f%% |\n",
                    lock.monitorClass, ioEndpoints,
                    SchemaUtil.formatDuration(lock.totalDurationMs),
                    SchemaUtil.formatDuration(totalIoDuration),
                    ioUnderLockPct));
            count++;
        }
        sb.append("\n");

        if (totalLockDuration > 0 && totalIoDuration > totalLockDuration * 0.5) {
            sb.append("**Interpretation:** Locks where >50% of hold time is I/O indicate blocking I/O under lock — a critical anti-pattern. Consider decoupling I/O from synchronized blocks.\n\n");
        }
    }

    private void appendHotMethodLockCorrelation(StringBuilder sb, List<HotMethod> hotMethods, List<LockSite> lockSites, int topN) {
        sb.append("## Hot Method ↔ Lock Correlation\n\n");
        if (hotMethods.isEmpty() || lockSites.isEmpty()) {
            sb.append("Insufficient hot method or lock data for correlation.\n\n");
            return;
        }

        sb.append("| Hot Method | Lock Contentions | Total Contention Time | % of Total Contention |\n");
        sb.append("|------------|-----------------|----------------------|----------------------|\n");

        long totalContentionMs = lockSites.stream().mapToLong(LockSite::getTotalDurationMs).sum();

        int count = 0;
        for (HotMethod method : hotMethods) {
            if (count >= topN) break;
            List<LockSite> relatedLocks = lockSites.stream()
                    .filter(ls -> ls.topFrame.contains(method.methodName) || method.methodName.contains(ls.topFrame))
                    .toList();

            if (relatedLocks.isEmpty()) continue;

            long relatedDuration = relatedLocks.stream().mapToLong(LockSite::getTotalDurationMs).sum();
            double pct = totalContentionMs > 0 ? (relatedDuration * 100.0 / totalContentionMs) : 0;

            sb.append(String.format("| `%s` | %s | %s | %.1f%% |\n",
                    method.methodName,
                    relatedLocks.stream().map(ls -> "`" + ls.monitorClass + "`").reduce((a, b) -> a + ", " + b).orElse("N/A"),
                    SchemaUtil.formatDuration(relatedDuration),
                    pct));
            count++;
        }
        sb.append("\n");
    }

    private void appendHotMethodIoCorrelation(StringBuilder sb, List<HotMethod> hotMethods, List<IoSite> ioSites, int topN) {
        sb.append("## Hot Method ↔ I/O Correlation\n\n");
        if (hotMethods.isEmpty() || ioSites.isEmpty()) {
            sb.append("Insufficient hot method or I/O data for correlation.\n\n");
            return;
        }

        sb.append("| Hot Method | I/O Endpoints | Total I/O Time | % of Total I/O |\n");
        sb.append("|------------|---------------|----------------|----------------|\n");

        long totalIoMs = ioSites.stream().mapToLong(IoSite::getTotalDurationMs).sum();

        int count = 0;
        for (HotMethod method : hotMethods) {
            if (count >= topN) break;
            List<IoSite> relatedIo = ioSites.stream()
                    .filter(io -> io.topFrame.contains(method.methodName) || method.methodName.contains(io.topFrame))
                    .toList();

            if (relatedIo.isEmpty()) continue;

            long relatedDuration = relatedIo.stream().mapToLong(IoSite::getTotalDurationMs).sum();
            double pct = totalIoMs > 0 ? (relatedDuration * 100.0 / totalIoMs) : 0;

            sb.append(String.format("| `%s` | %s | %s | %.1f%% |\n",
                    method.methodName,
                    relatedIo.stream().map(io -> "`" + io.endpoint + "`").reduce((a, b) -> a + ", " + b).orElse("N/A"),
                    SchemaUtil.formatDuration(relatedDuration),
                    pct));
            count++;
        }
        sb.append("\n");
    }

    private void appendBottleneckChain(StringBuilder sb, List<HotMethod> hotMethods, List<LockSite> lockSites, List<IoSite> ioSites) {
        sb.append("## Bottleneck Chain\n\n");
        sb.append("The longest sequential dependency chain identified:\n\n```\n");

        int step = 1;
        for (HotMethod method : hotMethods.stream().limit(2).toList()) {
            sb.append(step++).append(". CPU: ").append(method.methodName).append(" [hot method]\n");

            List<LockSite> relatedLocks = lockSites.stream()
                    .filter(ls -> ls.topFrame.contains(method.methodName) || method.methodName.contains(ls.topFrame))
                    .limit(1).toList();
            for (LockSite lock : relatedLocks) {
                sb.append("   ↓ (same thread)\n");
                sb.append(step++).append(". BLOCKED: ").append(lock.monitorClass)
                        .append(" [").append(SchemaUtil.formatDuration(lock.totalDurationMs / Math.max(lock.count, 1)))
                        .append(" avg wait]\n");

                List<IoSite> relatedIo = ioSites.stream()
                        .filter(io -> io.topFrame.contains(method.methodName) || method.methodName.contains(io.topFrame))
                        .limit(2).toList();
                for (IoSite io : relatedIo) {
                    sb.append("   ↓ (acquired, then)\n");
                    sb.append(step++).append(". I/O: ").append(io.endpoint)
                            .append(" [").append(SchemaUtil.formatDuration(io.totalDurationMs / Math.max(io.count, 1)))
                            .append(" avg]\n");
                }
            }
        }

        if (step == 1) {
            sb.append("No clear bottleneck chain identified from available data.\n");
        }
        sb.append("```\n\n");
    }

    private void appendCpuGcCorrelation(StringBuilder sb, IItemCollection events, int topN) {
        sb.append("## CPU ↔ GC Correlation\n\n");

        IItemCollection cpuLoad = events.apply(ItemFilters.type("jdk.CPULoad"));
        IItemCollection gcPauses = events.apply(ItemFilters.type("jdk.GCPhasePause"));

        if (!cpuLoad.hasItems()) {
            sb.append("No CPU load data available.\n\n");
            return;
        }

        IQuantity avgCpu = JfrItemUtils.avgQuantity(cpuLoad, "machineTotal");
        IQuantity maxCpu = JfrItemUtils.maxQuantity(cpuLoad, "machineTotal");
        long gcCount = JfrItemUtils.count(gcPauses);
        IQuantity totalGcPause = JfrItemUtils.sumQuantity(gcPauses, JfrAttributes.DURATION.getIdentifier());

        sb.append("| Metric | Value |\n|--------|-------|\n");
        if (avgCpu != null) sb.append(String.format("| Avg CPU Load | %.1f%% |\n", avgCpu.doubleValue() * 100));
        if (maxCpu != null) sb.append(String.format("| Max CPU Load | %.1f%% |\n", maxCpu.doubleValue() * 100));
        sb.append("| GC Pause Count | ").append(gcCount).append(" |\n");
        sb.append("| Total GC Pause Time | ").append(JfrAnalysisService.display(totalGcPause)).append(" |\n\n");

        if (avgCpu != null && avgCpu.doubleValue() > 0.75 && gcCount > 10) {
            sb.append("**Interpretation:** High CPU load combined with frequent GC pauses suggests memory pressure causing both GC overhead and CPU thrashing.\n\n");
        }
    }

    private static class LockSite {
        final String monitorClass;
        final String topFrame;
        long totalDurationMs;
        long count;

        LockSite(String monitorClass, String topFrame) {
            this.monitorClass = monitorClass;
            this.topFrame = topFrame;
        }

        public long getTotalDurationMs() {
            return totalDurationMs;
        }

        public long getCount() {
            return count;
        }

        public String getMonitorClass() {
            return monitorClass;
        }

        public String getTopFrame() {
            return topFrame;
        }
    }

    private static class IoSite {
        final String eventType;
        final String endpoint;
        final String topFrame;
        long totalDurationMs;
        long count;
        long totalBytes;

        IoSite(String eventType, String endpoint, String topFrame) {
            this.eventType = eventType;
            this.endpoint = endpoint;
            this.topFrame = topFrame;
        }

        public long getTotalDurationMs() {
            return totalDurationMs;
        }
    }

    private record HotMethod(String methodName, long sampleCount) {}
}