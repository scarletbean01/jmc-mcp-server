package io.github.deplague.jmcmcp.infrastructure.heapdump;

import io.github.deplague.jmcmcp.infrastructure.api.model.ApiResponse;
import io.github.deplague.jmcmcp.infrastructure.api.model.UploadResponse;
import io.github.deplague.jmcmcp.infrastructure.jfr.RecordingStorageService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

@Path("/api/v1/heap-dumps")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class HeapDumpUploadResource {

    private final HeapDumpStorageService heapDumpStorage;
    private final RecordingStorageService recordingStorage;

    @RunOnVirtualThread
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@RestForm("file") FileUpload file) {
        try {
            if (file == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("No file provided"))
                        .build();
            }

            UploadResponse result;
            if (file.uploadedFile() != null && Files.exists(file.uploadedFile())) {
                try (InputStream is = Files.newInputStream(file.uploadedFile())) {
                    result = heapDumpStorage.storeHeapDump(
                            file.fileName(),
                            is,
                            Files.size(file.uploadedFile())
                    );
                }
            } else if (file.filePath() != null && Files.exists(file.filePath())) {
                try (InputStream is = Files.newInputStream(file.filePath())) {
                    result = heapDumpStorage.storeHeapDump(
                            file.fileName(),
                            is,
                            Files.size(file.filePath())
                    );
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("File upload failed — no content received"))
                        .build();
            }

            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.ok(result))
                    .build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(ApiResponse.error("Upload failed: " + e.getMessage()))
                    .build();
        }
    }

    @RunOnVirtualThread
    @GET
    public Response list() {
        return Response.ok(ApiResponse.ok(heapDumpStorage.listHeapDumps())).build();
    }

    @RunOnVirtualThread
    @GET
    @Path("/{heapDumpId}")
    public Response info(@PathParam("heapDumpId") String heapDumpId) {
        var info = heapDumpStorage.getHeapDumpInfo(heapDumpId);
        if (info == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Heap dump not found: " + heapDumpId))
                    .build();
        }
        return Response.ok(ApiResponse.ok(info)).build();
    }

    @RunOnVirtualThread
    @DELETE
    @Path("/{heapDumpId}")
    public Response delete(@PathParam("heapDumpId") String heapDumpId) {
        boolean deleted = heapDumpStorage.deleteHeapDump(heapDumpId);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Heap dump not found: " + heapDumpId))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Deleted")).build();
    }

    @RunOnVirtualThread
    @POST
    @Path("/{heapDumpId}/link/{recordingId}")
    public Response link(
            @PathParam("heapDumpId") String heapDumpId,
            @PathParam("recordingId") String recordingId
    ) {
        String recordingPath = recordingStorage.getRecordingPath(recordingId);
        if (recordingPath == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Recording not found: " + recordingId))
                    .build();
        }
        try {
            recordingStorage.associateHeapDump(recordingId, heapDumpId);
            return Response.ok(ApiResponse.ok("Linked")).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }

    @RunOnVirtualThread
    @GET
    @Path("/{heapDumpId}/linked-recording")
    public Response getLinkedRecording(@PathParam("heapDumpId") String heapDumpId) {
        String recordingId = recordingStorage.getAssociatedRecordingId(heapDumpId);
        if (recordingId == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("No recording linked to heap dump: " + heapDumpId))
                    .build();
        }
        return Response.ok(ApiResponse.ok(Map.of("recordingId", recordingId))).build();
    }
}
