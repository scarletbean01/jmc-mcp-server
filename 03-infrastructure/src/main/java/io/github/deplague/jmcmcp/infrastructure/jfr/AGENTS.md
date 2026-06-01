# Package: io.github.deplague.jmcmcp.infrastructure.jfr

This package contains the high-performance implementation for JFR parsing and caching.

## Key Classes
- **`JfrRecordingCache`**: Enterprise-grade Caffeine cache for `IItemCollection`. Offloads CPU-bound parsing to a dedicated platform thread pool to avoid pinning Virtual Thread carriers.
- **`JfrProviderImpl`**: Implements the `JfrProvider` port, integrating caching and recording access.
- **`JfrAccessorRepository`**: Provides high-performance attribute accessors to minimize reflection/lookup during event processing.
- **`JfrQuantityAggregator`**: Helper for statistical computations (min, max, avg, percentiles) on JFR quantities.
- **`CallTreeCache`**: Specialized cache for interactive call tree exploration.
- **`AnalysisResultCache`**: Caches the final domain results of analysis operations.

## Responsibilities
- **Efficient Parsing:** Loading JFR files with minimal overhead.
- **Memory Management:** Using weighed caches and explicit GC hints to manage large JFR objects.
- **Data Conversion:** Translating JFR-specific types to domain models via `JfrValueConverter`.
- **Grouped Access:** Using `StackTraceKey` for zero-allocation stack trace grouping.

## Patterns Used
- **Platform Thread Offloading:** CPU-intensive JFR loading is handled by a managed `ExecutorService`.
- **Weighed Caching:** Caffeine caches are weighed by estimated heap impact.
- **Identity Deduplication:** Using `JfrStackTraceService` to minimize heap pressure when formatting traces.

## Guidelines for Agents
- **Performance First:** Never parse a JFR file directly; always go through `JfrRecordingCache`.
- **Memory Pressure:** JFR data is heavy. Be surgical with event filtering.
- **Stack Traces:** When grouping by stack trace, use `StackTraceKey` instead of converting to Strings early.
- **Metrics:** Register cache gauges with `AnalysisMetrics`.
