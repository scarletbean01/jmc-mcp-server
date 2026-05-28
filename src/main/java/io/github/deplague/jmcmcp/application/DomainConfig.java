package io.github.deplague.jmcmcp.application;

import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.domain.service.AllocationFlameService;
import io.github.deplague.jmcmcp.domain.service.AllocationHotspotsService;
import io.github.deplague.jmcmcp.domain.service.BlockingSummaryService;
import io.github.deplague.jmcmcp.domain.service.ClassHistogramService;
import io.github.deplague.jmcmcp.domain.service.CpuFlameService;
import io.github.deplague.jmcmcp.domain.service.ClassLoadingService;
import io.github.deplague.jmcmcp.domain.service.CodeCacheService;
import io.github.deplague.jmcmcp.domain.service.ContainerMetricsService;
import io.github.deplague.jmcmcp.domain.service.DeadlockDetectionService;
import io.github.deplague.jmcmcp.domain.service.DirectBuffersService;
import io.github.deplague.jmcmcp.domain.service.EventSchemaService;
import io.github.deplague.jmcmcp.domain.service.ExceptionAnalysisService;
import io.github.deplague.jmcmcp.domain.service.GcCauseService;
import io.github.deplague.jmcmcp.domain.service.HotMethodsService;
import io.github.deplague.jmcmcp.domain.service.IncidentTimelineService;
import io.github.deplague.jmcmcp.domain.service.IoAnalysisService;
import io.github.deplague.jmcmcp.domain.service.JdkBugReferenceService;
import io.github.deplague.jmcmcp.domain.service.JfrEventStatsService;
import io.github.deplague.jmcmcp.domain.service.JfrOverviewService;
import io.github.deplague.jmcmcp.domain.service.JfrRulesService;
import io.github.deplague.jmcmcp.domain.service.JitCompilationService;
import io.github.deplague.jmcmcp.domain.service.LockFlameService;
import io.github.deplague.jmcmcp.domain.service.MemoryLeaksService;
import io.github.deplague.jmcmcp.domain.service.NativeMemoryService;
import io.github.deplague.jmcmcp.domain.service.ObjectStatisticsService;
import io.github.deplague.jmcmcp.domain.service.SafepointAnalysisService;
import io.github.deplague.jmcmcp.domain.service.ProcessInfoService;
import io.github.deplague.jmcmcp.domain.service.SearchEventsService;
import io.github.deplague.jmcmcp.domain.service.SystemPropertiesService;
import io.github.deplague.jmcmcp.domain.service.ThreadAllocationService;
import io.github.deplague.jmcmcp.domain.service.ThreadDumpService;
import io.github.deplague.jmcmcp.domain.service.VirtualThreadsService;
import io.github.deplague.jmcmcp.domain.service.VmOperationsService;
import io.github.deplague.jmcmcp.jfr.CallTreeCache;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * CDI producer methods for domain and infrastructure objects.
 * Keeps the domain layer free of framework annotations while making
 * instances available for injection.
 */
@ApplicationScoped
public class DomainConfig {

    @Produces
    @ApplicationScoped
    public HotMethodsService hotMethodsService() {
        return new HotMethodsService();
    }

    @Produces
    @ApplicationScoped
    public SystemPropertiesService systemPropertiesService() {
        return new SystemPropertiesService();
    }

    @Produces
    @ApplicationScoped
    public ProcessInfoService processInfoService() {
        return new ProcessInfoService();
    }

    @Produces
    @ApplicationScoped
    public VmOperationsService vmOperationsService() {
        return new VmOperationsService();
    }

    @Produces
    @ApplicationScoped
    public ClassLoadingService classLoadingService() {
        return new ClassLoadingService();
    }

    @Produces
    @ApplicationScoped
    public CodeCacheService codeCacheService() {
        return new CodeCacheService();
    }

    @Produces
    @ApplicationScoped
    public ObjectStatisticsService objectStatisticsService() {
        return new ObjectStatisticsService();
    }

