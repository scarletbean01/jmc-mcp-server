# JMC MCP Server

A [Model Context Protocol (MCP)](https://modelcontextprotocol.io) server for analyzing Java Flight Recorder (JFR) recordings using the Java Mission Control (JMC) core libraries.

## Features

- **38 MCP tools** for comprehensive JFR analysis:

  **Overview & Discovery**
  - `jfr_overview` — Recording summary, event counts, JVM info
  - `event_schema` — Discover JFR event types and field schemas in a recording
  - `search_events` — Search for any JFR event type with detailed attribute listing
  - `jfr_event_stats` — Provide statistical summaries for specific event types
  - `recording_settings` — Inspect the JFR configuration (events, thresholds, stack traces)
  - `jfr_rules` — Automated bottleneck detection via JMC built-in rules
  - `compare_recordings` — Perform a comprehensive A/B comparison of two JFR recordings

  **Garbage Collection & Memory**
  - `gc_detail` — Detailed GC analysis: per-phase pause breakdowns, GC cause distribution, heap trends, and configuration
  - `gc_analysis` — GC pause times, frequencies, heap trends
  - `heap_trends` — Time-bucketed heap, metaspace, and thread count trends for leak detection
  - `object_statistics` — Heap occupancy by class (instance count and total size)
  - `memory_leaks` — Analyze old object samples to identify potential memory leaks and leaking classes
  - `native_memory` — Provide a memory footprint overview including native libraries and direct buffer limits

  **CPU & Code**
  - `hot_methods` — Top CPU-hot methods from execution samples
  - `cpu_flame` — Provide CPU flame graph data including thread states and hottest call paths
  - `incident_timeline` — Recreate an incident timeline around a specific event or time
  - `jit_compilation` — Analyze JIT compilation and deoptimization events
  - `safepoint_analysis` — Analyze safepoint events and stop-the-world pauses outside of GC
  - `vm_operations` — Analyze "Stop-the-World" events and non-GC VM operations

  **Threading & Locks**
  - `thread_activity` — Analyze thread lifecycle, creation/destruction rates, and sleep patterns
  - `thread_contention` — Monitor blocking, parking, wait times
  - `lock_analysis` — Analyze ThreadPark and Biased Lock Revocation events for advanced lock contention
  - `thread_dumps` — Extract periodic thread dumps from the recording
  - `lock_flame` — Provide lock contention flame graph data

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
  - `time_series` — Analyze performance trends over time using bucketed metrics

  **Live JVM**
  - `live_recording` — Connect to running JVMs via JMX and manage live JFR recordings

- **Powered by JMC 9.1.1** — Rich aggregation, filtering, and the JMC rules engine
- **Built-in caching** — JFR recordings are cached by file path to avoid re-parsing
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
| `compare_recordings` | A/B JFR comparison | `baseline_jfr_path`, `target_jfr_path` | `start_time`, `end_time` |

### Garbage Collection & Memory

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `gc_detail` | In-depth GC analysis | `jfr_file_path` | `start_time`, `end_time`, `detail_level` |
| `gc_analysis` | Basic GC statistics | `jfr_file_path` | `start_time`, `end_time`, `stat_type` |
| `heap_trends` | Heap/metaspace trends | `jfr_file_path` | `start_time`, `end_time`, `bucket_size` |
| `object_statistics` | Heap occupancy | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `memory_leaks` | Old object sampling | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `native_memory` | Native lib & buffer memory | `jfr_file_path` | `start_time`, `end_time` |

### CPU & Code

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `hot_methods` | CPU hotspots | `jfr_file_path` | `start_time`, `end_time`, `thread_name`, `top_n` |
| `cpu_flame` | CPU flame graph paths | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `incident_timeline` | Chronological event context | `jfr_file_path` | `anchor_event`, `anchor_time`, `window_ms` |
| `jit_compilation` | JIT compilation events | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `safepoint_analysis` | Safepoint STW metrics | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `vm_operations` | VM pauses/Safepoints | `jfr_file_path` | `start_time`, `end_time`, `top_n` |

### Threading & Locks

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `thread_activity` | Thread lifecycle stats | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `thread_contention` | Monitor lock contention | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `lock_analysis` | ThreadPark & Biased locks | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `thread_dumps` | Extract thread dumps | `jfr_file_path` | `start_time`, `end_time`, `max_dumps` |
| `lock_flame` | Lock contention flame graph | `jfr_file_path` | `start_time`, `end_time`, `top_n` |

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
| `allocation_flame` | Allocation flame graph | `jfr_file_path` | `start_time`, `end_time`, `top_n` |

### System & Trends

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `system_health` | System metrics | `jfr_file_path` | `start_time`, `end_time` |
| `container_metrics` | Container resource usage | `jfr_file_path` | `start_time`, `end_time` |
| `system_properties` | JVM properties/Env | `jfr_file_path` | `start_time`, `end_time`, `filter` |
| `time_series` | Trend analysis | `jfr_file_path` | `start_time`, `end_time`, `bucket_size`, `metric` |

### Live JVM

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `live_recording` | Live JVM JFR management | `jmx_url`, `action` | `recording_name`, `duration_seconds`, `recording_id`, `output_path` |

## Architecture

```
jmc-mcp/
  JmcMcpServer.java          # Entry point, MCP server bootstrap
  jfr/
    JfrRecordingCache.java   # LRU cache for loaded recordings
    JfrAnalysisService.java  # Core analysis service
    JfrItemUtils.java        # Reflection-free(ish) attribute extraction
  tools/
    SchemaUtil.java          # MCP JSON schema helpers
    *Tool.java               # One per MCP tool
```

## Logging

All logging is directed to **stderr** via logback. stdout is reserved exclusively for MCP JSON-RPC messages.

## License

MIT
