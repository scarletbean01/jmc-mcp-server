# Package: io.github.deplague.jmcmcp

This is the root package of the JMC MCP Server, responsible for bootstrapping and high-level lifecycle management.

## Key Classes
- **`JmcMcpServer`**: The `@QuarkusMain` entry point. It implements `QuarkusApplication` and handles the initial startup logging and shutdown hooks (e.g., clearing the `JfrRecordingCache`).

## Responsibilities
- **Bootstrap:** Orchestrates the Quarkus lifecycle.
- **Resource Management:** Ensures graceful shutdown of caches and background executors.
- **Project Structure:** Defines the top-level package for the Hexagonal Architecture implementation.

## Patterns Used
- **Singleton Entry Point:** Using `@QuarkusMain` for a clean CLI-style execution.
- **CDI Injection:** Standard Jakarta EE/Quarkus dependency injection.

## Guidelines for Agents
- **Architectural Reference:** This project follows **Hexagonal Architecture**.
- **Stdio Communication:** This server is designed to communicate via MCP over stdio. **NEVER** write to `System.out` or `stdout` directly (use SLF4J/Logback which are configured to redirect to `stderr`).
- **Lifecycle:** When adding global services, ensure they are registered or managed within the `JmcMcpServer` shutdown logic if they require manual cleanup.
