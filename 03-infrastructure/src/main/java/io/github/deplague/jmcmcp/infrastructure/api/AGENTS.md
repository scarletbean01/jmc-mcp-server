# Package: io.github.deplague.jmcmcp.infrastructure.api

This package contains the REST API driving adapter, built with Quarkus REST.

## Sub-packages
- **`health`**: SmallRye Health checks (`JmcLivenessCheck`, `JmcReadinessCheck`).
- **`metrics`**: Micrometer-based instrumentation (`AnalysisMetrics`).
- **`model`**: HTTP request/response DTOs (Java Records).

## Key Classes
- **`AnalysisResource`**: Main endpoint for JFR analysis and async job status. Supports SSE for job progress.
- **`ComparisonResource`**: Endpoints for A/B testing recordings. Supports structured overview metrics, diff call trees, and method-level hotspot deep dives.
- **`AnalysisDispatcher`**: Central hub that maps analysis types (Strings) to specific application services.
- **`RecordingUploadResource`**: Handles multipart JFR file uploads.
- **`AnalysisMetrics`**: Centralizes metrics for duration, cache hits, errors, and queue depth.

## Responsibilities
- **REST Endpoints:** Exposing the server's capabilities over HTTP.
- **Multipart Handling:** Managing JFR file uploads via `RecordingStorageService`.
- **SSE Streaming:** Providing real-time updates for long-running analysis jobs.
- **Metrics & Health:** Exposing the operational state of the server.

## Patterns Used
- **Dispatcher Pattern:** `AnalysisDispatcher` decouples the HTTP resource from the myriad of application services.
- **Standardized Responses:** Using `ApiResponse<T>` for consistent JSON output.
- **Virtual Threads:** Every endpoint method uses `@RunOnVirtualThread`.

## Guidelines for Agents
- **Mirroring:** Ensure the REST API behavior (analysis types, parameters) remains consistent with the MCP tools.
- **Error Mapping:** Use `ApiResponse.error()` to return meaningful messages to the client.
- **Observability:** Ensure every new endpoint is instrumented via `AnalysisMetrics`.
- **Models:** Use Java Records for all request and response bodies.
- **Absolute Boundaries:** The `RecordingInfo` DTO exposes computed absolute boundaries (`startTime`, `endTime`) and the actual duration of the JFR file. Frontend clients must use these to convert relative timeline selections into absolute ISO-8601 params before querying endpoints.
