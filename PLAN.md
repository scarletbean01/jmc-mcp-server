# Phase 1 — From Data Reporter to Expert Diagnostician

## Current State

52 tools registered in `JmcMcpServer.java`. The project has excellent coverage across all major JFR event categories. This plan addresses the gap between "reporting raw metrics" and "providing expert-level diagnosis" — the LLM currently must call 10+ individual tools and manually correlate results to understand a single request path.

## Problem Statement

Analysis of real JFR recordings (`before.jfr`, `after.jfr`) revealed that understanding a single `getClientAuthenticate30Response()` request required manually correlating output from 15+ tool calls:

- `hot_methods` → found EC cryptography and JDBC methods but couldn't show the full call chain
- `thread_contention` → found `DBConnPool` and `SOAPProcessorFactory` locks but not what I/O happened under those locks
- `io_hotspots` → found Oracle DB `:1521` chatty reads but not which threads or methods drove them
- `search_events` → could find individual events but not reconstruct request flow

The missing capabilities are:
1. **Request tracing** — reconstructing a single request end-to-end across lock→I/O→CPU phases
2. **Stack trace search** — finding a class/method across all event types with full (non-truncated) traces
3. **Cross-dimensional correlation** — automatically linking locks↔I/O↔hot methods
4. **One-click diagnosis** — running the most impactful analyses in a single call with severity classification
5. **Stack trace diff** — comparing hot methods between recordings at the method level

---

## Tool 1: `stack_trace_search` — Full-Text Stack Trace Search

**Priority:** 🔴 Critical  
**File:** `src/main/java/io/github/deplague/jmcmcp/tools/StackTraceSearchTool.java`  
**Estimated Lines:** ~220

### Gap

The `hot_methods` tool only searches `jdk.ExecutionSample` events and truncates stack traces to 5 frames. When we needed to find `IcpTenantService`, we had to call `hot_methods` with `package_prefix` and still couldn't find it because the class wasn't in the top execution samples. There is no way to search for a class/method pattern across all event types (locks, I/O, exceptions, allocations) with full stack traces.

### JFR Events Searched

All event types that contain a `stackTrace` field:

| Event Type | Purpose |
|------------|---------|
| `jdk.ExecutionSample` | CPU hot spots |
| `jdk.JavaMonitorEnter` | Lock acquisition sites |
| `jdk.JavaMonitorWait` | Lock wait sites |
| `jdk.ThreadPark` | Thread parking sites |
| `jdk.SocketRead` | Network read sites |
| `jdk.SocketWrite` | Network write sites |
| `jdk.FileRead` | File read sites |
| `jdk.FileWrite` | File write sites |
| `jdk.JavaExceptionThrow` | Exception throw sites |
| `jdk.JavaErrorThrow` | Error throw sites |
| `jdk.ObjectAllocationInNewTLAB` | TLAB allocation sites |
| `jdk.ObjectAllocationOutsideTLAB` | Non-TLAB allocation sites |
| `jdk.Compilation` | JIT compilation sites |

### Input Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `jfr_file_path` | string | ✅ | — | Path to JFR recording |
| `class_pattern` | string | ✅ | — | Regex pattern to match against class names in stack traces (e.g., `.*TenantService.*`, `.*DAO.*`) |
| `event_type` | string | ❌ | `all` | Filter to specific event type (e.g., `jdk.JavaMonitorEnter`), or `all` |
| `start_time` | string | ❌ | — | ISO-8601 start time |
| `end_time` | string | ❌ | — | ISO-8601 end time |
| `limit` | integer | ❌ | 20 | Maximum results to return |

### Output Sections

1. **Search Summary** — pattern searched, event types searched, total matches found
2. **Matching Stack Traces** — for each match:
   - Event type
   - Timestamp
   - Thread name
   - **Full stack trace** (all frames, not truncated)
   - Event-specific detail (lock class for monitor events, host:port for socket events, file path for file events, exception message for throw events)
3. **Class Distribution** — table showing which event types contained the most matches, with counts
4. **Agent Hint** — suggests drilling into specific event types or threads, e.g.:
   - "Found 45 matches in `jdk.JavaMonitorEnter`. Consider `thread_contention` for detailed lock analysis."
   - "Found 12 matches in `jdk.SocketRead`. Consider `io_hotspots` for I/O performance details."

### Implementation Details

