package io.github.deplague.jmcmcp.infrastructure.api;

import io.github.deplague.jmcmcp.application.service.RecordingStorageService;
import io.github.deplague.jmcmcp.infrastructure.api.model.ApiResponse;
import io.github.deplague.jmcmcp.infrastructure.api.model.CompareRequest;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * REST resource for comparing two JFR recordings.
 */
@Path("/api/v1/compare")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ComparisonResource {

    private final AnalysisDispatcher dispatcher;
    private final RecordingStorageService storageService;


    @RunOnVirtualThread
    @POST
    public Response compare(CompareRequest request) {
        String baselinePath = storageService.getRecordingPath(request.baselineRecordingId());
        String comparisonPath = storageService.getRecordingPath(request.comparisonRecordingId());

        if (baselinePath == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Baseline recording not found: " + request.baselineRecordingId()))
                    .build();
        }
        if (comparisonPath == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Comparison recording not found: " + request.comparisonRecordingId()))
                    .build();
        }

        try {
            Object result = dispatcher.compareRecordings(baselinePath, comparisonPath);
            return Response.ok(ApiResponse.ok(result)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(ApiResponse.error("Comparison failed: " + e.getMessage()))
                    .build();
        }
    }

    @RunOnVirtualThread
    @POST
    @Path("/structured")
    public Response compareStructured(CompareRequest request) {
        String baselinePath = storageService.getRecordingPath(request.baselineRecordingId());
        String comparisonPath = storageService.getRecordingPath(request.comparisonRecordingId());

        if (baselinePath == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Baseline recording not found: " + request.baselineRecordingId()))
                    .build();
        }
        if (comparisonPath == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Comparison recording not found: " + request.comparisonRecordingId()))
                    .build();
        }

        try {
            Object result = dispatcher.compareRecordingsStructured(baselinePath, comparisonPath);
            return Response.ok(ApiResponse.ok(result)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(ApiResponse.error("Comparison failed: " + e.getMessage()))
                    .build();
        }
    }

    @RunOnVirtualThread
    @POST
    @Path("/call-tree")
    public Response diffCallTree(CompareRequest request) {
        String baselinePath = storageService.getRecordingPath(request.baselineRecordingId());
        String comparisonPath = storageService.getRecordingPath(request.comparisonRecordingId());

        if (baselinePath == null || comparisonPath == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            Map<String, Object> p = request.params();
            Object result = dispatcher.diffCallTree(
                    baselinePath, comparisonPath,
                    (String) p.getOrDefault("subsystem", "cpu"),
                    (String) p.get("packageFilter"));
            return Response.ok(ApiResponse.ok(result)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(ApiResponse.error("Diff call tree failed: " + e.getMessage()))
                    .build();
        }
    }

    @RunOnVirtualThread
    @POST
    @Path("/stack-traces")
    public Response diffStackTraces(CompareRequest request) {
        String baselinePath = storageService.getRecordingPath(request.baselineRecordingId());
        String comparisonPath = storageService.getRecordingPath(request.comparisonRecordingId());

        if (baselinePath == null || comparisonPath == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            Map<String, Object> p = request.params();
            Object result = dispatcher.diffStackTraces(
                    baselinePath, comparisonPath,
                    (String) p.get("packagePrefix"),
                    (int) p.getOrDefault("topN", 20));
            return Response.ok(ApiResponse.ok(result)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(ApiResponse.error("Diff stack traces failed: " + e.getMessage()))
                    .build();
        }
    }

    @RunOnVirtualThread
    @POST
    @Path("/call-tree/{treeId}/expand")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response expandDiffCallTree(
            @PathParam("treeId") String treeId,
            @QueryParam("nodeId") String nodeId
    ) {
        try {
            Object result = dispatcher.expandDiffCallTree(treeId, nodeId);
            return Response.ok(ApiResponse.ok(result)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(ApiResponse.error("Expansion failed: " + e.getMessage()))
                    .build();
        }
    }
}
