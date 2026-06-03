package io.github.deplague.jmcmcp.infrastructure.jfr;

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
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Map<String, String> recordingToHeapDump = new ConcurrentHashMap<>();
    private final Map<String, String> heapDumpToRecording = new ConcurrentHashMap<>();
    private final long maxStorageBytes;

    @Inject
    public RecordingStorageService(
            JfrProvider jfrProvider,
            @ConfigProperty(name = "storage.path", defaultValue = "uploads") String storagePath,
            @ConfigProperty(name = "storage.max-size-mb", defaultValue = "10240") long maxSizeMb
    ) {
        this.jfrProvider = jfrProvider;
        this.uploadDir = Paths.get(storagePath).toAbsolutePath().normalize();
        this.maxStorageBytes = maxSizeMb * 1024 * 1024;
        try {
            Files.createDirectories(uploadDir);
            log.info("Upload directory: {} (maxSize={}MB)", uploadDir, maxSizeMb);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory: " + uploadDir, e);
        }
    }

    public UploadResponse storeRecording(String fileName, InputStream fileData, long fileSize) throws IOException {
        long currentSize = getTotalStorageBytes();
        if (currentSize + fileSize > maxStorageBytes) {
            evictOldestToMakeSpace(fileSize);
        }

        String recordingId = UUID.randomUUID().toString();
        Path targetPath = uploadDir.resolve(recordingId + ".jfr");

        // Zero-copy transfer if the InputStream is backed by a FileChannel
        if (fileData instanceof java.io.FileInputStream fis) {
            try (FileChannel source = fis.getChannel();
                 FileChannel dest = FileChannel.open(targetPath,
                         java.nio.file.StandardOpenOption.CREATE,
                         java.nio.file.StandardOpenOption.WRITE,
                         java.nio.file.StandardOpenOption.TRUNCATE_EXISTING)) {
                long transferred = source.transferTo(0, source.size(), dest);
                log.debug("Zero-copy upload: transferred {} bytes for {}", transferred, recordingId);
            }
        } else {
            Files.copy(fileData, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Pre-load into JFR cache to validate
        IItemCollection events = jfrProvider.loadRecording(targetPath.toString());

        recordings.put(recordingId, new RecordingMetadata(
                recordingId, fileName, fileSize, targetPath, Instant.now()
        ));

        log.info("Stored recording {} ({} bytes, totalStorage={}MB)",
                recordingId, fileSize, getTotalStorageBytes() / (1024 * 1024));
        return new UploadResponse(recordingId, fileName, fileSize, Instant.now());
    }

    public RecordingInfo getRecordingInfo(String recordingId) throws IOException {
        RecordingMetadata meta = recordings.get(recordingId);
        if (meta == null) {
            return null;
        }
        return mapToInfo(meta);
    }

    public List<RecordingInfo> listRecordings() {
        return recordings.values().stream()
                .map(meta -> {
                    try {
                        return mapToInfo(meta);
                    } catch (IOException e) {
                        log.warn("Failed to get info for recording {}", meta.recordingId, e);
                        return new RecordingInfo(meta.recordingId, meta.fileName, meta.fileSize, meta.uploadedAt, 0.0, 0, null, null, Map.of());
                    }
                })
                .sorted(java.util.Comparator.comparing(RecordingInfo::uploadTime).reversed())
                .toList();
    }

    private RecordingInfo mapToInfo(RecordingMetadata meta) throws IOException {
        IItemCollection events = jfrProvider.loadRecording(meta.filePath.toString());
        long eventCount = 0;
        for (var iterable : events) {
            eventCount += iterable.getItemCount();
        }
        double durationSeconds = 0.0;
        Instant startTime = null;
        Instant endTime = null;
        try {
            org.openjdk.jmc.common.unit.IQuantity startQ = org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit.getEarliestStartTime(events);
            org.openjdk.jmc.common.unit.IQuantity endQ = org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit.getLatestEndTime(events);
            if (startQ != null && endQ != null) {
                long startMillis = startQ.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS);
                long endMillis = endQ.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS);
                durationSeconds = (endMillis - startMillis) / 1000.0;
                startTime = Instant.ofEpochMilli(startMillis);
                endTime = Instant.ofEpochMilli(endMillis);
            }
        } catch (Exception e) {
            log.warn("Failed to extract duration for recording {}", meta.recordingId, e);
        }
        String heapDumpId = recordingToHeapDump.get(meta.recordingId);
        Map<String, String> extra = heapDumpId != null ? Map.of("heapDumpId", heapDumpId) : Map.of();
        return new RecordingInfo(
                meta.recordingId,
                meta.fileName,
                meta.fileSize,
                meta.uploadedAt,
                durationSeconds,
                eventCount,
                startTime,
                endTime,
                extra
        );
    }

    public String getRecordingPath(String recordingId) {
        RecordingMetadata meta = recordings.get(recordingId);
        return meta != null ? meta.filePath.toString() : null;
    }

    public void associateHeapDump(String recordingId, String heapDumpId) {
        recordings.get(recordingId); // validate recording exists
        recordingToHeapDump.put(recordingId, heapDumpId);
        heapDumpToRecording.put(heapDumpId, recordingId);
        log.info("Associated heap dump {} with recording {}", heapDumpId, recordingId);
    }

    public String getAssociatedHeapDumpId(String recordingId) {
        return recordingToHeapDump.get(recordingId);
    }

    public String getAssociatedRecordingId(String heapDumpId) {
        return heapDumpToRecording.get(heapDumpId);
    }

    public boolean deleteRecording(String recordingId) {
        RecordingMetadata meta = recordings.remove(recordingId);
        String heapDumpId = recordingToHeapDump.remove(recordingId);
        if (heapDumpId != null) {
            heapDumpToRecording.remove(heapDumpId);
        }
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

    private void evictOldestToMakeSpace(long requiredBytes) {
        while (getTotalStorageBytes() + requiredBytes > maxStorageBytes && !recordings.isEmpty()) {
            RecordingMetadata oldest = recordings.values().stream()
                    .min(java.util.Comparator.comparing(RecordingMetadata::uploadedAt))
                    .orElse(null);
            if (oldest == null) break;
            recordings.remove(oldest.recordingId);
            try {
                Files.deleteIfExists(oldest.filePath);
                log.warn("Evicted oldest recording {} to make space ({} bytes)",
                        oldest.recordingId, oldest.fileSize);
            } catch (IOException e) {
                log.warn("Failed to delete evicted recording file for {}", oldest.recordingId, e);
            }
        }
    }

    private long getTotalStorageBytes() {
        return recordings.values().stream().mapToLong(RecordingMetadata::fileSize).sum();
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