```java
public final class StackTraceSearchTool {
    private static final String NAME = "stack_trace_search";
    // Event types that have stackTrace field
    private static final List<String> SEARCHABLE_EVENT_TYPES = List.of(
        "jdk.ExecutionSample", "jdk.JavaMonitorEnter", "jdk.JavaMonitorWait",
        "jdk.ThreadPark", "jdk.SocketRead", "jdk.SocketWrite",
        "jdk.FileRead", "jdk.FileWrite", "jdk.JavaExceptionThrow",
        "jdk.JavaErrorThrow", "jdk.ObjectAllocationInNewTLAB",
        "jdk.ObjectAllocationOutsideTLAB", "jdk.Compilation"
    );
    
    // For each event type, define the "detail" fields to extract alongside stack trace
    private static final Map<String, List<String>> EVENT_DETAIL_FIELDS = Map.of(
        "jdk.JavaMonitorEnter", List.of("monitorClass"),
        "jdk.JavaMonitorWait", List.of("monitorClass"),
        "jdk.SocketRead", List.of("host", "port"),
        "jdk.SocketWrite", List.of("host", "port"),
        "jdk.FileRead", List.of("path"),
        "jdk.FileWrite", List.of("path"),
        "jdk.JavaExceptionThrow", List.of("thrownClass", "message"),
        "jdk.JavaErrorThrow", List.of("thrownClass", "message"),
        "jdk.ObjectAllocationInNewTLAB", List.of("objectClass", "tlabSize"),
        "jdk.ObjectAllocationOutsideTLAB", List.of("objectClass", "allocationSize")
    );
}
```

Key implementation notes:
- Use `java.util.regex.Pattern` for `class_pattern` matching against each frame's class name
- For each matching event, extract the full `stackTrace` field using `JfrItemUtils.getAccessor()` and format all frames (not just top 5)
- Create a `formatFullStackTrace(Object stackTrace)` method in `JfrItemUtils` that returns all frames, not just the top N
- Aggregate matches by event type for the "Class Distribution" section
- Follow the existing caching pattern: `service.getCachedResult()` / `service.cacheResult()`

---

## Tool 2: `request_waterfall` — End-to-End Request Tracing

**Priority:** 🔴 Critical  
**File:** `src/main/java/io/github/deplague/jmcmcp/tools/RequestWaterfallTool.java`  
**Estimated Lines:** ~280

### Gap

There is no way to trace a single request end-to-end. The `before.jfr` analysis showed that a single `getClientAuthenticate30Response()` call touches 8-10 DB queries, ECDH crypto, socket I/O to 2 services, and multiple lock acquisitions — but this had to be manually inferred from scattered tool outputs. A waterfall view reconstructs the chronological sequence of events for a specific thread.

### JFR Events Used

| Event | Fields | Purpose |
|-------|--------|---------|
| `jdk.ExecutionSample` | `stackTrace`, `thread`, `startTime` | CPU hot spots on thread |
| `jdk.JavaMonitorEnter` | `stackTrace`, `thread`, `duration`, `monitorClass` | Lock acquisition timing |
| `jdk.JavaMonitorWait` | `stackTrace`, `thread`, `duration`, `monitorClass` | Lock wait timing |
| `jdk.ThreadPark` | `stackTrace`, `thread`, `duration` | Thread parking timing |
| `jdk.SocketRead` | `stackTrace`, `thread`, `duration`, `host`, `port`, `bytesRead` | Network read timing |
| `jdk.SocketWrite` | `stackTrace`, `thread`, `duration`, `host`, `port`, `bytesWritten` | Network write timing |
| `jdk.FileRead` | `stackTrace`, `thread`, `duration`, `path`, `bytesRead` | File read timing |
| `jdk.FileWrite` | `stackTrace`, `thread`, `duration`, `path`, `bytesWritten` | File write timing |
| `jdk.JavaExceptionThrow` | `stackTrace`, `thread`, `message`, `class` | Exceptions on thread |

### Input Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `jfr_file_path` | string | ✅ | — | Path to JFR recording |
| `thread_name` | string | ✅ | — | Exact thread name or regex pattern to match |
| `start_time` | string | ❌ | — | ISO-8601 start time |
| `end_time` | string | ❌ | — | ISO-8601 end time |
| `max_events` | integer | ❌ | 100 | Maximum events in waterfall |

### Output Sections

1. **Thread Summary** — matched thread name(s), total events, time span, event type breakdown
2. **Waterfall Timeline** — chronological table:

| Time | Event Type | Duration | Detail | Top Frame |
|------|-----------|----------|--------|-----------|
| +0ms | MONITOR_ENTER | 42ms | `DBConnPool$ConnectionItem[]` | `OracleDatabaseServices.getConnection()` |
| +42ms | SOCKET_READ | 21ms | `oracle-db:1521` (128B) | `IsxPreparedStatement.executeQuery()` |
| +63ms | MONITOR_ENTER | 15ms | `SOAPProcessorFactory` | `ClientAuthProcessor.process()` |
| +78ms | SOCKET_READ | 53ms | `localhost:9000` (256B) | `CTSChannelServices.authenticate()` |
| +131ms | EXECUTION_SAMPLE | — | — | `JcaECDHAgreement.generateSecret()` |

