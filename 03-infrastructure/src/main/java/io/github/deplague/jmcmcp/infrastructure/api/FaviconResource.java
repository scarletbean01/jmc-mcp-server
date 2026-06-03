package io.github.deplague.jmcmcp.infrastructure.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;

@Path("/favicon.ico")
public class FaviconResource {

    @GET
    @Produces("image/x-icon")
    public Response getFavicon() {
        InputStream is = getClass().getResourceAsStream("/META-INF/resources/favicon.ico");
        if (is == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(is).build();
    }
}
