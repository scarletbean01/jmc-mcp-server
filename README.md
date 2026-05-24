# JMC MCP Server

A [Model Context Protocol (MCP)](https://modelcontextprotocol.io) server for analyzing Java Flight Recorder (JFR) recordings using the Java Mission Control (JMC) core libraries.

## Features

- **9 MCP tools** for comprehensive JFR analysis:
  - `jfr_overview` — Recording summary, event counts, JVM info
  - `gc_analysis` — GC pause times, frequencies, heap trends
  - `hot_methods` — Top CPU-hot methods from execution samples
  - `thread_contention` — Monitor blocking, parking, wait times
  - `allocation_hotspots` — Memory allocation by class and site
  - `io_analysis` — File and socket I/O latency/throughput
  - `exception_analysis` — Exception/error throw statistics
  - `jfr_rules` — Automated bottleneck detection via JMC built-in rules
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
- "Start a 30-second JFR recording on the JVM at `service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi`"

## Tool Reference

| Tool | Description | Required Args | Optional Args |
|------|-------------|---------------|---------------|
| `jfr_overview` | Recording summary | `jfr_file_path` | — |
| `gc_analysis` | GC statistics | `jfr_file_path` | `stat_type` (all/pause_times/frequencies/heap_summary) |
| `hot_methods` | CPU hotspots | `jfr_file_path` | `top_n` (default 10) |
| `thread_contention` | Lock contention | `jfr_file_path` | `top_n` (default 10) |
| `allocation_hotspots` | Allocation hotspots | `jfr_file_path` | `top_n` (default 10) |
| `io_analysis` | I/O analysis | `jfr_file_path` | `io_type` (all/file/socket) |
| `exception_analysis` | Exception stats | `jfr_file_path` | `top_n` (default 10) |
| `jfr_rules` | Auto bottleneck detection | `jfr_file_path` | `min_score` (default 50) |
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
