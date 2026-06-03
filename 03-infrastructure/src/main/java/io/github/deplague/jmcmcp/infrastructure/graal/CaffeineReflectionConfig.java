package io.github.deplague.jmcmcp.infrastructure.graal;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Registers Caffeine's dynamically-loaded internal cache implementation classes
 * for GraalVM native image compilation.
 *
 * <p>Caffeine constructs cache implementation class names at runtime based on the
 * builder configuration (key/value reference strength, eviction policy, weighting,
 * expiration, statistics, etc.). These classes are loaded reflectively via
 * {@code MethodHandles.Lookup.findClass()}, which fails in a native image unless
 * they are explicitly registered.</p>
 *
 * <p>The registered class names correspond to the following cache configurations
 * used across the application:</p>
 * <ul>
 *   <li><b>SSLSMWA</b> – {@code JfrRecordingCache} &amp; {@code HeapDumpCache}
 *       (strong keys/values, removal listener, stats, max-weight, expire-after-access)</li>
 *   <li><b>SSLMSW</b> – {@code AnalysisResultCache}
 *       (strong keys/values, removal listener, stats, max-size, expire-after-write)</li>
 *   <li><b>SSLMWA</b> – {@code CallTreeCache}
 *       (strong keys/values, removal listener, max-weight, expire-after-access)</li>
 * </ul>
 */
@RegisterForReflection(classNames = {
        "com.github.benmanes.caffeine.cache.SSLSMWA",
        "com.github.benmanes.caffeine.cache.SSLMSW",
        "com.github.benmanes.caffeine.cache.SSLMWA"
})
public final class CaffeineReflectionConfig {
    // No instances – purely a holder for the @RegisterForReflection annotation.
}
