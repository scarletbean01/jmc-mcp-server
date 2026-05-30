package io.github.deplague.jmcmcp.infrastructure.api;

import io.github.deplague.jmcmcp.application.service.RecordingStorageService;
import io.github.deplague.jmcmcp.infrastructure.api.model.ApiResponse;
import io.github.deplague.jmcmcp.infrastructure.api.model.CompareRequest;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

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
}