    @Produces
    @ApplicationScoped
    public EventSchemaService eventSchemaService() {
        return new EventSchemaService();
    }

    @Produces
    @ApplicationScoped
    public NativeMemoryService nativeMemoryService() {
        return new NativeMemoryService();
    }

    @Produces
    @ApplicationScoped
    public ContainerMetricsService containerMetricsService() {
        return new ContainerMetricsService();
    }

    @Produces
    @ApplicationScoped
    public DirectBuffersService directBuffersService() {
        return new DirectBuffersService();
    }

    @Produces
    @ApplicationScoped
    public GcCauseService gcCauseService() {
        return new GcCauseService();
    }

    @Produces
    @ApplicationScoped
    public ThreadAllocationService threadAllocationService() {
        return new ThreadAllocationService();
    }

    @Produces
    @ApplicationScoped
    public ThreadDumpService threadDumpService() {
        return new ThreadDumpService();
    }

    @Produces
    @ApplicationScoped
    public SearchEventsService searchEventsService() {
        return new SearchEventsService();
    }

    @Produces
    @ApplicationScoped
    public ClassHistogramService classHistogramService() {
        return new ClassHistogramService();
    }

    @Produces
    @ApplicationScoped
    public ExceptionAnalysisService exceptionAnalysisService() {
        return new ExceptionAnalysisService();
    }

    @Produces
    @ApplicationScoped
    public JfrEventStatsService jfrEventStatsService() {
        return new JfrEventStatsService();
    }

    @Produces
    @ApplicationScoped
    public JfrRulesService jfrRulesService() {
        return new JfrRulesService();
    }

    @Produces
    @ApplicationScoped
    public IoAnalysisService ioAnalysisService() {
        return new IoAnalysisService();
    }

    @Produces
    @ApplicationScoped
    public VirtualThreadsService virtualThreadsService() {
        return new VirtualThreadsService();
    }

    @Produces
    @ApplicationScoped
    public JitCompilationService jitCompilationService() {
        return new JitCompilationService();
    }

    @Produces
    @ApplicationScoped
    public DeadlockDetectionService deadlockDetectionService() {
        return new DeadlockDetectionService();
    }

    @Produces
    @ApplicationScoped
    public JdkBugReferenceService jdkBugReferenceService() {
        return new JdkBugReferenceService();
    }

    @Produces
    @ApplicationScoped
    public BlockingSummaryService blockingSummaryService() {
        return new BlockingSummaryService();
    }

    @Produces
    @ApplicationScoped
    public JfrOverviewService jfrOverviewService() {
        return new JfrOverviewService();
    }

    @Produces
    @ApplicationScoped
    public SafepointAnalysisService safepointAnalysisService() {
        return new SafepointAnalysisService();
    }

    @Produces
    @ApplicationScoped
    public IncidentTimelineService incidentTimelineService() {
        return new IncidentTimelineService();
    }

    @Produces
    @ApplicationScoped
    public MemoryLeaksService memoryLeaksService() {
        return new MemoryLeaksService();
    }

    @Produces
    @ApplicationScoped
    public AllocationHotspotsService allocationHotspotsService() {
        return new AllocationHotspotsService();
    }

    @Produces
    @ApplicationScoped
    public AllocationFlameService allocationFlameService() {
        return new AllocationFlameService();
    }

    @Produces
    @ApplicationScoped
    public CpuFlameService cpuFlameService() {
        return new CpuFlameService();
    }

    @Produces
    @ApplicationScoped
    public LockFlameService lockFlameService() {
        return new LockFlameService();
    }

    @Produces
    @ApplicationScoped
    public JfrRecordingCache jfrRecordingCache() {
        return new JfrRecordingCache();
    }

    @Produces
    @ApplicationScoped
    public RecordingAccessController recordingAccessController() {
        return new RecordingAccessController();
    }

    @Produces
    @ApplicationScoped
    public CallTreeCache callTreeCache() {
        return new CallTreeCache();
    }

    @Produces
    @ApplicationScoped
    public AsyncJobService asyncJobService() {
        return new AsyncJobService();
    }
}
