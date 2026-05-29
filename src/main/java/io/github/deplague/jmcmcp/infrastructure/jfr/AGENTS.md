# Package: io.github.deplague.jmcmcp.infrastructure.jfr

This package contains the technical infrastructure for loading, parsing, and caching Java Flight Recorder (JFR) data using the JMC core libraries.

## Responsibilities
- **Data Ingestion:** `JfrProviderImpl` implements the `JfrProvider` application port.
- **Recording Caching:** `JfrRecordingCache` manages parsed `IItemCollection` instances using Caffeine to avoid redundant parsing.
- **Call Tree Caching:** `CallTreeCache` manages stateful call tree data for interactive exploration using Caffeine.
- **Low-level Utilities:** Modularized helpers for attribute extraction, quantity aggregation, zero-allocation map grouping, and stack trace formatting.

## Guidelines for Agents
- **Performance:** JFR files can be massive. Always prefer `JfrRecordingCache.loadRecording()` over direct parsing.
- **Deduplication:** Use the identity-based caches in `JfrStackTraceService` when formatting large numbers of events to minimize heap pressure.
- **Zero-Allocation Grouping:** Use `StackTraceKey` when grouping JFR events by stack trace in hash maps. Never format the full trace into a String until *after* grouping is complete.
- **Modular Utilities:**
    - Use `JfrAccessorRepository` for high-performance attribute access.
    - Use `JfrQuantityAggregator` for statistical computations.
    - Use `JfrValueConverter` for type and display conversions.
