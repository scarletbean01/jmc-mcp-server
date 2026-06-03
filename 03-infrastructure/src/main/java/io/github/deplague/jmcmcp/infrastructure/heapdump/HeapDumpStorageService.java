package io.github.deplague.jmcmcp.infrastructure.heapdump;

import io.github.deplague.jmcmcp.application.port.HeapDumpProvider;
import io.github.deplague.jmcmcp.domain.model.HeapDumpInfo;
import io.github.deplague.jmcmcp.infrastructure.api.model.UploadResponse;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.HeapSummary;

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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
public class HeapDumpStorageService {

    private static final Duration MAX_AGE = Duration.ofHours(24);

    private final Path uploadDir;
    private final HeapDumpProvider heapDumpProvider;
    private final Map<String, HeapDumpMetadata> heapDumps = new ConcurrentHashMap<>();
    private final Map<String, String> recordingToHeapDump = new ConcurrentHashMap<>();
    private final long maxStorageBytes;

    @Inject
    public HeapDumpStorageService(
            HeapDumpProvider heapDumpProvider,
            @ConfigProperty(name = "heapdump.storage.path", defaultValue = "heap-dumps") String storagePath,
            @ConfigProperty(name = "heapdump.max-size-mb", defaultValue = "2048") long maxSizeMb
    ) {
        this.heapDumpProvider = heapDumpProvider;
        this.uploadDir = Paths.get(storagePath).toAbsolutePath().normalize();
        this.maxStorageBytes = maxSizeMb * 1024 * 1024;
        try {
            Files.createDirectories(uploadDir);
            log.info("Heap dump upload directory: {} (maxSize={}MB)", uploadDir, maxSizeMb);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create heap dump upload directory: " + uploadDir, e);
        }
    }

