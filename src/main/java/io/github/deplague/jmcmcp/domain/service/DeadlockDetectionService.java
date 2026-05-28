package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.DeadlockCycle;
import io.github.deplague.jmcmcp.domain.model.DeadlockDetectionResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for deadlock detection.
 */
@Slf4j
public final class DeadlockDetectionService {

    public DeadlockDetectionResult analyze(IItemCollection events) {
        IItemCollection threadDumps = events.apply(ItemFilters.type("jdk.ThreadDump"));

        if (!threadDumps.hasItems()) {
            IItemCollection monitorEnter = events.apply(ItemFilters.type("jdk.JavaMonitorEnter"));
            IItemCollection monitorWait = events.apply(ItemFilters.type("jdk.JavaMonitorWait"));
            return new DeadlockDetectionResult(
                    List.of(),
                    0, 0, 0, 0,
                    JfrItemUtils.count(monitorEnter),
                    JfrItemUtils.count(monitorWait),
                    false,
                    true
            );
        }

        ThreadDumpData latestDump = extractLatestThreadDump(threadDumps);
        if (latestDump == null) {
            return new DeadlockDetectionResult(
                    List.of(), 0, 0, 0, 0, 0, 0, true, false
            );
        }

        List<DeadlockCycle> deadlocks = detectDeadlocks(latestDump);

        int threadsWithLocks = 0;
        int threadsWaiting = 0;
        int totalMonitors = 0;
        for (ThreadInfo t : latestDump.threads) {
            if (!t.heldMonitors.isEmpty()) {
                threadsWithLocks++;
                totalMonitors += t.heldMonitors.size();
            }
            if (t.waitingFor != null) threadsWaiting++;
        }

        return new DeadlockDetectionResult(
                deadlocks,
                latestDump.threads.size(),
                threadsWithLocks,
                threadsWaiting,
                totalMonitors,
                0,
                0,
                true,
                true
        );
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
}
