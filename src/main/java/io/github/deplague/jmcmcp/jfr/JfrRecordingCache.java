package io.github.deplague.jmcmcp.jfr;

import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory cache for loaded JFR recordings.
 * Maps absolute file paths to loaded {@link IItemCollection} instances.
 * Thread-safe via {@link ConcurrentHashMap}.
 */
public final class JfrRecordingCache {

    private static final Logger LOG = LoggerFactory.getLogger(JfrRecordingCache.class);

    private final Map<String, IItemCollection> cache = new ConcurrentHashMap<>();

    /**
     * Load a JFR recording from the given file path, using the cache if available.
     *
     * @param filePath path to the .jfr file
     * @return the loaded item collection
     * @throws IOException if the file cannot be read or parsed
     */
    public IItemCollection load(String filePath) throws IOException {
        File file = new File(filePath).getAbsoluteFile();
        String key = file.getAbsolutePath();

        IItemCollection cached = cache.get(key);
        if (cached != null) {
            LOG.debug("Cache hit for recording: {}", key);
            return cached;
        }

        if (!file.exists() || !file.isFile()) {
            throw new IOException("JFR file does not exist or is not a regular file: " + filePath);
        }

        LOG.info("Loading JFR recording: {}", key);
        IItemCollection events;
        try {
            events = JfrLoaderToolkit.loadEvents(file);
        } catch (org.openjdk.jmc.flightrecorder.CouldNotLoadRecordingException e) {
            throw new IOException("Failed to load JFR recording: " + filePath, e);
        }
        cache.put(key, events);
        LOG.info("Loaded {} events from {}", events.iterator().hasNext() ? "some" : "no", key);
        return events;
    }

    /**
     * Evict a specific recording from the cache.
     *
     * @param filePath path to the .jfr file
     */
    public void evict(String filePath) {
        File file = new File(filePath).getAbsoluteFile();
        cache.remove(file.getAbsolutePath());
    }

    /**
     * Clear the entire cache.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * @return number of cached recordings
     */
    public int size() {
        return cache.size();
    }
}
