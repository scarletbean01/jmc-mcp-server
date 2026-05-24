# Tier 6 — Final Profiling Gap Analysis

## Current State

44 tools, 55 JFR event types covered. The project has excellent coverage across all major profiling areas. This analysis identifies the last remaining gaps for comprehensive production profiling.

### Coverage Assessment

| Area | Tools | Events | Assessment |
|---|---|---|---|
| GC & Memory | `gc_detail`, `gc_analysis`, `heap_trends`, `object_statistics`, `memory_leaks`, `native_memory` | 12+ | ✅ Strong |
| CPU & Code | `hot_methods`, `cpu_flame`, `incident_timeline`, `jit_compilation`, `safepoint_analysis`, `vm_operations` | 7 | ✅ Strong |
| Threading & Locks | `thread_activity`, `thread_contention`, `lock_analysis`, `lock_flame`, `thread_dumps` | 10 | ✅ Strong |
| I/O & Network | `io_hotspots`, `io_analysis`, `network_analysis` | 5 | ✅ Strong |
| Errors & Exceptions | `error_analysis`, `exception_analysis` | 2 | ✅ Adequate |
| Allocation & Classes | `allocation_hotspots`, `class_histogram`, `class_loading`, `allocation_flame` | 4 | ✅ Strong |
| System & Container | `system_health`, `container_metrics`, `system_properties`, `time_series` | 7 | ✅ Strong |
| Overview & Discovery | `jfr_overview`, `event_schema`, `search_events`, `jfr_event_stats`, `recording_settings`, `jfr_rules`, `compare_recordings` | Dynamic | ✅ Strong |
| Per-Thread Analysis | `thread_cpu`, `blocking_summary`, `thread_allocation` | 9+ | ✅ Strong |
| Virtual Threads | `virtual_threads` | 3 | ✅ Strong |
| GC Deep Dive | `gc_cause` | 2 | ✅ Adequate |
| JIT Internals | `code_cache` | 2 | ✅ Adequate |

### Remaining Gaps

| Priority | Gap | Why It Matters |
|---|---|---|
| 🟡 High | No JVM flags/ergonomics tool | JVM tuning decisions require knowing runtime flag values (`UseG1GC`, `CompileThreshold`, etc.). `system_properties` only shows `-D` properties, not `-XX:` flags. |
| 🟡 High | No direct buffer / off-heap tool | Off-heap memory leaks via `ByteBuffer.allocateDirect()` are notoriously hard to diagnose. `native_memory` only shows system property limits, not actual direct buffer pool usage. |
| 🟠 Medium | No process/environment tool | OS version, CPU count, virtualization type (KVM/VMware/container) is essential context for performance analysis. Currently scattered across `jfr_overview` and `system_health`. |

---

## Tool 22: `JvmFlagsTool` — JVM Runtime Flags & Ergonomics (HIGH)

**Name:** `jvm_flags`

**Gap:** When analyzing a performance problem, knowing the actual runtime GC algorithm (`UseG1GC` vs `UseZGC`), heap sizing heuristics (`MinHeapFreeRatio`, `MaxHeapFreeRatio`), and compiler thresholds (`CompileThreshold`) is essential context. `system_properties` only shows `-D` system properties, not `-XX:` JVM flags. JFR emits flag events at recording start that capture all runtime flag values.

**JFR Events:**

| Event | Fields Used | Purpose |
|-------|------------|---------|
| `jdk.IntFlag` | `name`, `value` | Integer JVM flags (e.g., `CompileThreshold`) |
| `jdk.UintFlag` | `name`, `value` | Unsigned integer JVM flags |
| `jdk.DoubleFlag` | `name`, `value` | Double JVM flags (e.g., `G1HeapWastePercent`) |
| `jdk.BooleanFlag` | `name`, `value` | Boolean JVM flags (e.g., `UseG1GC`, `UseCompressedOops`) |

**Input Parameters:**

- `jfr_file_path` (required)
- `filter` (optional) — substring filter for flag names (e.g., "GC", "Compile", "Heap")

**Output Sections:**

1. **GC Configuration** — All flags containing "GC" or "Heap" in their name, grouped by relevance
2. **Compiler Flags** — All flags containing "Compile" or "Tiered" in their name
3. **Memory Flags** — All flags containing "Memory", "Heap", "Metaspace", or "Compressed" in their name
4. **All Flags** (if no filter) or **Filtered Flags** — Table: flag name, value, type

**Key Implementation Details:**

- Iterate all 4 flag event types, collect name/value pairs
- Group by type (boolean, int, uint, double) for display
- If `filter` is provided, only show flags whose name contains the filter string (case-insensitive)
- Sort alphabetically within each section
- No `start_time`/`end_time` needed — flags are emitted once at recording start

**Estimated Lines:** ~120

---

## Tool 23: `DirectBuffersTool` — Off-Heap Buffer Statistics (HIGH)

**Name:** `direct_buffers`

**Gap:** Direct buffer (off-heap `ByteBuffer.allocateDirect()`) leaks are a real production issue. `native_memory` shows system property limits (`MaxDirectMemorySize`) but not actual pool usage. `jdk.DirectBufferStatistics` provides the actual count, total capacity, and memory used by direct buffers over time.

**JFR Events:**

