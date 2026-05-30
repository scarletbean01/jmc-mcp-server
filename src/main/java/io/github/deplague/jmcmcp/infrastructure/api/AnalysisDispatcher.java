package io.github.deplague.jmcmcp.infrastructure.api;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.application.service.*;
import io.github.deplague.jmcmcp.domain.service.RecordingSettingsService;
import io.github.deplague.jmcmcp.infrastructure.api.model.AnalysisRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.util.Map;

/**
 * Dispatches analysis requests to the appropriate application service.
 * Maps URL path segments to service calls, keeping REST resources thin.
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AnalysisDispatcher {

    private final JfrOverviewApplicationService overviewService;
    private final HotMethodsApplicationService hotMethodsService;
    private final GcDetailApplicationService gcDetailService;
    private final HeapTrendsApplicationService heapTrendsService;
    private final ThreadCpuApplicationService threadCpuService;
    private final CallTreeApplicationService callTreeService;
    private final ExpandCallTreeApplicationService expandCallTreeService;
    private final CpuFlameApplicationService cpuFlameService;
    private final AllocationFlameApplicationService allocationFlameService;
    private final LockFlameApplicationService lockFlameService;
    private final ThreadDumpApplicationService threadDumpService;
    private final DeadlockDetectionApplicationService deadlockService;
    private final BlockingSummaryApplicationService blockingService;
    private final MemoryLeaksApplicationService memoryLeaksService;
    private final ExceptionAnalysisApplicationService exceptionService;
    private final ErrorAnalysisApplicationService errorService;
    private final IoAnalysisApplicationService ioService;
    private final NetworkAnalysisApplicationService networkService;
    private final SafepointAnalysisApplicationService safepointService;
    private final VmOperationsApplicationService vmOpsService;
    private final JitCompilationApplicationService jitService;
    private final CodeCacheApplicationService codeCacheService;
    private final ClassLoadingApplicationService classLoadingService;
    private final SystemHealthApplicationService systemHealthService;
    private final ContainerMetricsApplicationService containerService;
    private final VirtualThreadsApplicationService virtualThreadsService;
    private final QuickAnalysisApplicationService quickService;
    private final CompareRecordingsApplicationService compareService;
    private final DiffCallTreeApplicationService diffCallTreeService;
    private final DiffStackTracesApplicationService diffStackService;
    private final HighCpuDiagnosticApplicationService highCpuService;
    private final IncidentTimelineApplicationService incidentService;
    private final IoHotspotsApplicationService ioHotspotsService;
    private final JdkBugReferenceApplicationService jdkBugService;
    private final JfrEventStatsApplicationService eventStatsService;
    private final JfrRulesApplicationService rulesService;
    private final LiveRecordingApplicationService liveService;
    private final LockAnalysisApplicationService lockAnalysisService;
    private final NativeMemoryApplicationService nativeMemService;
    private final ObjectStatisticsApplicationService objectStatsService;
    private final PredictiveLeakAnalysisApplicationService predictiveLeakService;
    private final RecordingSettingsService settingsService;
    private final RequestWaterfallApplicationService waterfallService;
    private final SearchEventsApplicationService searchService;
    private final SmartJdbcNPlusOneAnalyzerApplicationService nplusoneService;
    private final SmartLockResolverApplicationService lockResolverService;
    private final SmartThreadStarvationDetectorApplicationService starvationService;
    private final StackTraceSearchApplicationService stackSearchService;
    private final ThreadActivityApplicationService threadActivityService;
    private final ThreadAllocationApplicationService threadAllocService;
    private final ThreadContentionApplicationService threadContentionService;
    private final ThreadPoolAnalysisApplicationService threadPoolService;
    private final TimeSeriesApplicationService timeSeriesService;
    private final JfrProvider jfrProvider;

    public Object dispatch(String analysisType, String filePath, AnalysisRequest request) throws Exception {
        String startTime = request.startTime();
        String endTime = request.endTime();
        Map<String, Object> p = request.params();

        return switch (analysisType) {
            case "overview" -> overviewService.analyze(filePath, startTime, endTime);
            case "hot-methods" -> hotMethodsService.analyze(
                    filePath, startTime, endTime,
                    strParam(p, "threadName", null),
                    strParam(p, "packagePrefix", null),
                    intParam(p, "topN", 20));
            case "gc-detail" ->
                    gcDetailService.analyze(filePath, startTime, endTime, strParam(p, "detailLevel", "standard"));
            case "heap-trends" ->
                    heapTrendsService.analyze(filePath, startTime, endTime, strParam(p, "bucketSize", "auto"));
            case "thread-cpu" -> threadCpuService.analyze(
                    filePath, startTime, endTime,
                    strParam(p, "packagePrefix", null),
                    intParam(p, "topN", 20));
            case "call-tree" -> callTreeService.analyze(
                    filePath,
                    strParam(p, "subsystem", "cpu"),
                    strParam(p, "packageFilter", null),
                    startTime, endTime
            );
            case "cpu-flame" -> cpuFlameService.analyze(filePath, startTime, endTime, intParam(p, "topN", 50));
            case "allocation-flame" ->
                    allocationFlameService.analyze(filePath, startTime, endTime, strParam(p, "packagePrefix", null), intParam(p, "topN", 50));
            case "lock-flame" -> lockFlameService.analyze(filePath, startTime, endTime, intParam(p, "topN", 50));
            case "thread-dump" -> threadDumpService.analyze(filePath, intParam(p, "maxDumps", 10));
            case "deadlock-detection" -> deadlockService.analyze(filePath, startTime, endTime);
            case "blocking-summary" -> blockingService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "memory-leaks" -> memoryLeaksService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "exceptions" -> exceptionService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "errors" -> errorService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "io-analysis" -> ioService.analyze(filePath, startTime, endTime, strParam(p, "ioType", "all"));
            case "network-analysis" -> networkService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "safepoint-analysis" ->
                    safepointService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "vm-operations" -> vmOpsService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "jit-compilation" -> jitService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "code-cache" -> codeCacheService.analyze(filePath, startTime, endTime);
            case "class-loading" -> classLoadingService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "system-health" -> systemHealthService.analyze(filePath, startTime, endTime);
            case "container-metrics" -> containerService.analyze(filePath, startTime, endTime);
            case "virtual-threads" ->
                    virtualThreadsService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "quick-analysis" -> quickService.analyze(filePath, startTime, endTime, strParam(p, "focus", null));
            case "high-cpu-diagnostic" ->
                    highCpuService.analyze(filePath, startTime, endTime, strParam(p, "packagePrefix", null));
            case "incident-timeline" ->
                    incidentService.analyze(filePath, strParam(p, "anchorEvent", null), strParam(p, "anchorTime", null), intParam(p, "windowMs", 30000));
            case "io-hotspots" ->
                    ioHotspotsService.analyze(filePath, startTime, endTime, strParam(p, "endpointFilter", null), intParam(p, "topN", 20));
            case "jdk-bug-reference" -> jdkBugService.analyze(filePath, startTime, endTime);
            case "event-stats" ->
                    eventStatsService.analyze(filePath, strParam(p, "eventType", null), startTime, endTime);
            case "jfr-rules" ->
                    rulesService.analyze(filePath, startTime, endTime, strParam(p, "minSeverity", "warning"));
            case "lock-analysis" -> lockAnalysisService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "native-memory" -> nativeMemService.analyze(filePath, startTime, endTime);
            case "object-statistics" ->
                    objectStatsService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "predictive-leak" ->
                    predictiveLeakService.analyze(filePath, startTime, endTime, doubleParam(p, "rSquaredThreshold", 0.85));
            case "recording-settings" -> {
                IItemCollection events = jfrProvider.loadRecording(filePath);
                yield settingsService.analyze(events);
            }
            case "request-waterfall" ->
                    waterfallService.analyze(filePath, startTime, endTime, strParam(p, "threadName", null), intParam(p, "maxEvents", 100));
            case "search-events" ->
                    searchService.search(filePath, startTime, endTime, strParam(p, "eventType", null), intParam(p, "limit", 50));
            case "jdbc-nplusone" -> nplusoneService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "lock-resolver" -> lockResolverService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "thread-starvation" ->
                    starvationService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "stack-trace-search" ->
                    stackSearchService.analyze(filePath, startTime, endTime, strParam(p, "classPattern", ".*"), strParam(p, "eventType", null), intParam(p, "limit", 100));
            case "thread-activity" ->
                    threadActivityService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "thread-allocation" ->
                    threadAllocService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "thread-contention" ->
                    threadContentionService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "thread-pool" -> threadPoolService.analyze(filePath, startTime, endTime, intParam(p, "topN", 20));
            case "time-series" ->
                    timeSeriesService.analyze(filePath, startTime, endTime, strParam(p, "bucketSize", "auto"), strParam(p, "metric", "cpu"));
            default -> throw new IllegalArgumentException("Unknown analysis type: " + analysisType);
        };
    }

    public Object expandCallTree(String treeId, String nodeId) throws Exception {
        return expandCallTreeService.expand(treeId, nodeId);
    }

    public Object compareRecordings(String baselinePath, String comparisonPath) throws Exception {
        return compareService.analyze(baselinePath, comparisonPath);
    }

    public Object compareRecordingsStructured(String baselinePath, String comparisonPath) throws Exception {
        return compareService.analyzeStructured(baselinePath, comparisonPath);
    }

    private int intParam(Map<String, Object> params, String key, int defaultValue) {
        Object val = params.get(key);
        if (val instanceof Number n) return n.intValue();
        if (val instanceof String s) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private double doubleParam(Map<String, Object> params, String key, double defaultValue) {
        Object val = params.get(key);
        if (val instanceof Number n) return n.doubleValue();
        if (val instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private String strParam(Map<String, Object> params, String key, String defaultValue) {
        Object val = params.get(key);
        return val != null ? val.toString() : defaultValue;
    }
}
