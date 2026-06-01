package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.domain.model.LiveRecordingDumpResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingListResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingStartResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingStopResult;
import io.github.deplague.jmcmcp.domain.service.LiveRecordingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Application service that orchestrates live JFR recording management.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class LiveRecordingApplicationService {

    private final LiveRecordingService liveRecordingService;

    public LiveRecordingListResult list(String jmxUrl) throws Exception {
        return liveRecordingService.listRecordings(jmxUrl);
    }

    public LiveRecordingStartResult start(String jmxUrl, String name, long durationSeconds) throws Exception {
        return liveRecordingService.startRecording(jmxUrl, name, durationSeconds);
    }

    public LiveRecordingStopResult stop(String jmxUrl, long recordingId) throws Exception {
        return liveRecordingService.stopRecording(jmxUrl, recordingId);
    }

    public LiveRecordingDumpResult dump(String jmxUrl, long recordingId, String outputPath) throws Exception {
        return liveRecordingService.dumpRecording(jmxUrl, recordingId, outputPath);
    }
}
