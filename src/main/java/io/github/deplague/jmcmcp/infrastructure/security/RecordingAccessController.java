package io.github.deplague.jmcmcp.infrastructure.security;

import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validates JFR recording paths against an enterprise allowlist to prevent
 * path traversal attacks and unauthorized file system access.
 *
 * <p>Configuration via environment variable {@code JMC_MCP_ALLOWED_PATHS}
 * (comma-separated list of allowed base directories). If unset, defaults to
 * the current working directory only.</p>
 *
 * <p>Supported URI schemes: {@code file} (always). Additional schemes like
 * {@code s3://} or {@code https://} can be added via
 * {@code JMC_MCP_ALLOWED_SCHEMES}.</p>
 */
@Getter
public final class RecordingAccessController {

    private static final Logger LOG = LoggerFactory.getLogger(RecordingAccessController.class);

    private static final String ENV_ALLOWED_PATHS = "JMC_MCP_ALLOWED_PATHS";
    private static final String ENV_ALLOWED_SCHEMES = "JMC_MCP_ALLOWED_SCHEMES";
    private static final String ENV_DISABLE_VALIDATION = "JMC_MCP_DISABLE_PATH_VALIDATION";

    private static final Set<String> DEFAULT_SCHEMES = Set.of("file");
    private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024 * 1024; // 10 GB

    private final List<Path> allowedBasePaths;
    private final Set<String> allowedSchemes;
    private final boolean validationDisabled;

    public RecordingAccessController() {
        this.validationDisabled = Boolean.parseBoolean(System.getenv(ENV_DISABLE_VALIDATION));

        String pathsEnv = System.getenv(ENV_ALLOWED_PATHS);
        if (pathsEnv != null && !pathsEnv.isBlank()) {
            this.allowedBasePaths = Arrays.stream(pathsEnv.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Paths::get)
                    .map(Path::toAbsolutePath)
                    .map(Path::normalize)
                    .collect(Collectors.toList());
        } else {
            // Default: only allow files under the current working directory
            this.allowedBasePaths = Collections.singletonList(
                    Paths.get("").toAbsolutePath().normalize()
            );
        }

        String schemesEnv = System.getenv(ENV_ALLOWED_SCHEMES);
        if (schemesEnv != null && !schemesEnv.isBlank()) {
            Set<String> extra = Arrays.stream(schemesEnv.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toSet());
            Set<String> merged = new java.util.HashSet<>(DEFAULT_SCHEMES);
            merged.addAll(extra);
            this.allowedSchemes = Collections.unmodifiableSet(merged);
        } else {
            this.allowedSchemes = DEFAULT_SCHEMES;
        }

        LOG.info("RecordingAccessController initialized: allowedPaths={}, allowedSchemes={}, disabled={}",
                allowedBasePaths, allowedSchemes, validationDisabled);
    }

    /**
     * Validate that the given path is within an allowed directory and does not
     * contain traversal sequences. Also validates file size limits.
     *
     * @param rawPath the raw path string from user input
     * @throws SecurityException        if the path is not allowed
     * @throws IllegalArgumentException if the path is malformed
     */
    public void validate(String rawPath) {
        if (validationDisabled) {
            LOG.warn("Path validation is DISABLED. This is insecure for production use.");
            return;
        }

        if (rawPath == null || rawPath.isBlank()) {
            throw new AnalysisFailedException("Path cannot be null or blank");
        }

        // Detect null bytes and control characters
        if (rawPath.contains("\0") || containsControlChars(rawPath)) {
            throw new AnalysisFailedException("Path contains invalid characters");
        }

        // Check for obvious traversal patterns
        if (containsTraversal(rawPath)) {
            throw new AnalysisFailedException("Path traversal detected: " + rawPath);
        }

        // Parse URI to detect scheme
        URI uri = parseUri(rawPath);
        String scheme = uri.getScheme();
        if (scheme == null) {
            scheme = "file";
        }

        if (!allowedSchemes.contains(scheme)) {
            throw new SecurityException("URI scheme not allowed: " + scheme
                    + ". Allowed schemes: " + allowedSchemes);
        }

        // For file:// URIs and plain paths, validate against base directory allowlist
        if ("file".equals(scheme)) {
            String pathPart = uri.getPath();
            if (pathPart == null) {
                pathPart = uri.getSchemeSpecificPart();
            }
            if (pathPart == null) {
                pathPart = rawPath;
            }

            File file = new File(pathPart).getAbsoluteFile();
            Path normalizedPath = file.toPath().toAbsolutePath().normalize();

            // Must be under one of the allowed base paths
            boolean allowed = allowedBasePaths.stream()
                    .anyMatch(base -> normalizedPath.startsWith(base));

            if (!allowed) {
                throw new SecurityException(
                        "Access denied: " + normalizedPath + " is not within any allowed base path: " + allowedBasePaths);
            }

            // File size check (only for existing files)
            if (file.exists() && file.isFile() && file.length() > MAX_FILE_SIZE_BYTES) {
                throw new SecurityException("File exceeds maximum allowed size (10GB): " + rawPath);
            }
        }

        LOG.debug("Path validated: {}", rawPath);
    }

    /**
     * Check if a path contains directory traversal sequences that could escape
     * the intended directory.
     */
    private static boolean containsTraversal(String path) {
        // Normalize separators and check for .. sequences
        String normalized = path.replace('\\', '/');

        // Skip valid URI scheme prefixes (file://, s3://, https://) before traversal checks
        String pathOnly = normalized;
        if (pathOnly.contains("://")) {
            int schemeEnd = pathOnly.indexOf("://") + 3;
            pathOnly = pathOnly.substring(schemeEnd);
        }

        // Direct .. at start, after /, or before end
        if (pathOnly.contains("/../") || pathOnly.endsWith("/..") || pathOnly.startsWith("../")) {
            return true;
        }

        // Double slashes in the path portion can be used for confusion
        if (pathOnly.contains("//")) {
            return true;
        }

        // URL-encoded traversal
        if (normalized.contains("%2e%2e") || normalized.contains("%2E%2E") ||
                normalized.contains("..%2f") || normalized.contains("..%2F")) {
            return true;
        }

        return false;
    }

    private static boolean containsControlChars(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 0x20 && c != '\t' && c != '\n' && c != '\r') {
                return true;
            }
        }
        return false;
    }

    private static URI parseUri(String rawPath) {
        try {
            return new URI(rawPath);
        } catch (URISyntaxException e) {
            // Not a valid URI — treat as local file path
            return new File(rawPath).toURI();
        }
    }

}