    public UploadResponse storeHeapDump(String fileName, InputStream fileData, long fileSize) throws IOException {
        long currentSize = getTotalStorageBytes();
        if (currentSize + fileSize > maxStorageBytes) {
            evictOldestToMakeSpace(fileSize);
        }

        String heapDumpId = UUID.randomUUID().toString();
        Path targetPath = uploadDir.resolve(heapDumpId + ".hprof");

        if (fileData instanceof java.io.FileInputStream fis) {
            try (FileChannel source = fis.getChannel();
                 FileChannel dest = FileChannel.open(targetPath,
                         java.nio.file.StandardOpenOption.CREATE,
                         java.nio.file.StandardOpenOption.WRITE,
                         java.nio.file.StandardOpenOption.TRUNCATE_EXISTING)) {
                long transferred = source.transferTo(0, source.size(), dest);
                log.debug("Zero-copy upload: transferred {} bytes for {}", transferred, heapDumpId);
            }
        } else {
            Files.copy(fileData, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        heapDumps.put(heapDumpId, new HeapDumpMetadata(
                heapDumpId, fileName, fileSize, targetPath, Instant.now()
        ));

        log.info("Stored heap dump {} ({} bytes, totalStorage={}MB)",
                heapDumpId, fileSize, getTotalStorageBytes() / (1024 * 1024));

        // Async enrichment: parse heap dump to extract object/class counts
        CompletableFuture.runAsync(() -> enrichHeapDumpInfo(heapDumpId, targetPath.toString()));

        return new UploadResponse(heapDumpId, fileName, fileSize, Instant.now());
    }

    public HeapDumpInfo getHeapDumpInfo(String heapDumpId) {
        HeapDumpMetadata meta = heapDumps.get(heapDumpId);
        if (meta == null) {
            return null;
        }
        return toInfo(meta);
    }

    public List<HeapDumpInfo> listHeapDumps() {
        return heapDumps.values().stream()
                .map(this::toInfo)
                .sorted(java.util.Comparator.comparing(HeapDumpInfo::uploadTime).reversed())
                .toList();
    }

    private HeapDumpInfo toInfo(HeapDumpMetadata meta) {
        return new HeapDumpInfo(
                meta.heapDumpId,
                meta.fileName,
                meta.fileSize,
                meta.uploadedAt,
                meta.format,
                meta.objectCount,
                meta.classCount,
                Map.of()
        );
    }

    private void enrichHeapDumpInfo(String heapDumpId, String filePath) {
        try {
            Heap heap = heapDumpProvider.loadSnapshot(filePath);
            HeapSummary summary = heap.getSummary();
            long objectCount = summary != null ? summary.getTotalLiveInstances() : 0;
            long classCount = heap.getAllClasses() != null ? heap.getAllClasses().size() : 0;

            heapDumps.computeIfPresent(heapDumpId, (_, meta) -> {
                meta.objectCount = objectCount;
                meta.classCount = classCount;
                return meta;
            });
            log.info("Enriched heap dump {}: {} objects, {} classes", heapDumpId, objectCount, classCount);
        } catch (Exception e) {
            log.warn("Failed to enrich heap dump info for {}: {}", heapDumpId, e.getMessage());
        }
    }

    public String getHeapDumpPath(String heapDumpId) {
        HeapDumpMetadata meta = heapDumps.get(heapDumpId);
        return meta != null ? meta.filePath.toString() : null;
    }

    public boolean deleteHeapDump(String heapDumpId) {
        HeapDumpMetadata meta = heapDumps.remove(heapDumpId);
        if (meta != null) {
            recordingToHeapDump.values().removeIf(v -> v.equals(heapDumpId));
            try {
                Files.deleteIfExists(meta.filePath);
                log.info("Deleted heap dump {}", heapDumpId);
                return true;
            } catch (IOException e) {
                log.warn("Failed to delete heap dump file for {}", heapDumpId, e);
            }
        }
        return false;
    }

    public void associateWithRecording(String heapDumpId, String recordingId) {
        if (!heapDumps.containsKey(heapDumpId)) {
            throw new IllegalArgumentException("Heap dump not found: " + heapDumpId);
        }
        recordingToHeapDump.put(recordingId, heapDumpId);
        log.info("Associated heap dump {} with recording {}", heapDumpId, recordingId);
    }

    public String getAssociatedHeapDumpId(String recordingId) {
        return recordingToHeapDump.get(recordingId);
    }

    @Scheduled(every = "1h")
    void cleanupExpiredHeapDumps() {
        Instant cutoff = Instant.now().minus(MAX_AGE);
        log.debug("Running scheduled cleanup for heap dumps older than {}", cutoff);

        heapDumps.entrySet().removeIf(entry -> {
            HeapDumpMetadata meta = entry.getValue();
            if (meta.uploadedAt.isBefore(cutoff)) {
                try {
                    Files.deleteIfExists(meta.filePath);
                    log.info("Cleaned up expired heap dump {} (uploaded {})", meta.heapDumpId, meta.uploadedAt);
                } catch (IOException e) {
                    log.warn("Failed to delete expired heap dump file for {}", meta.heapDumpId, e);
                }
                return true;
            }
            return false;
        });
    }

    private void evictOldestToMakeSpace(long requiredBytes) {
        while (getTotalStorageBytes() + requiredBytes > maxStorageBytes && !heapDumps.isEmpty()) {
            HeapDumpMetadata oldest = heapDumps.values().stream()
                    .min(java.util.Comparator.comparing(m -> m.uploadedAt))
                    .orElse(null);
            if (oldest == null) break;
            heapDumps.remove(oldest.heapDumpId);
            recordingToHeapDump.values().removeIf(v -> v.equals(oldest.heapDumpId));
            try {
                Files.deleteIfExists(oldest.filePath);
                log.warn("Evicted oldest heap dump {} to make space ({} bytes)",
                        oldest.heapDumpId, oldest.fileSize);
            } catch (IOException e) {
                log.warn("Failed to delete evicted heap dump file for {}", oldest.heapDumpId, e);
            }
        }
    }

    private long getTotalStorageBytes() {
        return heapDumps.values().stream().mapToLong(m -> m.fileSize).sum();
    }

    private static final class HeapDumpMetadata {
        final String heapDumpId;
        final String fileName;
        final long fileSize;
        final Path filePath;
        final Instant uploadedAt;
        volatile String format = "HPROF";
        volatile long objectCount;
        volatile long classCount;

        HeapDumpMetadata(String heapDumpId, String fileName, long fileSize, Path filePath, Instant uploadedAt) {
            this.heapDumpId = heapDumpId;
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.filePath = filePath;
            this.uploadedAt = uploadedAt;
        }
    }
}