3. **Phase Breakdown** — aggregated time by phase type:

| Phase | Total Time | % of Recorded | Event Count |
|-------|-----------|----------------|-------------|
| Blocked (Monitor Enter) | 57ms | 43.5% | 2 |
| I/O (Socket Read) | 74ms | 56.5% | 2 |
| CPU (Execution Sample) | — | — | 1 |

4. **Critical Path** — longest sequential chain of blocking events
5. **Agent Hint** — suggests next tool based on dominant phase:
   - If blocked > 40%: "Thread spends most time blocked. Consider `thread_contention` for lock details or `correlate` to see what I/O happens under locks."
   - If I/O > 40%: "Thread spends most time in I/O. Consider `io_hotspots` for endpoint-level analysis."
   - If CPU > 40%: "Thread spends most time on CPU. Consider `hot_methods` with `package_prefix` for application-level hot spots."

### Implementation Details

```java
public final class RequestWaterfallTool {
    private static final String NAME = "request_waterfall";
    
    // Internal record for waterfall events
    private record WaterfallEvent(
        long timeMs,
        String eventType,      // "MONITOR_ENTER", "SOCKET_READ", etc.
        long durationMs,
        String detail,         // lock class, host:port, file path, exception message
        String topFrame,       // top frame of stack trace
        String fullStackTrace  // complete stack trace
    ) {}
    
    // Phase aggregation
    private record PhaseSummary(
        String phaseName,
        long totalTimeMs,
        int eventCount
    ) {}
}
```

Key implementation notes:
- Filter events by thread name (exact match first, then regex fallback)
- Sort all matching events by start time
- For each event, extract: timestamp, event type, duration, event-specific detail, top stack frame
- Classify each event into a phase: `BLOCKED` (monitor enter/wait), `WAITING` (thread park), `IO` (socket/file read/write), `CPU` (execution sample), `EXCEPTION` (throw events)
- Calculate phase totals and percentages
- Identify the "critical path" as the longest sequential chain of blocking events (where each event's start time falls within the previous event's time window)
- The `detail` field is event-type-specific: `monitorClass` for locks, `host:port` for sockets, `path` for files, `message` for exceptions
- Follow existing caching pattern

---

## Tool 3: `correlate` — Cross-Dimensional Correlation Engine

**Priority:** 🟠 High  
**File:** `src/main/java/io/github/deplague/jmcmcp/tools/CorrelateTool.java`  
**Estimated Lines:** ~320

### Gap

Each tool operates in isolation. The LLM must manually correlate "lock contention on `DBConnPool`" with "socket reads on `:1521`" with "hot methods in `IsxPreparedStatement.executeQuery()`". This tool automatically cross-references lock contention sites, I/O hotspots, and hot methods to identify correlated request paths.

### Algorithm

1. **Lock ↔ I/O Correlation**: For each lock contention site, check if the same thread also has socket/file I/O events whose timestamps overlap with the lock hold time. This identifies "blocking I/O under lock" — a critical anti-pattern.

2. **Hot Method ↔ Lock Correlation**: For each hot method, check if it appears in the stack traces of `jdk.JavaMonitorEnter` events. This identifies methods that are on the critical path of lock contention.

3. **Hot Method ↔ I/O Correlation**: For each hot method, check if it appears in the stack traces of `jdk.SocketRead`/`jdk.SocketWrite`/`jdk.FileRead`/`jdk.FileWrite` events. This identifies methods that drive I/O.

4. **Bottleneck Chain**: Construct the longest sequential dependency chain by linking: hot method → lock acquisition → I/O under lock → lock release → next operation.

### Input Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `jfr_file_path` | string | ✅ | — | Path to JFR recording |
| `dimension` | string | ❌ | `all` | Correlation dimension: `lock_io_db`, `cpu_gc`, `all` |
| `start_time` | string | ❌ | — | ISO-8601 start time |
| `end_time` | string | ❌ | — | ISO-8601 end time |
| `top_n` | integer | ❌ | 10 | Number of top results per section |

### Output Sections

#### Section 1: Request Flow Map

Identified end-to-end request paths, reconstructed from correlated data:

