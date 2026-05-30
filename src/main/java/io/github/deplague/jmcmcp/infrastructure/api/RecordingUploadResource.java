package io.github.deplague.jmcmcp.infrastructure.api;

import io.github.deplague.jmcmcp.application.service.RecordingStorageService;
import io.github.deplague.jmcmcp.infrastructure.api.model.ApiResponse;
import io.github.deplague.jmcmcp.infrastructure.api.model.RecordingInfo;
import io.github.deplague.jmcmcp.infrastructure.api.model.UploadResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.InputStream;
import java.nio.file.Files;

/**
 * REST resource for JFR recording upload and management.
 * Uses RESTEasy Reactive multipart support.
 */
@Path("/api/v1/recordings")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RecordingUploadResource {

    private final RecordingStorageService storageService;

    @RunOnVirtualThread
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadRecording(
            @org.jboss.resteasy.reactive.RestForm("file") FileUpload file
    ) {
        try {
            if (file == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("No file provided"))
                        .build();
            }

            UploadResponse response;
            if (file.uploadedFile() != null && Files.exists(file.uploadedFile())) {
                // File was saved to disk by RESTEasy Reactive
                try (InputStream is = Files.newInputStream(file.uploadedFile())) {
                    response = storageService.storeRecording(
                            file.fileName(),
                            is,
                            Files.size(file.uploadedFile())
                    );
                }
            } else if (file.filePath() != null && Files.exists(file.filePath())) {
                // Alternative path
                try (InputStream is = Files.newInputStream(file.filePath())) {
                    response = storageService.storeRecording(
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
                    .entity(ApiResponse.ok(response))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Upload failed: " + e.getMessage()))
                    .build();
        }
    }

    @RunOnVirtualThread
    @GET
    @Path("/{id}")
    public Response getRecording(@PathParam("id") String recordingId) {
        try {
            RecordingInfo info = storageService.getRecordingInfo(recordingId);
            if (info == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Recording not found: " + recordingId))
                        .build();
            }
            return Response.ok(ApiResponse.ok(info)).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(ApiResponse.error("Failed to get recording: " + e.getMessage()))
                    .build();
        }
    }

    @RunOnVirtualThread
    @DELETE
    @Path("/{id}")
    public Response deleteRecording(@PathParam("id") String recordingId) {
        boolean deleted = storageService.deleteRecording(recordingId);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Recording not found: " + recordingId))
                    .build();
        }
        return Response.noContent().build();
    }
}
