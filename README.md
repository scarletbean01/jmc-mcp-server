# JMC MCP Server

A [Model Context Protocol (MCP)](https://modelcontextprotocol.io) server for analyzing Java Flight Recorder (JFR) recordings using the Java Mission Control (JMC) core libraries.

## Features

- **24 MCP tools** for comprehensive JFR analysis:
  - `jfr_overview` — Recording summary, event counts, JVM info
  - `gc_detail` — Detailed GC analysis: per-phase pause breakdowns, GC cause distribution, heap trends, and configuration
  - `gc_analysis` — GC pause times, frequencies, heap trends
  - `io_hotspots` — Identify slow and frequent I/O operations by path/host with call-site breakdowns
  - `safepoint_analysis` — Analyze safepoint events and stop-the-world pauses outside of GC
  - `thread_activity` — Analyze thread lifecycle, creation/destruction rates, and sleep patterns
  - `hot_methods` — Top CPU-hot methods from execution samples
  - `thread_contention` — Monitor blocking, parking, wait times
  - `allocation_hotspots` — Memory allocation by class and site
  - `io_analysis` — File and socket I/O latency/throughput
  - `exception_analysis` — Exception/error throw statistics
  - `jfr_rules` — Automated bottleneck detection via JMC built-in rules
  - `live_recording` — Connect to running JVMs via JMX and manage live JFR recordings
  - `system_health` — CPU load, physical memory usage, and OS metrics
  - `thread_dumps` — Extract periodic thread dumps from the recording
  - `search_events` — Search for any JFR event type with detailed attribute listing
  - `vm_operations` — Analyze "Stop-the-World" events and non-GC VM operations
  - `object_statistics` — Heap occupancy by class (instance count and total size)
  - `system_properties` — List JVM system properties and environment variables
  - `recording_settings` — Inspect the JFR configuration (events, thresholds, stack traces)
  - `time_series` — Analyze performance trends over time using bucketed metrics
  - `jit_compilation` — Analyze JIT compilation and deoptimization events
  - `class_loading` — Analyze class loading events and statistics
  - `compare_recordings` — Perform a comprehensive A/B comparison of two JFR recordings

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

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `jfr_overview` | Recording summary | `jfr_file_path` | `start_time`, `end_time` |
| `gc_detail` | In-depth GC analysis | `jfr_file_path` | `start_time`, `end_time`, `detail_level` |
| `gc_analysis` | Basic GC statistics | `jfr_file_path` | `start_time`, `end_time`, `stat_type` |
| `io_hotspots` | I/O operation hotspots | `jfr_file_path` | `start_time`, `end_time`, `io_type`, `top_n` |
| `safepoint_analysis` | Safepoint STW metrics | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `thread_activity` | Thread lifecycle stats | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `hot_methods` | CPU hotspots | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `thread_contention` | Lock contention | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `allocation_hotspots` | Allocation hotspots | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `io_analysis` | I/O latency/throughput | `jfr_file_path` | `start_time`, `end_time`, `io_type` |
| `exception_analysis` | Exception stats | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `jfr_rules` | Auto bottleneck detection | `jfr_file_path` | `start_time`, `end_time`, `min_severity` |
| `live_recording` | Live JVM JFR management | `jmx_url`, `action` | `recording_name`, `duration_seconds`, `recording_id`, `output_path` |
| `system_health` | System metrics | `jfr_file_path` | `start_time`, `end_time` |
| `thread_dumps` | Extract thread dumps | `jfr_file_path` | `start_time`, `end_time`, `max_dumps` |
| `search_events` | Search any event type | `jfr_file_path`, `event_type` | `start_time`, `end_time`, `limit` |
| `vm_operations` | VM pauses/Safepoints | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `object_statistics` | Heap occupancy | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `system_properties` | JVM properties/Env | `jfr_file_path` | `start_time`, `end_time`, `filter` |
| `recording_settings` | JFR config inspection | `jfr_file_path` | `start_time`, `end_time` |
| `time_series` | Trend analysis | `jfr_file_path` | `start_time`, `end_time`, `bucket_size` |
| `jit_compilation` | JIT compilation events | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `class_loading` | Class loading stats | `jfr_file_path` | `start_time`, `end_time`, `top_n` |
| `compare_recordings`| A/B JFR Comparison | `baseline_jfr_path`, `target_jfr_path` | `start_time`, `end_time` |

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