| Event | Fields Used | Purpose |
|-------|------------|---------|
| `jdk.DirectBufferStatistics` | `directBufferCount`, `directTotalCapacity`, `directMemoryUsed` | Direct buffer pool usage |

**Input Parameters:**

- `jfr_file_path` (required)
- `start_time` (optional)
- `end_time` (optional)

**Output Sections:**

1. **Direct Buffer Summary** — Current/max buffer count, total capacity, memory used
2. **Direct Buffer Trend** — Time-bucketed table: time, buffer count, total capacity, memory used (if multiple samples)
3. **Memory Pressure Warning** — If direct memory used approaches `MaxDirectMemorySize` (from `jdk.InitialSystemProperty`), flag risk

**Key Implementation Details:**

- Use `JfrItemUtils.getAccessor()` for `directBufferCount`, `directTotalCapacity`, `directMemoryUsed`
- Use `JfrItemUtils.avgQuantity/maxQuantity/minQuantity` for summary statistics
- If multiple samples exist, use time-bucketing pattern from `HeapTrendsTool`
- Cross-reference `jdk.InitialSystemProperty` for `sun.nio.MaxDirectMemorySize` to compute utilization %
- Use `SchemaUtil.formatBytes()` for byte formatting

**Estimated Lines:** ~100

---

## Tool 24: `ProcessInfoTool` — OS & Environment Context (MEDIUM)

**Name:** `process_info`

**Gap:** Knowing the OS version, CPU architecture, virtualization type (KVM/VMware/container), and what processes are running alongside the JVM is valuable diagnostic context. This information is currently scattered across `jfr_overview` and `system_health` or not available at all. A consolidated tool provides a single place to check the runtime environment.

**JFR Events:**

| Event | Fields Used | Purpose |
|-------|------------|---------|
| `jdk.OSInformation` | `osVersion`, `osName`, `osArch` | Operating system details |
| `jdk.VirtualizationInformation` | `virtualizationTechnology` | Whether running in a VM/container |
| `jdk.SystemProcess` | `pid`, `commandLine` | Running processes at recording start |

**Input Parameters:**

- `jfr_file_path` (required)

**Output Sections:**

1. **Operating System** — OS name, version, architecture
2. **Virtualization** — Virtualization technology (if detected), or "bare metal" / "unknown"
3. **Running Processes** — Table: PID, command line (limited to top 50 for readability)

**Key Implementation Details:**

- These events are emitted once at recording start, so no time-range filtering needed
- Use `JfrItemUtils.getMember()` for field extraction
- `jdk.VirtualizationInformation` may not be present in all recordings — handle gracefully
- `jdk.SystemProcess` can have many entries — limit to top 50
- No `start_time`/`end_time` needed — environment events are point-in-time at recording start

**Estimated Lines:** ~80

---

## Not Recommended (Too Niche)

| Tool | Events | Why Not |
|---|---|---|
| Module events | `jdk.ModuleExport`, `jdk.ModuleRequire` | Only useful for JPMS debugging, not general profiling |
| Security events | `jdk.X509Certificate`, `jdk.TLSHandshake`, `jdk.SecurityPropertyModification` | TLS/certificate analysis is a different domain |
| GC reference processing | `jdk.GCReferenceStatistics` (deeper) | Already covered in `gc_detail`; diminishing returns for a separate tool |
| Thread stack depth | `jdk.ThreadStack` | Redundant with `thread_dumps` |

---

## Implementation Order

| Step | File | Description |
|------|------|-------------|
| 1 | `JvmFlagsTool.java` | JVM runtime flags — essential for tuning context |
| 2 | `DirectBuffersTool.java` | Off-heap buffer statistics — direct buffer leak detection |
| 3 | `ProcessInfoTool.java` | OS & environment context — diagnostic context consolidation |
| 4 | `JmcMcpServer.java` | Register all 3 new tools |

---

## New JFR Event Coverage

| New Event Type | Tool |
|----------------|------|
| `jdk.IntFlag` | `jvm_flags` |
| `jdk.UintFlag` | `jvm_flags` |
| `jdk.DoubleFlag` | `jvm_flags` |
| `jdk.BooleanFlag` | `jvm_flags` |
| `jdk.DirectBufferStatistics` | `direct_buffers` |
| `jdk.OSInformation` | `process_info` |
| `jdk.VirtualizationInformation` | `process_info` |
| `jdk.SystemProcess` | `process_info` |

Total event types after Tier 6: **~63**. Total tool count: **47** (44 current + 3 Tier 6).

**Beyond Tier 6, the tool set covers virtually every critical JFR event for production profiling.** The remaining events are either too niche (module exports, TLS handshakes) or already adequately covered by existing tools.

---

## Existing Tool Enhancements (Backlog)

| Tool | Enhancement | Priority |
|------|-------------|----------|
| `SystemHealthTool` | Add `start_time`/`end_time` params (currently missing) | High |
| `GcDetailTool` | Add deeper `jdk.GCReferenceStatistics` breakdown per reference type | Medium |
| `SearchEventsTool` | Add `event_type` display name resolution (use `event_schema` data) | Low |
| `ExceptionAnalysisTool` | Add error-to-exception ratio (cross-reference `jdk.JavaErrorThrow`) | Low |