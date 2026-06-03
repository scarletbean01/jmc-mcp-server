package io.github.deplague.jmcmcp.application.port;

import org.netbeans.lib.profiler.heap.Heap;

import java.io.IOException;

/**
 * Port for loading and parsing heap dump files.
 * Implemented by infrastructure adapters.
 */
public interface HeapDumpProvider {

    /**
     * Load a heap dump from the given file path.
     *
     * @param filePath path to the .hprof or .phd file
     * @return the parsed heap
     * @throws IOException if the file cannot be read or parsed
     */
    Heap loadSnapshot(String filePath) throws IOException;

    /**
     * Evict a cached snapshot.
     *
     * @param filePath the file path to evict
     */
    void evict(String filePath);

    /**
     * Clear all cached snapshots.
     */
    void clear();
}
