# Package: io.github.deplague.jmcmcp.infrastructure.api

This package contains the Public REST API driving adapter, built with Quarkus REST (RESTEasy Reactive).

## Responsibilities
- **Endpoints:** Exposes JFR analysis use cases via HTTP.
- **Multipart Upload:** Handles JFR file uploads through `RecordingUploadResource`.
- **Async Execution:** Provides background analysis via `/async` endpoints.
- **SSE Streaming:** Streams job progress through `AnalysisResource` using Server-Sent Events.
- **Dispatching:** `AnalysisDispatcher` centralizes the mapping between analysis types and application services.

## Guidelines for Agents
- **Hexagonal Architecture:** This is a **Driving Adapter**. It depends only on the `application` layer and `api.model` wrappers.
- **Virtual Threads:** Every endpoint method MUST be annotated with `@RunOnVirtualThread`.
- **JSON Serialization:** Use Java 25 Records for all request/response models. All responses should be wrapped in `ApiResponse<T>`.
- **Error Handling:** Catch exceptions and return appropriate HTTP status codes (400 for logic errors, 404 for missing recordings, 500 for internal failures) wrapped in `ApiResponse.error()`.
- **Consistency:** Ensure the REST API behavior mirrors the MCP tool behavior for the same analysis types.