```
Path 1: HTTP Handler → lock(DBConnPool) → socketRead(oracle:1521) → lock(SOAPProcessorFactory) → socketRead(localhost:9000)
  Estimated frequency: 45 req/s
  Estimated latency: 131ms (57ms blocked + 74ms I/O)
```

#### Section 2: Lock ↔ I/O Correlation

| Lock | I/O Under Lock | Total Lock Time | I/O Time Under Lock | % I/O Under Lock |
|------|---------------|-----------------|---------------------|------------------|
| `DBConnPool$ConnectionItem[]` | `oracle-db:1521` (read) | 42s | 38s | 90.5% |
| `SOAPProcessorFactory` | `localhost:9000` (read) | 28s | 25s | 89.3% |

**Interpretation:** Locks where >50% of hold time is I/O indicate blocking I/O under lock — a critical anti-pattern. Consider decoupling I/O from synchronized blocks.

#### Section 3: Hot Method ↔ Lock Correlation

| Hot Method | Lock Contentions | Total Contention Time | % of Total Contention |
|------------|-----------------|----------------------|----------------------|
| `OracleDatabaseServices.getConnection()` | `DBConnPool$ConnectionItem[]` | 42s | 31.6% |
| `ClientAuthProcessor.process()` | `SOAPProcessorFactory` | 28s | 21.1% |

#### Section 4: Hot Method ↔ I/O Correlation

| Hot Method | I/O Endpoints | Total I/O Time | % of Total I/O |
|------------|---------------|----------------|----------------|
| `IsxPreparedStatement.executeQuery()` | `oracle-db:1521` | 38s | 62.3% |
| `CTSChannelServices.authenticate()` | `localhost:9000` | 25s | 41.0% |

#### Section 5: Bottleneck Chain

The longest sequential dependency chain identified:

```
1. CPU: JcaECDHAgreement.generateSecret() [hot method]
   ↓ (same thread)
2. BLOCKED: DBConnPool$ConnectionItem[] [42ms avg wait]
   ↓ (acquired, then)
3. I/O: oracle-db:1521 read [21ms avg, 8 queries per request]
   ↓ (released, then)
4. BLOCKED: SOAPProcessorFactory [15ms avg wait]
   ↓ (acquired, then)
5. I/O: localhost:9000 read [53ms avg]
```

#### Section 6: Agent Hint

```
<agent_hint>
Primary bottleneck: DBConnPool lock held during Oracle JDBC reads (90.5% I/O under lock).
Consider: (1) request_waterfall for a specific thread trace, (2) thread_contention for lock details,
(3) io_hotspots for endpoint-level I/O analysis.
</agent_hint>
```

### Implementation Details

```java
public final class CorrelateTool {
    private static final String NAME = "correlate";
    
    // Internal records for correlation
    private record LockSite(String monitorClass, String topFrame, long totalDurationMs, long count) {}
    private record IoSite(String hostPort, String topFrame, long totalDurationMs, long count, long bytes) {}
    private record HotMethod(String methodName, long sampleCount) {}
    private record CorrelatedPath(List<String> steps, long estimatedLatencyMs, long frequency) {}
    
    // Correlation algorithm
    // 1. Extract top N lock contention sites from jdk.JavaMonitorEnter
    // 2. Extract top N I/O hotspots from jdk.SocketRead/Write
    // 3. Extract top N hot methods from jdk.ExecutionSample
    // 4. For each lock site, check if any I/O hotspot's top frame appears in the lock's stack trace
    //    OR if any hot method appears in both the lock's stack trace and an I/O event's stack trace
    // 5. Group correlated items into "request paths"
}
```

Key implementation notes:
- The correlation algorithm works by matching **stack trace frames** across event types
- For Lock ↔ I/O: extract the top N frames from `JavaMonitorEnter` events and check if those frames also appear in `SocketRead`/`SocketWrite` events on the same thread
- For Hot Method ↔ Lock: check if hot method names appear in the stack traces of `JavaMonitorEnter` events
- For Hot Method ↔ I/O: check if hot method names appear in the stack traces of `SocketRead`/`SocketWrite` events
- The "Bottleneck Chain" is constructed by identifying sequential dependencies: if a hot method's frame appears in both a lock event and an I/O event, they're on the same request path
- The `dimension` parameter controls which correlation sections to compute:
  - `lock_io_db`: only Lock ↔ I/O and Hot Method ↔ Lock/↔ I/O
  - `cpu_gc`: hot methods that correlate with GC pause times (simpler — just check if hot methods spike during GC)
  - `all`: all sections
- Follow existing caching pattern

---

## Tool 4: `quick_analysis` — One-Click Overview Dashboard

