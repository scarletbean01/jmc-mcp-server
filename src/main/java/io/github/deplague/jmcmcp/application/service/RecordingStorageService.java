package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.infrastructure.api.model.RecordingInfo;
import io.github.deplague.jmcmcp.infrastructure.api.model.UploadResponse;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Handles JFR recording file uploads, persistent storage, and lifecycle management.
 * Includes scheduled cleanup of recordings older than 24 hours.
 */
@Slf4j
@ApplicationScoped
public class RecordingStorageService {

    private static final Duration MAX_AGE = Duration.ofHours(24);

    private final Path uploadDir;
    private final JfrProvider jfrProvider;
    private final Map<String, RecordingMetadata> recordings = new ConcurrentHashMap<>();

    @Inject
    public RecordingStorageService(
            JfrProvider jfrProvider,
            @ConfigProperty(name = "storage.path", defaultValue = "uploads") String storagePath
    ) {
        this.jfrProvider = jfrProvider;
        this.uploadDir = Paths.get(storagePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadDir);
            log.info("Upload directory: {}", uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory: " + uploadDir, e);
        }
    }

    public UploadResponse storeRecording(String fileName, InputStream fileData, long fileSize) throws IOException {
        String recordingId = UUID.randomUUID().toString();
        Path targetPath = uploadDir.resolve(recordingId + ".jfr");
        Files.copy(fileData, targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Pre-load into JFR cache to validate
        IItemCollection events = jfrProvider.loadRecording(targetPath.toString());

        recordings.put(recordingId, new RecordingMetadata(
                recordingId, fileName, fileSize, targetPath, Instant.now()
        ));

        log.info("Stored recording {} ({} bytes)", recordingId, fileSize);
        return new UploadResponse(recordingId, fileName, fileSize, Instant.now());
    }

    public RecordingInfo getRecordingInfo(String recordingId) throws IOException {
        RecordingMetadata meta = recordings.get(recordingId);
        if (meta == null) {
            return null;
        }

        IItemCollection events = jfrProvider.loadRecording(meta.filePath.toString());
        long eventCount = 0;
        for (var iterable : events) {
            eventCount += iterable.getItemCount();
        }
        return new RecordingInfo(
                meta.recordingId,
                meta.fileName,
                meta.fileSize,
                meta.uploadedAt,
                0.0,
                eventCount,
                Map.of()
        );
    }

    public String getRecordingPath(String recordingId) {
        RecordingMetadata meta = recordings.get(recordingId);
        return meta != null ? meta.filePath.toString() : null;
    }

    public boolean deleteRecording(String recordingId) {
        RecordingMetadata meta = recordings.remove(recordingId);
        if (meta != null) {
            try {
                Files.deleteIfExists(meta.filePath);
                log.info("Deleted recording {}", recordingId);
                return true;
            } catch (IOException e) {
                log.warn("Failed to delete recording file for {}", recordingId, e);
            }
        }
        return false;
    }

    /**
     * Scheduled cleanup: removes recordings older than 24 hours.
     */
    @Scheduled(every = "1h")
    void cleanupExpiredRecordings() {
        Instant cutoff = Instant.now().minus(MAX_AGE);
        log.debug("Running scheduled cleanup for recordings older than {}", cutoff);

        recordings.entrySet().removeIf(entry -> {
            RecordingMetadata meta = entry.getValue();
            if (meta.uploadedAt.isBefore(cutoff)) {
                try {
                    Files.deleteIfExists(meta.filePath);
                    log.info("Cleaned up expired recording {} (uploaded {})", meta.recordingId, meta.uploadedAt);
                } catch (IOException e) {
                    log.warn("Failed to delete expired recording file for {}", meta.recordingId, e);
                }
                return true;
            }
            return false;
        });
    }

    private record RecordingMetadata(
            String recordingId,
            String fileName,
            long fileSize,
            Path filePath,
            Instant uploadedAt
    ) {
    }
}
