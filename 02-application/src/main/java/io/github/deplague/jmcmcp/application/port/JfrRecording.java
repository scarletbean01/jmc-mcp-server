package io.github.deplague.jmcmcp.application.port;

import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

public interface JfrRecording {
    IItemCollection load(String filePath) throws IOException;

    void evict(String filePath);

    void clear();

    int size();

    long getHitCount();

    long getMissCount();

    long getEvictionCount();

    long getTotalCachedBytes();

    void shutdown();
}
