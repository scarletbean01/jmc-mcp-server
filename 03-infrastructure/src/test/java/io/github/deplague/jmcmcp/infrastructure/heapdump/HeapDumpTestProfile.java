package io.github.deplague.jmcmcp.infrastructure.heapdump;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.nio.file.Files;
import java.util.Map;

public class HeapDumpTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        try {
            return Map.of(
                    "heapdump.storage.path", Files.createTempDirectory("heapdump-test").toString()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
