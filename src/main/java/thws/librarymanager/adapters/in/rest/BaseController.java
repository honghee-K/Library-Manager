package thws.librarymanager.adapters.in.rest;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

public abstract class BaseController {

    protected void addLink(Response.ResponseBuilder rb, URI uri, String rel) {
        String link = String.format("<%s>; rel=\"%s\"; type=\"%s\"",
                uri.toString(), rel, MediaType.APPLICATION_JSON);
        rb.header("Link", link);
    }

    protected void addRelationLink(Response.ResponseBuilder rb, UriInfo uriInfo,
                                   Class<?> resourceClass, String methodName, Object id, String rel) {
        URI relationUri = uriInfo.getBaseUriBuilder()
                .path(resourceClass)
                .path(resourceClass, methodName)
                .build(id);
        addLink(rb, relationUri, rel);
    }
}