**Priority:** 🟠 High  
**File:** `src/main/java/io/github/deplague/jmcmcp/tools/QuickAnalysisTool.java`  
**Estimated Lines:** ~200

### Gap

The LLM currently needs 5-6 tool calls to get a basic understanding of a recording. This macro tool runs the most impactful analyses in one call and returns a prioritized summary with severity classification.

### Implementation Strategy

This is a macro tool (like `HighCpuDiagnosticTool`) that orchestrates existing tools:

```java
public final class QuickAnalysisTool {
    private static final String NAME = "quick_analysis";
    
    // Orchestrates: jfr_overview, system_health, hot_methods, gc_analysis, 
    //               thread_contention, io_hotspots, error_analysis
}
```

### Input Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `jfr_file_path` | string | ✅ | — | Path to JFR recording |
| `start_time` | string | ❌ | — | ISO-8601 start time |
| `end_time` | string | ❌ | — | ISO-8601 end time |
| `focus` | string | ❌ | `auto` | Focus area: `cpu`, `memory`, `latency`, `locks`, `auto` |

### Output Sections

1. **Recording Overview** — from `JfrOverviewTool`
2. **System Health** — from `SystemHealthTool`
3. **Severity-Classified Findings** — prioritized list with severity icons:

```
🔴 CRITICAL: CPU utilization 92% (machine total)
🔴 CRITICAL: Lock contention total 130s across all monitors
🟠 HIGH: Oracle DB :1521 socket reads averaging 21ms
🟠 HIGH: 71 old GC pauses (potential memory pressure)
🟡 MEDIUM: SOAPProcessorFactory lock contention 28s
🟡 MEDIUM: JIT compilation storm (467 compilations)
🟢 LOW: Thread pool utilization normal
```

4. **Top Hot Methods** — top 5 from `HotMethodsTool`
5. **Top Lock Contentions** — top 5 from `ThreadContentionTool`
6. **Top I/O Hotspots** — top 5 from `IoHotspotsTool`
7. **GC Summary** — from `GcAnalysisTool`
8. **Top Errors** — top 5 from `ErrorAnalysisTool`
9. **Recommended Next Steps** — agent hint based on findings

### Severity Classification Thresholds

| Severity | CPU | Lock Contention | GC P99 Pause | Heap Usage | I/O Latency |
|----------|-----|----------------|--------------|------------|-------------|
| 🔴 CRITICAL | >90% | >30s total | >500ms | >90% | >500ms avg |
| 🟠 HIGH | >75% | >10s total | >200ms | >75% | >100ms avg |
| 🟡 MEDIUM | >60% | >5s total | >100ms | >60% | >50ms avg |
| 🟢 LOW | ≤60% | ≤5s total | ≤100ms | ≤60% | ≤50ms avg |

### Focus Modes

- `auto` — auto-detect the dominant bottleneck from overview data, then deep-dive
  - If CPU > 75%: include `thread_cpu` and `hot_methods` with higher detail
  - If lock contention > 10s: include `thread_contention` and `lock_analysis` with higher detail
  - If GC P99 > 200ms: include `gc_detail` and `gc_recommendations`
  - If I/O latency > 100ms: include `io_hotspots` and `network_analysis`
- `cpu` — emphasize `thread_cpu`, `hot_methods`, `jit_compilation`
- `memory` — emphasize `gc_analysis`, `heap_trends`, `memory_leaks`, `predictive_leak_analysis`
- `latency` — emphasize `thread_contention`, `io_hotspots`, `request_waterfall` (if available)
- `locks` — emphasize `thread_contention`, `lock_analysis`, `lock_flame`, `deadlock_detection`

### Implementation Details

- Macro tool pattern: instantiate each sub-tool and call its `analyze()` method directly
- Strip the `# Title\n\n` prefix from each sub-tool's output to avoid heading conflicts
- Add `---` separators between sections
- Compute severity classifications from raw metrics extracted during orchestration
- The `focus` parameter controls which sub-tools get `top_n=10` vs `top_n=5`

---

## Tool 5: `diff_stack_traces` — Stack Trace Comparison Between Recordings

**Priority:** 🟡 Medium  
**File:** `src/main/java/io/github/deplague/jmcmcp/tools/DiffStackTracesTool.java`  
**Estimated Lines:** ~200

### Gap

The `compare_recordings` tool compares aggregate metrics (CPU %, GC pause times, etc.) but cannot show which specific methods appeared, disappeared, or changed in prominence between two recordings. This tool provides method-level diff capability.

### JFR Events Used

`jdk.ExecutionSample` from both recordings (the primary source of hot method data).

