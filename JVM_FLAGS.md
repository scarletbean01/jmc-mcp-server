# JVM Tuning Flags for JMC MCP Server

This document lists recommended JVM launch flags for running the JMC MCP Server in production.

## Recommended Launch Command

```bash
java \
  -Xms4g -Xmx16g \
  -XX:MaxMetaspaceSize=512m \
  -XX:MaxDirectMemorySize=2g \
  -XX:ReservedCodeCacheSize=512m \
  -XX:+UseCodeCacheFlushing \
  -XX:+UseZGC \
  -XX:+ZGenerational \
  -XX:MaxGCPauseMillis=10 \
  -XX:+UseNUMA \
  -XX:+AlwaysPreTouch \
  -XX:+UseTransparentHugePages \
  -XX:Tier3CompileThreshold=1000 \
  -XX:Tier4CompileThreshold=5000 \
  -Djdk.virtualThreadScheduler.parallelism=32 \
  -Djdk.virtualThreadScheduler.maxPoolSize=256 \
  -jar target/jmc-mcp-server-1.0-SNAPSHOT.jar
```

## Flag Breakdown

### Heap Sizing
- `-Xms4g -Xmx16g` — Set min/max heap. JFR `IItemCollection` objects can retain ~4× file size; 16GB provides safe headroom for concurrent analyses.
- `-XX:MaxMetaspaceSize=512m` — Cap metaspace growth.
- `-XX:MaxDirectMemorySize=2g` — Headroom for NIO file uploads and SSE streams.
- `-XX:ReservedCodeCacheSize=512m` — Avoid C2 compiler stalls on large codebases.
- `-XX:+UseCodeCacheFlushing` — Allow eviction of cold compiled code.

### Garbage Collector (ZGC)
- `-XX:+UseZGC` — Low-latency GC ideal for heap-heavy workloads.
- `-XX:+ZGenerational` — Generational ZGC (Java 25) improves throughput.
- `-XX:MaxGCPauseMillis=10` — Target sub-10ms pauses.

### JIT & Memory Optimizations
- `-XX:+UseNUMA` — Optimize memory allocation on NUMA systems.
- `-XX:+AlwaysPreTouch` — Pre-touch heap pages to avoid runtime page faults.
- `-XX:+UseTransparentHugePages` — Reduce TLB misses.
- `-XX:Tier3CompileThreshold=1000` / `-XX:Tier4CompileThreshold=5000` — Faster C2 warmup for hot analysis paths.

### Virtual Threads
- `-Djdk.virtualThreadScheduler.parallelism=32` — Carrier thread parallelism (default = available processors).
- `-Djdk.virtualThreadScheduler.maxPoolSize=256` — Max carrier threads before overflow.

### Alternative: G1GC
If ZGC is unavailable, use G1GC with these flags:
```bash
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=16m
-XX:+UseStringDeduplication
-XX:+UseLargePages
```

## Development / Diagnostics

Enable pinning diagnostics (do NOT use in production):
```bash
-Djdk.tracePinnedThreads=full
```
