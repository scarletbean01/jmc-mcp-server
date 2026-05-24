# Tier 4 — Critical Profiling Gap Analysis

## Current Coverage

42 JFR event types covered across 28 tools. Analysis of production profiling workflows reveals 3 critical gaps and 2 medium-priority enhancements.

### Critical Gaps

| Profiling Area | Current Tools | Events Covered | Gap |
|---|---|---|---|
| Memory Leak Detection | `heap_trends`, `object_statistics` | `jdk.GCHeapSummary`, `jdk.ObjectCount` | No `jdk.OldObjectSample` — can't identify which objects are leaking |
| Lock Contention | `thread_contention` | `jdk.JavaMonitorEnter`, `jdk.JavaMonitorWait` | Missing `jdk.ThreadPark` (LockSupport.park — CompletableFuture, conditions), `jdk.BiasedLockRevocation` (revocation storms) |
| Container Awareness | `system_health` | `jdk.CPULoad`, `jdk.PhysicalMemory` | Missing `jdk.ContainerConfiguration`, `jdk.ContainerCPUUsage`, `jdk.ContainerMemoryUsage` — most production JVMs run in containers |

---

## Tool 13: `MemoryLeaksTool` — Old Object Sampling (CRITICAL)

**Name:** `memory_leaks`

**Gap:** `heap_trends` shows memory growing, `object_statistics` shows what's on-heap at GC time, but neither tells you *which specific objects are surviving and potentially leaking*. `jdk.OldObjectSample` is JFR's purpose-built event for this — it samples objects that have survived multiple GC cycles, giving you the actual leaking object references, their age, and their allocation stack traces.

**JFR Events:**

| Event | Fields Used | Purpose |
|-------|------------|---------|
| `jdk.OldObjectSample` | `objectClass`, `age`, `description`, `stackTrace` | Sampled objects surviving multiple GCs |

**Input Parameters:**

- `jfr_file_path` (required)
- `start_time` (optional)
- `end_time` (optional)
- `top_n` (optional, default 20)

**Output Sections:**

1. **Leak Summary** — Total sampled objects, total retained bytes
2. **Top Leaking Classes** — Table: class name, object count, total retained bytes, avg age (GC generations survived)
3. **Top Leak Allocation Sites** — Table: stack trace, object count, total bytes
4. **Oldest Surviving Objects** — Table: object class, age (GC cycles), description

**Estimated Lines:** ~140

---

## Tool 14: `LockAnalysisTool` — Deep Lock & Contention Analysis

**Name:** `lock_analysis`

**Gap:** `thread_contention` only covers `jdk.JavaMonitorEnter` and `jdk.JavaMonitorWait`. Modern Java uses `LockSupport.park()` heavily (CompletableFuture, StampedLock, ReentrantLock, conditions), which emits `jdk.ThreadPark`. Biased lock revocation (`jdk.BiasedLockRevocation`) causes stop-the-world safepoints that tank throughput under contention — a well-known production issue pattern that's invisible without this event.

**JFR Events:**

| Event | Fields Used | Purpose |
|-------|------------|---------|
| `jdk.ThreadPark` | `stackTrace`, `duration` | LockSupport.park() wait times and call sites |
| `jdk.BiasedLockRevocation` | `lockClass`, `stackTrace` | Single biased lock revocation events |
| `jdk.BiasedLockClassRevocation` | `revokedClass` | Bulk/class-level biased lock revocation |
| `jdk.BiasedLockSelfRevocation` | `lockClass` | Self-revocation of biased locks |

**Input Parameters:**

- `jfr_file_path` (required)
- `start_time` (optional)
- `end_time` (optional)
- `top_n` (optional, default 10)

**Output Sections:**

1. **Park Summary** — Total park events, avg/max park duration, top park reasons
2. **Top Park Sites** — Table: stack trace, count, avg duration, max duration
3. **Biased Lock Revocations** — Table: lock class, revocation count, revocation type (single/class/bulk)
4. **Revocation Hotspots** — Table: lock class + stack trace, count

**Estimated Lines:** ~160

---

## Tool 15: `ContainerMetricsTool` — Container Resource Limits & Usage

**Name:** `container_metrics`

