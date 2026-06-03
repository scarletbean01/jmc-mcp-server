package io.github.deplague.jmcmcp.infrastructure.heapdump;

import io.github.deplague.jmcmcp.infrastructure.api.model.AnalysisRequest;
import io.github.deplague.jmcmcp.infrastructure.api.model.ApiResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Path("/api/v1/heap-dumps/{heapDumpId}/analyze")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class HeapDumpAnalysisResource {

    private final HeapDumpStorageService storageService;
    private final HeapDumpAnalysisDispatcher dispatcher;

    @RunOnVirtualThread
    @POST
    @Path("/{analysisType}")
    public Response analyze(
            @PathParam("heapDumpId") String heapDumpId,
            @PathParam("analysisType") String analysisType,
            AnalysisRequest request
    ) {
        String filePath = storageService.getHeapDumpPath(heapDumpId);
        if (filePath == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Heap dump not found: " + heapDumpId))
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
    @Path("/dominator-tree/{treeId}/expand")
    public Response expandDominatorTree(
            @PathParam("heapDumpId") String heapDumpId,
            @PathParam("treeId") String treeId,
            @QueryParam("nodeId") String nodeId
    ) {
        String filePath = storageService.getHeapDumpPath(heapDumpId);
        if (filePath == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Heap dump not found: " + heapDumpId))
                    .build();
        }
        if (nodeId == null || nodeId.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("nodeId is required"))
                    .build();
        }

        try {
            Object result = dispatcher.expandDominatorNode(treeId, nodeId);
            return Response.ok(ApiResponse.ok(result)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(ApiResponse.error("Expansion failed: " + e.getMessage()))
                    .build();
        }
    }
}
