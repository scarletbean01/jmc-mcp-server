package io.github.deplague.jmcmcp.infrastructure.api;

import io.github.deplague.jmcmcp.application.service.AsyncJobService;
import io.github.deplague.jmcmcp.application.service.RecordingStorageService;
import io.github.deplague.jmcmcp.domain.model.AsyncJob;
import io.github.deplague.jmcmcp.infrastructure.api.model.AnalysisRequest;
import io.github.deplague.jmcmcp.infrastructure.api.model.ApiResponse;
import io.github.deplague.jmcmcp.infrastructure.api.model.JobStatusResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST resource for JFR analysis operations.
 * Supports synchronous, asynchronous, and SSE streaming analysis.
 */
@Path("/api/v1/recordings/{recordingId}/analyze")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AnalysisResource {

    private final AnalysisDispatcher dispatcher;
    private final RecordingStorageService storageService;
    private final AsyncJobService jobService;

    @RunOnVirtualThread
    @POST
    @Path("/{analysisType}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response analyze(
            @PathParam("recordingId") String recordingId,
            @PathParam("analysisType") String analysisType,
            AnalysisRequest request
    ) {
        String filePath = storageService.getRecordingPath(recordingId);
        if (filePath == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Recording not found: " + recordingId))
                    .build();
        }

        try {
            Object result = dispatcher.dispatch(analysisType, filePath, request);
            return Response.ok(ApiResponse.ok(result)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(ApiResponse.error("Analysis failed: " + e.getMessage()))
                    .build();
        }
    }

    @RunOnVirtualThread
    @POST
    @Path("/{analysisType}/async")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response analyzeAsync(
            @PathParam("recordingId") String recordingId,
            @PathParam("analysisType") String analysisType,
            AnalysisRequest request
    ) {
        String filePath = storageService.getRecordingPath(recordingId);
        if (filePath == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Recording not found: " + recordingId))
                    .build();
        }

        AsyncJob job = jobService.createJob(recordingId, analysisType);

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                jobService.updateJobStatus(job.jobId(), "RUNNING", 10);
                Object result = dispatcher.dispatch(analysisType, filePath, request);
                jobService.completeJob(job.jobId(), result);
            } catch (Exception e) {
                jobService.failJob(job.jobId(), e.getMessage());
            }
        });

        jobService.setJobFuture(job.jobId(), future);

        return Response.status(Response.Status.ACCEPTED)
                .entity(ApiResponse.ok(Map.of("jobId", job.jobId(), "status", "PENDING")))
                .build();
    }

    @RunOnVirtualThread
    @GET
    @Path("/jobs/{jobId}")
    public Response getJobStatus(@PathParam("jobId") String jobId) {
        AsyncJob job = jobService.getJob(jobId);
        if (job == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Job not found: " + jobId))
                    .build();
        }

        JobStatusResponse status = new JobStatusResponse(
                job.jobId(),
                job.status(),
                job.result(),
                job.error(),
                job.createdAt(),
                job.completedAt(),
                job.progressPercent()
        );
        return Response.ok(ApiResponse.ok(status)).build();
    }

    @GET
    @Path("/jobs/{jobId}/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void streamJob(
            @PathParam("jobId") String jobId,
            SseEventSink sink,
            @Context Sse sse
    ) {
        if (sink == null) {
            return;
        }

        jobService.subscribe(jobId, status -> {
            try {
                OutboundSseEvent event = sse.newEventBuilder()
                        .name("job-update")
                        .mediaType(MediaType.APPLICATION_JSON_TYPE)
                        .data(ApiResponse.ok(status))
                        .build();
                sink.send(event);

                if ("COMPLETED".equals(status.status()) || "FAILED".equals(status.status())) {
                    sink.close();
                    jobService.unsubscribe(jobId);
                }
            } catch (Exception e) {
                sink.close();
                jobService.unsubscribe(jobId);
            }
        });
    }

    @RunOnVirtualThread
    @POST
    @Path("/call-tree/{treeId}/expand")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response expandCallTree(
            @PathParam("recordingId") String recordingId,
            @PathParam("treeId") String treeId,
            @QueryParam("nodeId") String nodeId,
            AnalysisRequest request
    ) {
        String filePath = storageService.getRecordingPath(recordingId);
        if (filePath == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Recording not found: " + recordingId))
                    .build();
        }

        try {
            Object result = dispatcher.expandCallTree(treeId, nodeId);
            return Response.ok(ApiResponse.ok(result)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(ApiResponse.error("Expansion failed: " + e.getMessage()))
                    .build();
        }
    }
}
