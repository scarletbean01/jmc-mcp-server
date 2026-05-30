# PLAN: HTTP API Improvements

This document outlines a plan to improve the REST API design, consistency, and robustness of the JMC MCP Server.

## 1. Architectural Consistency

### 1.1 Standardized Resource Hierarchy
- Ensure all recording-related operations follow the `/api/v1/recordings/{id}` pattern.
- Currently, `ComparisonResource` is at `/api/v1/compare`. This should be moved to `/api/v1/recordings/compare` to keep the resource root clean.
- Ensure `recordingId` in `/api/v1/recordings/{recordingId}/analyze/call-tree/{treeId}/expand` is actually validated against the `treeId` context.

### 1.2 Refactor `AnalysisDispatcher`
- The `AnalysisDispatcher` is currently a "God Object" with 50+ injections and a massive switch-case.
- **Improvement:** Implement an `AnalysisModule` interface and use CDI to discover and register analysis handlers. This will make the dispatcher thin and the system more extensible.

## 2. Robustness & Error Handling

### 2.1 Unified Exception Mapping
- Replace manual `try-catch` blocks in resources with a set of `ExceptionMapper` implementations.
- Map `RecordingNotFoundException` to `404 Not Found`.
- Map `AnalysisFailedException` to `422 Unprocessable Entity` or `500 Internal Server Error` depending on the cause.
- Map `IllegalArgumentException` to `400 Bad Request`.
- Create a `GlobalExceptionMapper` to catch all unexpected errors and return a standardized `ApiResponse.error()`.

### 2.2 Input Validation
- Add `jakarta.validation.constraints` to `AnalysisRequest` and `CompareRequest`.
- Use `@Valid` in resource methods to trigger Bean Validation.
- Ensure `startTime` and `endTime` follow ISO-8601 format via custom validators.

## 3. Developer Experience & Discovery

### 3.1 OpenAPI Documentation
- Add `io.quarkus:quarkus-smallrye-openapi` dependency.
- Annotate all endpoints with `@Operation`, `@APIResponse`, and `@Parameter` to provide a rich Swagger UI at `/q/swagger-ui`.

### 3.2 Discovery Endpoint
- Implement `GET /api/v1/analysis/types`.
- This endpoint should return a list of available analysis types, their descriptions, and the parameters they support (with default values).

## 4. API Polish

### 4.1 Enhanced Metadata
- Update `ApiResponse<T>` to include `executionTimeMs`.
- Add a `pagination` field to `ApiResponse` for endpoints that return lists (e.g., `hot-methods` using `topN`).

### 4.2 SSE Streaming Improvements
- Ensure `streamJob` returns a `404` immediately if the `jobId` does not exist.
- Add heartbeat events to keep connections alive through aggressive proxies.

### 4.3 Async Execution Control
- Define a dedicated `ManagedExecutor` for JFR analysis tasks.
- Avoid using the `ForkJoinPool.commonPool()` for CPU-intensive analysis to prevent starving other platform components.

## 5. Implementation Roadmap

1. **Phase 1: Foundation** - Add OpenAPI and Exception Mappers.
2. **Phase 2: Validation** - Add Bean Validation to models.
3. **Phase 3: Refactoring** - Decompose `AnalysisDispatcher` into modules.
4. **Phase 4: Polish** - Add discovery endpoint and metadata enhancements.
