package io.github.deplague.jmcmcp.infrastructure.heapdump;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(HeapDumpUploadResource.class)
@TestProfile(HeapDumpTestProfile.class)
class HeapDumpUploadResourceTest {

    @Test
    void uploadHeapDump() throws Exception {
        File tempFile = Files.createTempFile("test", ".hprof").toFile();
        Files.writeString(tempFile.toPath(), "FAKE_HPROF_DATA");
        tempFile.deleteOnExit();

        given()
            .multiPart("file", tempFile, ContentType.BINARY.toString())
            .when()
            .post("/upload")
            .then()
            .statusCode(201)
            .body("success", is(true))
            .body("data.filename", equalTo(tempFile.getName()));
    }

    @Test
    void uploadWithoutFileReturnsError() {
        given()
            .when()
            .post("/upload")
            .then()
            .statusCode(415); // RESTEasy Reactive returns 415 for missing multipart body
    }

    @Test
    void listHeapDumpsAfterUpload() throws Exception {
        File tempFile = Files.createTempFile("test", ".hprof").toFile();
        Files.writeString(tempFile.toPath(), "FAKE_HPROF_DATA");
        tempFile.deleteOnExit();

        given()
            .multiPart("file", tempFile, ContentType.BINARY.toString())
            .post("/upload");

        given()
            .when()
            .get("/")
            .then()
            .statusCode(200)
            .body("success", is(true))
            .body("data.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void deleteHeapDump() throws Exception {
        File tempFile = Files.createTempFile("test", ".hprof").toFile();
        Files.writeString(tempFile.toPath(), "FAKE_HPROF_DATA");
        tempFile.deleteOnExit();

        String heapDumpId = given()
            .multiPart("file", tempFile, ContentType.BINARY.toString())
            .post("/upload")
            .then()
            .statusCode(201)
            .extract()
            .path("data.id");

        given()
            .when()
            .delete("/" + heapDumpId)
            .then()
            .statusCode(200)
            .body("success", is(true));

        given()
            .when()
            .get("/" + heapDumpId)
            .then()
            .statusCode(404);
    }
}
