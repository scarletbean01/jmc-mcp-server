# Package: io.github.deplague.jmcmcp.infrastructure.security

This package provides technical guards and access control for the server.

## Key Classes
- **`RecordingAccessController`**: Validates file paths against an allowlist (`JMC_MCP_ALLOWED_PATHS`) to prevent path traversal and unauthorized access. It also checks file size limits and URI schemes.

## Responsibilities
- **Path Validation:** Ensuring user-provided paths are safe and within permitted directories.
- **Security Policy:** Managing the list of allowed URI schemes (e.g., `file://`).
- **Resource Protection:** Preventing denial-of-service via massive JFR files.

## Patterns Used
- **Allowlist Validation:** Deny by default, allow by configuration.
- **Environment-Based Config:** Using environment variables for flexible security policies.

## Guidelines for Agents
- **Security First:** **ALWAYS** call `RecordingAccessController.validate(path)` before any file I/O involving user-provided paths.
- **No Bypass:** Do not implement your own path normalization or validation; rely on this centralized controller.
- **Error Propagation:** Validation failures will throw `SecurityException` or `AnalysisFailedException`, which are handled by tool interceptors.
