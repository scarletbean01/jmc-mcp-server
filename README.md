# JMC MCP Server

A [Model Context Protocol (MCP)](https://modelcontextprotocol.io) server for analyzing Java Flight Recorder (JFR) recordings using the Java Mission Control (JMC) core libraries.

## Features

- **61 MCP tools** for comprehensive JFR analysis — from raw metrics to expert-level diagnosis
- **Enterprise-grade security** — path traversal protection, configurable access controls
- **Async execution** — offload long-running analysis to background jobs with polling
- **Smart caching** — TTL-based recording cache with file-change detection and memory-pressure eviction
- **Performance-optimized search** — `stack_trace_search` uses frame-level regex matching instead of expensive full-trace formatting
- **Health monitoring** — built-in health check with JVM metrics, cache stats, and job queue state

### Tool Categories

**Overview & Discovery**
- `jfr_overview` — Recording summary, event counts, JVM info
- `event_schema` — Discover JFR event types and field schemas in a recording
- `search_events` — Search for any JFR event type with detailed attribute listing
- `jfr_event_stats` — Provide statistical summaries for specific event types
- `recording_settings` — Inspect the JFR configuration (events, thresholds, stack traces)
- `jfr_rules` — Automated bottleneck detection via JMC built-in rules
- `compare_recordings` — Perform a comprehensive A/B comparison of two JFR recordings
- `health_check` — Server health, JVM memory, cache stats, and async job queue state

**Garbage Collection & Memory**
- `gc_detail` — Detailed GC analysis: per-phase pause breakdowns, GC cause distribution, heap trends, and configuration
- `gc_analysis` — GC pause times, frequencies, heap trends
- `gc_cause` — Analyze GC causes to understand what triggers garbage collections
- `gc_recommendations` — Analyze GC patterns and provide JVM tuning recommendations
- `heap_trends` — Time-bucketed heap, metaspace, and thread count trends for leak detection
- `object_statistics` — Heap occupancy by class (instance count and total size)
- `memory_leaks` — Analyze old object samples to identify potential memory leaks and leaking classes
- `predictive_leak_analysis` — Mathematically detect memory leaks using linear regression on post-GC heap usage
- `native_memory` — Provide a memory footprint overview including native libraries and direct buffer limits
- `direct_buffers` — Analyze off-heap direct buffer statistics to detect potential memory leaks

**CPU & Code**
- `hot_methods` — Top CPU-hot methods from execution samples
- `thread_cpu` — Identify which threads are consuming the most CPU based on execution samples
- `cpu_flame` — Provide CPU flame graph data including thread states and hottest call paths
- `high_cpu_diagnostic` — Macro tool that orchestrates system health, thread CPU, and hot methods
- `incident_timeline` — Recreate an incident timeline around a specific event or time
- `jit_compilation` — Analyze JIT compilation and deoptimization events
- `code_cache` — Analyze Code Cache usage and JIT compiler statistics
- `safepoint_analysis` — Analyze safepoint events and stop-the-world pauses outside of GC
- `vm_operations` — Analyze "Stop-the-World" events and non-GC VM operations

**Threading & Locks**
- `thread_activity` — Analyze thread lifecycle, creation/destruction rates, and sleep patterns
- `thread_contention` — Monitor blocking, parking, wait times
- `lock_analysis` — Analyze ThreadPark and Biased Lock Revocation events for advanced lock contention
- `thread_dumps` — Extract periodic thread dumps from the recording
- `lock_flame` — Provide lock contention flame graph data
- `deadlock_detection` — Detect thread deadlocks by analyzing monitor ownership and wait-for relationships
- `blocking_summary` — Aggregate all blocking events (monitors, parking, sleeping, I/O) per thread
- `thread_pool_analysis` — Analyze thread pool utilization and detect thread pool starvation

**I/O & Network**
- `io_hotspots` — Identify slow and frequent I/O operations by path/host with call-site breakdowns
- `io_analysis` — File and socket I/O latency/throughput
- `network_analysis` — Socket connection hotspots: per-host:port latency, throughput, and failure tracking

**Errors & Exceptions**
- `error_analysis` — Analyze Java errors (OutOfMemoryError, StackOverflowError, etc.) with severity classification
- `exception_analysis` — Exception/error throw statistics

**Allocation & Classes**
- `allocation_hotspots` — Memory allocation by class and site
- `class_histogram` — Provide a class instance allocation histogram and top allocating classes
- `class_loading` — Analyze class loading events and statistics
- `allocation_flame` — Provide allocation flame graph data

**System & Trends**
- `system_health` — CPU load, physical memory usage, and OS metrics
- `container_metrics` — Analyze container resource limits and usage (Docker/Kubernetes)
- `system_properties` — List JVM system properties and environment variables
- `process_info` — Gather OS version, virtualization details, and running processes context
- `time_series` — Analyze performance trends over time using bucketed metrics
- `jvm_flags` — Analyze JVM runtime flags and ergonomics

**Expert Diagnostics (Phase 1)**
- `stack_trace_search` — Full-text regex search across 13 JFR event types with non-truncated stack traces
- `request_waterfall` — End-to-end request tracing: chronological lock→I/O→CPU→exception sequence per thread
- `correlate` — Cross-dimensional correlation engine linking locks↔I/O↔hot methods with bottleneck chains
- `quick_analysis` — One-click macro dashboard with severity classification and auto-detected dominant bottleneck
- `diff_stack_traces` — Method-level diff between two recordings (new, disappeared, changed prominence)

**Virtual Threads & Advanced**
- `virtual_threads` — Analyze virtual thread pinning sites and execution failures (Java 21+)
- `jdk_bug_reference` — Cross-reference JFR events against known OpenJDK bug signatures

**Live JVM**
- `live_recording` — Connect to running JVMs via JMX and manage live JFR recordings

**Async Infrastructure**
- `get_job_status` — Poll the status of an async analysis job (PENDING, RUNNING, COMPLETED, FAILED, CANCELLED)
- `get_job_result` — Retrieve the result of a completed async analysis job

- **Powered by JMC 9.1.1** — Rich aggregation, filtering, and the JMC rules engine
- **Built-in caching** — JFR recordings cached with TTL, file-change detection, and memory-pressure eviction
- **Stdio transport** — Runs as a subprocess for local CLI/IDE integration

## Requirements

- Java 21 or later
- Maven 3.9+ (for building)

## Build

```bash
mvn clean package
```

The fat JAR is produced at:
```
target/jmc-mcp-1.0.0-SNAPSHOT.jar
```

## Usage with an MCP Client

### Claude Desktop

Add to your `claude_desktop_config.json` (macOS: `~/Library/Application Support/Claude/claude_desktop_config.json`):

```json
{
  "mcpServers": {
    "jmc-mcp": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/jmc-mcp/target/jmc-mcp-1.0.0-SNAPSHOT.jar"]
    }
  }
}
```

### VS Code (with MCP extension)

Add to your `.vscode/mcp.json`:

```json
{
  "servers": {
    "jmc-mcp": {
      "type": "stdio",
      "command": "java",
      "args": ["-jar", "${workspaceFolder}/target/jmc-mcp-1.0.0-SNAPSHOT.jar"]
    }
  }
}
```

## Example Queries

Once connected, ask your agent things like:

- "Analyze the GC behavior in `/path/to/recording.jfr`"
- "What are the top 20 hot methods in this JFR file?"
- "Show me thread contention hotspots from `/tmp/flight.jfr`"
- "Run all JMC rules on `/path/to/recording.jfr` and report any critical issues"
- "Analyze VM operations and safepoint pauses in the recording."
- "Show me the heap occupancy snapshot from the last GC."
- "List all system properties starting with 'java.vm' from the recording."
- "Search for all `jdk.JavaMonitorWait` events in `/path/to/recording.jfr`."
- "Start a 30-second JFR recording on the JVM at `service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi`"
- "Run a quick_analysis on `/path/to/recording.jfr` with focus=memory"
- "Search stack traces for `.*TenantService.*` in `/path/to/recording.jfr`"
- "Trace the request waterfall for thread `http-nio-8080-exec-3`"
- "Correlate locks and I/O in `/path/to/recording.jfr`"
- "Diff stack traces between `baseline.jfr` and `optimized.jfr`"
- "Check the server health status"

## Async Execution

The following heavyweight tools support asynchronous execution via the `async` parameter. When `async: true`, the server immediately returns a job ID and processes the analysis in the background:

```json
{
  "tool": "quick_analysis",
  "arguments": {
    "jfr_file_path": "/path/to/huge-recording.jfr",
    "async": true
  }
}
```

The server responds immediately with a job ID and status:
```markdown
# Async Job Submitted

- **Job ID:** `a1b2c3d4...`
- **Tool:** `quick_analysis`
- **Status:** PENDING

Use `get_job_status` to check progress and `get_job_result` to retrieve the output.
```

**Tools supporting async:** `stack_trace_search`, `quick_analysis`, `correlate`, `request_waterfall`, `diff_stack_traces`, `compare_recordings`, `cpu_flame`, `allocation_flame`, `lock_flame`, `memory_leaks`, `predictive_leak_analysis`, `high_cpu_diagnostic`

Poll for completion:
```json
{ "tool": "get_job_status", "arguments": { "job_id": "a1b2c3d4..." } }
```

Retrieve the result:
```json
{ "tool": "get_job_result", "arguments": { "job_id": "a1b2c3d4..." } }
```

Completed jobs are retained for 1 hour before automatic cleanup.

## Security

### Path Access Control

By default, the server only allows access to JFR files within the current working directory. This prevents path traversal attacks such as:

```
../../../etc/passwd
```

Configure allowed base paths via environment variable:

```bash
export JMC_MCP_ALLOWED_PATHS="/var/jfr,/tmp/recordings,/home/user/jfr"
java -jar target/jmc-mcp-1.0.0-SNAPSHOT.jar
```

Additional URI schemes (e.g., for future cloud storage support):
```bash
export JMC_MCP_ALLOWED_SCHEMES="s3,https"
```

**⚠️ Warning:** To disable validation (not recommended for production):
```bash
export JMC_MCP_DISABLE_PATH_VALIDATION=true
```

### Validation Rules

The access controller enforces:
- **Path traversal detection** — blocks `..`, `%2e%2e`, `//`, and null bytes
- **Base directory allowlist** — rejects paths outside configured roots
- **File size limits** — rejects files larger than 10 GB
- **Scheme allowlisting** — only `file://` by default

## Enterprise Features

### Smart Caching

The recording cache provides production-grade behavior:

| Feature | Behavior |
|---------|----------|
| **TTL** | Recordings expire after 1 hour (configurable) |
| **File-change detection** | If a JFR file is modified after caching, it is reloaded on next access |
| **Memory-pressure eviction** | When heap usage exceeds 85%, the largest cached recording is evicted |
| **Background cleanup** | Expired entries are purged every 5 minutes |
| **Soft references** | Recordings can be reclaimed by GC under heap pressure |

Result caches also have TTL (24 hours) and automatic eviction.

### Health Monitoring

Use `health_check` to monitor server state:

```markdown
# Server Health Check

## Status
- **Overall:** HEALTHY
- **Uptime:** 2h 15m 30s

## JVM Memory
| Region | Used | Committed | Max | Utilization |
|--------|------|-----------|-----|-------------|
| Heap | 1.23 GB | 2.00 GB | 4.00 GB | 30.8% |

## Recording Cache
- **Cached recordings:** 5
- **Cache hits:** 42
- **Cache misses:** 8
- **Evictions:** 2

## Async Job Queue
- **Active jobs:** 1
- **Completed jobs:** 15
- **Failed jobs:** 0
```

## Tool Reference

### Overview & Discovery

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `jfr_overview` | Recording summary | `jfr_file_path` | `start_time`, `end_time` |
| `event_schema` | Event type discovery | `jfr_file_path` | `event_type` |
| `search_events` | Search any event type | `jfr_file_path`, `event_type` | `start_time`, `end_time`, `limit` |
| `jfr_event_stats`| Event type statistics | `jfr_file_path`, `event_type` | `start_time`, `end_time` |
| `recording_settings` | JFR config inspection | `jfr_file_path` | `start_time`, `end_time` |
| `jfr_rules` | Auto bottleneck detection | `jfr_file_path` | `start_time`, `end_time`, `min_severity` |
| `compare_recordings` | A/B JFR comparison | `baseline_jfr_path`, `target_jfr_path` | `start_time`, `end_time`, `async` |
| `health_check` | Server health & metrics | — | — |

### Garbage Collection & Memory

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `gc_detail` | In-depth GC analysis | `jfr_file_path` | `start_time`, `end_time`, `detail_level` |
| `gc_analysis` | Basic GC statistics | `jfr_file_path` | `start_time`, `end_time`, `stat_type` |
| `gc_cause` | GC trigger analysis | `jfr_file_path` | `start_time`, `end_time` |
| `gc_recommendations` | JVM tuning advice | `jfr_file_path` | `start_time`, `end_time` |
| `heap_trends` | Heap/metaspace trends | `jfr_file_path` | `start_time`, `end_time`, `bucket_size` |
| `object_statistics` | Heap occupancy | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `memory_leaks` | Old object sampling | `jfr_file_path` | `start_time`, `end_time`, `top_n`, `async` |
| `predictive_leak_analysis` | Mathematical leak detection | `jfr_file_path` | `start_time`, `end_time`, `r_squared_threshold`, `async` |
| `native_memory` | Native lib & buffer memory | `jfr_file_path` | `start_time`, `end_time` |
| `direct_buffers` | Off-heap buffer stats | `jfr_file_path` | `start_time`, `end_time` |

### CPU & Code

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `hot_methods` | CPU hotspots | `jfr_file_path` | `start_time`, `end_time`, `thread_name`, `package_prefix`, `top_n` |
| `thread_cpu` | Per-thread CPU usage | `jfr_file_path` | `start_time`, `end_time`, `package_prefix`, `top_n` |
| `cpu_flame` | CPU flame graph paths | `jfr_file_path` | `start_time`, `end_time`, `package_prefix`, `top_n`, `async` |
| `high_cpu_diagnostic` | Orchestrated CPU diagnosis | `jfr_file_path` | `start_time`, `end_time`, `package_prefix`, `async` |
| `incident_timeline` | Chronological event context | `jfr_file_path` | `anchor_event`, `anchor_time`, `window_ms` |
| `jit_compilation` | JIT compilation events | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `code_cache` | Code cache stats | `jfr_file_path` | `start_time`, `end_time` |
| `safepoint_analysis` | Safepoint STW metrics | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `vm_operations` | VM pauses/Safepoints | `jfr_file_path` | `start_time`, `end_time`, `top_n` |

### Threading & Locks

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `thread_activity` | Thread lifecycle stats | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `thread_contention` | Monitor lock contention | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `lock_analysis` | ThreadPark & Biased locks | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `thread_dumps` | Extract thread dumps | `jfr_file_path` | `start_time`, `end_time`, `max_dumps` |
| `lock_flame` | Lock contention flame graph | `jfr_file_path` | `start_time`, `end_time`, `top_n`, `async` |
| `deadlock_detection` | Deadlock cycle detection | `jfr_file_path` | `start_time`, `end_time` |
| `blocking_summary` | Aggregate blocking events | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `thread_pool_analysis` | Thread pool utilization | `jfr_file_path` | `start_time`, `end_time`, `top_n` |

### I/O & Network

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `io_hotspots` | I/O operation hotspots | `jfr_file_path` | `start_time`, `end_time`, `io_type`, `top_n` |
| `io_analysis` | I/O latency/throughput | `jfr_file_path` | `start_time`, `end_time`, `io_type` |
| `network_analysis` | Socket connection analysis | `jfr_file_path` | `start_time`, `end_time`, `top_n` |

### Errors & Exceptions

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `error_analysis` | Java error analysis | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `exception_analysis` | Exception stats | `jfr_file_path` | `start_time`, `end_time`, `top_n` |

### Allocation & Classes

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `allocation_hotspots` | Allocation hotspots | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `class_histogram` | Allocation histogram | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `class_loading` | Class loading stats | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `allocation_flame` | Allocation flame graph | `jfr_file_path` | `start_time`, `end_time`, `top_n`, `async` |

### System & Trends

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `system_health` | System metrics | `jfr_file_path` | `start_time`, `end_time` |
| `container_metrics` | Container resource usage | `jfr_file_path` | `start_time`, `end_time` |
| `system_properties` | JVM properties/Env | `jfr_file_path` | `start_time`, `end_time`, `filter` |
| `process_info` | OS & process context | `jfr_file_path` | `start_time`, `end_time` |
| `time_series` | Trend analysis | `jfr_file_path` | `start_time`, `end_time`, `bucket_size`, `metric` |
| `jvm_flags` | JVM flags & ergonomics | `jfr_file_path` | `filter` |

### Expert Diagnostics

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `stack_trace_search` | Regex search across all event stack traces | `jfr_file_path`, `class_pattern` | `event_type`, `start_time`, `end_time`, `limit`, `async` |
| `request_waterfall` | Per-thread chronological event trace | `jfr_file_path`, `thread_name` | `start_time`, `end_time`, `max_events`, `async` |
| `correlate` | Lock↔I/O↔hot-method correlation | `jfr_file_path` | `dimension`, `start_time`, `end_time`, `top_n`, `async` |
| `quick_analysis` | Severity-classified dashboard | `jfr_file_path` | `start_time`, `end_time`, `focus`, `async` |
| `diff_stack_traces` | Method-level diff between recordings | `baseline_jfr_path`, `target_jfr_path` | `package_prefix`, `top_n`, `async` |

### Advanced

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `virtual_threads` | Virtual thread pinning analysis | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `jdk_bug_reference` | Cross-reference JDK bugs | `jfr_file_path` | `start_time`, `end_time` |

### Live JVM

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `live_recording` | Live JVM JFR management | `jmx_url`, `action` | `recording_name`, `duration_seconds`, `recording_id`, `output_path`, `settings` |

### Async Infrastructure

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `get_job_status` | Poll async job status | `job_id` | — |
| `get_job_result` | Retrieve async job result | `job_id` | — |

## Architecture

```
jmc-mcp/
  JmcMcpServer.java              # Entry point, MCP server bootstrap
  security/
    RecordingAccessController.java # Path validation & traversal protection
  async/
    AsyncJobService.java           # Background job execution & polling
    JobRecord.java                 # Immutable job state snapshot
    JobStatus.java                 # Job lifecycle enum
  jfr/
    JfrRecordingCache.java         # Smart cache: TTL, file-change detection, SoftReference
    JfrAnalysisService.java        # Core analysis service with async support
    JfrItemUtils.java              # Reflection-free attribute extraction & stack traces
  tools/
    SchemaUtil.java                # MCP JSON schema helpers
    *Tool.java                     # One per MCP tool (~61 files)
```

## Logging

All logging is directed to **stderr** via logback. stdout is reserved exclusively for MCP JSON-RPC messages.

## License

MIT