### Input Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `baseline_jfr_path` | string | ✅ | — | Path to baseline JFR recording |
| `target_jfr_path` | string | ✅ | — | Path to target JFR recording |
| `package_prefix` | string | ❌ | — | Optional package prefix to filter (e.g., `com.mycompany`) |
| `top_n` | integer | ❌ | 20 | Number of top methods per category |

### Output Sections

1. **Recording Context** — baseline and target duration, total samples, samples/second rate
2. **New Methods** — methods in target but not in baseline (appeared after change)

| Method | Target Samples/s | Baseline Samples/s | Change |
|--------|-----------------|-------------------|--------|
| `com.imprivata.auth.FidoDAO.verifyAssertion()` | 45.2 | 0 | NEW |

3. **Disappeared Methods** — methods in baseline but not in target (resolved after change)

| Method | Baseline Samples/s | Target Samples/s | Change |
|--------|-------------------|-----------------|--------|
| `com.imprivata.crypto.OldKDF.deriveKey()` | 12.3 | 0 | REMOVED |

4. **Changed Prominence** — methods with >20% change in sample rate (normalized per second)

| Method | Baseline Samples/s | Target Samples/s | Change | % Change |
|--------|-------------------|-----------------|--------|----------|
| `sun.security.ec.ECDHKeyAgreement.generateSecret()` | 89.1 | 32.4 | -56.7 | -63.6% |
| `oracle.jdbc.driver.OraclePreparedStatement.executeQuery()` | 67.2 | 41.8 | -25.4 | -37.8% |

5. **Stable Methods** — methods with <20% change (top 10 only for brevity)
6. **Agent Hint** — suggests `compare_recordings` for metric-level comparison or `correlate` for deeper analysis

### Implementation Details

```java
public final class DiffStackTracesTool {
    private static final String NAME = "diff_stack_traces";
    
    // Extract hot methods from each recording as Map<String, Long> (method -> sample count)
    // Normalize by recording duration to get samples/second
    // Compute: new methods, disappeared methods, changed (>20% delta), stable (<20% delta)
    
    private record MethodDiff(
        String methodName,
        double baselineRate,    // samples/second in baseline
        double targetRate,      // samples/second in target
        double absoluteChange,  // targetRate - baselineRate
        double percentChange    // (targetRate - baselineRate) / baselineRate * 100
    ) {}
}
```

Key implementation notes:
- Load both recordings independently using `service.loadRecording()`
- Extract execution samples from each, group by top frame method name
- Normalize by recording duration (samples / seconds) to account for different recording lengths
- Use the same `HotMethodsTool` pattern for extracting top frames from `jdk.ExecutionSample`
- The 20% threshold for "changed" is configurable but hardcoded initially
- Handle edge cases: baseline rate of 0 (new method), target rate of 0 (disappeared method)
- Follow existing caching pattern (cache key includes both file paths)

---

## Existing Tool Enhancements

### Enhancement 1: SystemHealthTool — Add `start_time`/`end_time` Parameters

**Priority:** High  
**File:** `src/main/java/io/github/deplague/jmcmcp/tools/SystemHealthTool.java`

**Current State:** The `analyze()` method already accepts `startTimeStr` and `endTimeStr` parameters and passes them to `filterByTimeRange()`. However, the MCP tool schema uses `SchemaUtil.commonJfrProps()` which includes `start_time` and `end_time`, and the call handler extracts them. **Verified: this is already working correctly.** The schema exposes the time range parameters and the handler passes them through.

**Action:** No changes needed. ✅

### Enhancement 2: GcDetailTool — Deeper GC Reference Statistics Breakdown

**Priority:** Medium  
**File:** `src/main/java/io/github/deplague/jmcmcp/tools/GcDetailTool.java`

**Current State:** The `appendReferenceStatistics()` method already exists and processes `jdk.GCReferenceStatistics` events, cross-referenced with `jdk.GCPhasePause` events containing "Reference" in their name. It outputs a table with reference type, count, and processing time.

**Action:** Verify completeness. The current implementation:
- ✅ Groups by reference type (Soft, Weak, Final, Phantom)
- ✅ Matches reference types to GC phase pause times
- ✅ Outputs remaining unmatched phase times
- ⚠️ Missing: per-GC-cycle breakdown showing reference processing time as a percentage of total GC pause time

**Enhancement:** Add a "Reference Processing Overhead" section that shows what percentage of total GC pause time is spent on reference processing:

```java
// After existing reference statistics table
IQuantity totalGcPause = JfrItemUtils.sumQuantity(allGcPauses, JfrAttributes.DURATION.getIdentifier());
IQuantity totalRefPause = // sum of all reference-related phase pauses
double refOverheadPct = totalGcPause != null && totalRefPause != null 
    ? (totalRefPause.doubleValue() / totalGcPause.doubleValue()) * 100 : 0;
sb.append(String.format("\n**Reference Processing Overhead:** %.1f%% of total GC pause time\n", refOverheadPct));
```

### Enhancement 3: SearchEventsTool — Event Type Display Name Resolution

**Priority:** Low  
**File:** `src/main/java/io/github/deplague/jmcmcp/tools/SearchEventsTool.java`

**Current State:** The tool already resolves display names by iterating event types:

```java
String displayName = "Unknown";
for (var iter : allEvents) {
    if (iter.getType().getIdentifier().equals(eventType)) {
        displayName = iter.getType().getName();
        break;
    }
}
```

**Action:** No changes needed. ✅ The display name resolution is already implemented.

### Enhancement 4: ExceptionAnalysisTool — Error-to-Exception Ratio

**Priority:** Low  
**File:** `src/main/java/io/github/deplague/jmcmcp/tools/ExceptionAnalysisTool.java`

**Current State:** The tool already computes and displays the error-to-exception ratio:

```java
if (exceptionCount > 0) {
    sb.append(String.format("- **Error to Exception Ratio:** %.4f%n", (double) errorCount / exceptionCount));
}
```

**Action:** No changes needed. ✅ Already implemented.

---

## Agent Hints Expansion

Add `<agent_hint>` blocks to tools that currently lack them. Only `SystemHealthTool` and `HighCpuDiagnosticTool` have agent hints today.

### HotMethodsTool

Append after the output table:

```
<agent_hint>Top hot method is {topMethod}. Consider `thread_cpu` to see which threads consume the most CPU, `stack_trace_search` with `class_pattern` to find all events involving this class, or `correlate` to see if this method is associated with lock contention or I/O.</agent_hint>
```

### ThreadContentionTool

Append after the output table:

```
<agent_hint>Lock `{topLock}` has {topDuration}s total contention. Consider `correlate` to see if I/O is performed under this lock, or `request_waterfall` with the contending thread name to trace the full request path.</agent_hint>
```

### IoHotspotsTool

Append after the output table:

```
<agent_hint>Top I/O hotspot is {topEndpoint}. Consider `correlate` to see which hot methods and locks are associated with this endpoint, or `network_analysis` for connection-level details.</agent_hint>
```

### GcAnalysisTool

Append after the output table:

```
<agent_hint>GC analysis complete. Consider `gc_detail` for per-phase pause breakdowns, `gc_recommendations` for JVM tuning advice, or `heap_trends` for memory growth patterns.</agent_hint>
```

### MemoryLeaksTool

Append after the output table:

```
<agent_hint>Leak suspects identified. Consider `predictive_leak_analysis` for mathematical leak confirmation and OOM time projection, or `heap_trends` for memory growth visualization.</agent_hint>
```

### LockAnalysisTool

Append after the output table:

```
<agent_hint>Lock contention detected. Consider `correlate` to see if I/O is performed under contended locks (a critical anti-pattern), or `deadlock_detection` to check for deadlock cycles.</agent_hint>
```

### NetworkAnalysisTool

Append after the output table:

```
<agent_hint>Slow connections detected. Consider `request_waterfall` with a specific thread name to trace the full request path, or `io_hotspots` for file I/O analysis.</agent_hint>
```

### CompareRecordingsTool

Append after the output table:

```
<agent_hint>Significant changes detected between recordings. Consider `diff_stack_traces` for method-level comparison showing new, disappeared, and changed methods, or `correlate` for deeper analysis of the target recording.</agent_hint>
```

---

## Registration

**File:** `src/main/java/io/github/deplague/jmcmcp/JmcMcpServer.java`

Add 5 new tool registrations to the `tools` list:

```java
// Phase 1 new tools
new StackTraceSearchTool(analysisService).spec(),
new RequestWaterfallTool(analysisService).spec(),
new CorrelateTool(analysisService).spec(),
new QuickAnalysisTool(analysisService).spec(),
new DiffStackTracesTool(analysisService).spec(),
```

Total tool count after Phase 1: **57** (52 current + 5 new).

---

## JfrItemUtils Enhancement

**File:** `src/main/java/io/github/deplague/jmcmcp/jfr/JfrItemUtils.java`

Add a `formatFullStackTrace()` method that returns all frames (not truncated):

```java
/**
 * Format a stack trace object with all frames (no truncation).
 * Used by stack_trace_search and request_waterfall tools.
 */
public static String formatFullStackTrace(Object stackTrace) {
    return formatStackTrace(stackTrace, Integer.MAX_VALUE);
}
```