**Gap:** Most production JVMs run in Docker/Kubernetes. `system_health` shows host-level CPU/memory, but inside a container the JVM sees container limits, not host resources. Without `jdk.ContainerConfiguration`/`jdk.ContainerCPUUsage`/`jdk.ContainerMemoryUsage`, you can't tell if the container is throttled, near its memory limit, or misconfigured.

**JFR Events:**

| Event | Fields Used | Purpose |
|-------|------------|---------|
| `jdk.ContainerConfiguration` | `cpuShares`, `cpuPeriod`, `cpuQuota`, `memoryLimit`, `swapLimit`, `oomKillDisable` | Container resource limits |
| `jdk.ContainerCPUUsage` | `cpuTime`, `cpuTimeSystem` | Effective CPU usage vs. container quota |
| `jdk.ContainerMemoryUsage` | `memoryUsage`, `memoryAndSwapUsage` | Memory usage vs. container memory limit |

**Input Parameters:**

- `jfr_file_path` (required)
- `start_time` (optional)
- `end_time` (optional)

**Output Sections:**

1. **Container Configuration** — CPU shares/period/quota, memory limit, swap limit, OOM kill disable
2. **CPU Usage Trend** — Effective CPU usage vs. container CPU quota (time-bucketed)
3. **Memory Usage Trend** — Memory usage vs. container memory limit (time-bucketed)
4. If no container events found, note that JVM is likely running without container awareness

**Estimated Lines:** ~130

---

## Existing Tool Enhancements (Updated)

| Tool | Enhancement | Priority |
|------|-------------|----------|
| `SystemHealthTool` | Add `start_time`/`end_time` params (currently missing) | High |
| `SystemHealthTool` | Add container metrics fallback — if `jdk.ContainerConfiguration` events exist, show container limits alongside host metrics | Medium |
| `GcDetailTool` | Add `jdk.GCReferenceStatistics` — show reference processing impact on GC pause times | Medium |
| `SearchEventsTool` | Add `event_type` display name resolution (use `event_schema` data) | Low |
| `TimeSeriesTool` | Add `metric` filter param (cpu/gc/alloc/all) | Low |
| `ExceptionAnalysisTool` | Add error-to-exception ratio (cross-reference `jdk.JavaErrorThrow`) | Low |

---

## Implementation Order

| Step | File | Description |
|------|------|-------------|
| 1 | `MemoryLeaksTool.java` | Old object sampling — #1 production memory leak root cause |
| 2 | `LockAnalysisTool.java` | Thread park + biased lock revocation analysis |
| 3 | `ContainerMetricsTool.java` | Container resource limits and usage trends |
| 4 | `JmcMcpServer.java` | Register all 3 new tools |

---

## New JFR Event Coverage

| New Event Type | Tool |
|----------------|------|
| `jdk.OldObjectSample` | `memory_leaks` |
| `jdk.ThreadPark` | `lock_analysis` |
| `jdk.BiasedLockRevocation` | `lock_analysis` |
| `jdk.BiasedLockClassRevocation` | `lock_analysis` |
| `jdk.BiasedLockSelfRevocation` | `lock_analysis` |
| `jdk.ContainerConfiguration` | `container_metrics` |
| `jdk.ContainerCPUUsage` | `container_metrics` |
| `jdk.ContainerMemoryUsage` | `container_metrics` |

## Completed: Tier 4 Tools (3 tools)

| Tool | Name | Status |
|------|------|--------|
| `MemoryLeaksTool` | `memory_leaks` | ✅ Done |
| `LockAnalysisTool` | `lock_analysis` | ✅ Done |
| `ContainerMetricsTool` | `container_metrics` | ✅ Done |

Current tool count: **35** (20 original + 4 Tier 1 + 4 Tier 2 + 4 Tier 3 + 3 Tier 4)

**Note:** Container events (`jdk.ContainerConfiguration`, `jdk.ContainerCPUUsage`, `jdk.ContainerMemoryUsage`) and biased lock events (`jdk.BiasedLockRevocation`, `jdk.BiasedLockClassRevocation`, `jdk.BiasedLockSelfRevocation`) may require specific JFR configuration settings to be enabled. The tools should gracefully handle their absence with informative messages.