This reuses the existing `formatStackTrace(Object, int)` method with `maxFrames = Integer.MAX_VALUE`.

---

## Testing Strategy

### Unit Tests

Each new tool should have a unit test in `src/test/java/io/github/deplague/jmcmcp/tools/`:

| Test File | Tests |
|-----------|-------|
| `StackTraceSearchToolTest.java` | Schema validation, regex matching, event type filtering |
| `RequestWaterfallToolTest.java` | Schema validation, thread matching, phase classification |
| `CorrelateToolTest.java` | Schema validation, correlation algorithm |
| `QuickAnalysisToolTest.java` | Schema validation, macro orchestration |
| `DiffStackTracesToolTest.java` | Schema validation, rate normalization, diff computation |

### Integration Tests

Use `before.jfr` and `after.jfr` as test fixtures:

- `StackTraceSearchTool`: search for `.*DAO.*` — should find `SymantecUserTokenDAO`, `FidoDAO`, etc.
- `RequestWaterfallTool`: trace a Tomcat HTTP handler thread — should show lock→DB→lock→socket sequence
- `CorrelateTool`: should identify `DBConnPool` lock ↔ Oracle DB I/O correlation
- `QuickAnalysisTool`: should produce severity-classified findings for both recordings
- `DiffStackTracesTool`: should show EC crypto methods decreased in `after.jfr`

### Existing Test Pattern

Follow the pattern in `ToolSchemaTest.java` for schema validation and `CompareRecordingsToolTest.java` for integration tests.

---

## Implementation Order

| Step | File | Description | Est. Lines |
|------|------|-------------|------------|
| 1 | `JfrItemUtils.java` | Add `formatFullStackTrace()` method | ~5 |
| 2 | `StackTraceSearchTool.java` | Full-text stack trace search across all event types | ~220 |
| 3 | `RequestWaterfallTool.java` | End-to-end request tracing for a specific thread | ~280 |
| 4 | `CorrelateTool.java` | Cross-dimensional correlation engine | ~320 |
| 5 | `QuickAnalysisTool.java` | One-click overview dashboard macro | ~200 |
| 6 | `DiffStackTracesTool.java` | Method-level diff between two recordings | ~200 |
| 7 | `GcDetailTool.java` | Add reference processing overhead percentage | ~10 |
| 8 | `HotMethodsTool.java` | Add agent hint | ~5 |
| 9 | `ThreadContentionTool.java` | Add agent hint | ~5 |
| 10 | `IoHotspotsTool.java` | Add agent hint | ~5 |
| 11 | `GcAnalysisTool.java` | Add agent hint | ~5 |
| 12 | `MemoryLeaksTool.java` | Add agent hint | ~5 |
| 13 | `LockAnalysisTool.java` | Add agent hint | ~5 |
| 14 | `NetworkAnalysisTool.java` | Add agent hint | ~5 |
| 15 | `CompareRecordingsTool.java` | Add agent hint | ~5 |
| 16 | `JmcMcpServer.java` | Register 5 new tools | ~5 |
| **Total** | | | **~1,255** |

---

## Future Phases (Not in Scope)

### Phase 2: Advanced Diagnostics
- `diagnose` macro tool (generalized from `diagnose_high_cpu`) with `focus=auto|cpu|memory|latency|locks`
- `db_query_analysis` — parse JDBC events to show query patterns, slow queries, connection pool stats
- `thread_pool_health` — dedicated thread pool analysis (Tomcat, ForkJoinPool, ScheduledExecutor)
- Comparison improvements: hot method diff, lock diff, I/O endpoint diff, statistical significance testing

### Phase 3: Enterprise Readiness
- `flame_graph_svg` — generate actual SVG flame graphs (not just text tables)
- `anomaly_detect` — statistical anomaly detection (Z-score on time-series metrics)
- `export_prometheus` — export metrics in Prometheus exposition format
- `jfr_to_json` — convert JFR events to structured JSON for downstream tooling
- Result cache overhaul: TTL, file-change detection, memory-pressure eviction
- JSON output format option alongside Markdown

### Phase 4: Cloud-Native (from PLAN-ENTERPRISE.md)
- Cloud storage streaming (S3/HTTP)
- PII masking / data sanitization
- Kubernetes live attachment (`k8s_dump_jfr`)
- Predictive leak analysis enhancements (already implemented as `predictive_leak_analysis`)
- Automated deadlock detection (already implemented as `deadlock_detection`)
- JDK bug cross-referencing (already implemented as `jdk_bug_reference`)