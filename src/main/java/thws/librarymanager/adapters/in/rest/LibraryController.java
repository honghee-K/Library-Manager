package thws.librarymanager.adapters.in.rest;

// jakarta webservice ODER jax-rs

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.in.LibraryUseCase;
import thws.librarymanager.adapters.in.rest.models.Link;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/libraries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LibraryController {

    private final LibraryUseCase libraryUseCase;

    @Context
    UriInfo uriInfo;

    @Inject
    public LibraryController(LibraryUseCase libraryUseCase) {
        this.libraryUseCase = libraryUseCase;
    }

    // GET ALL LIBRARIES
    @GET
    public Response getAllLibraries() {

        List<Library> libraries = libraryUseCase.getAllLibraries();

        List<LibraryResponse> response = libraries.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return Response.ok(response).build();
    }

    // GET LIBRARY BY ID
    @GET
    @Path("/{id}")
    public Response getLibraryById(@PathParam("id") Long id) {

        Optional<Library> library = libraryUseCase.getLibraryById(id);

        return library
                .map(l -> Response.ok(toResponse(l)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }


    // HATEOAS MAPPING
    private LibraryResponse toResponse(Library library) {

        LibraryResponse response = new LibraryResponse(library);

        Long id = library.getId();

        // self link
        response.addLink(
                "self",
                uriInfo.getBaseUriBuilder()
                        .path(LibraryController.class)
                        .path(LibraryController.class, "getLibraryById")
                        .build(id)
                        .toString()
        );

        // relation: books of this library
        response.addLink(
                "books",
                uriInfo.getBaseUriBuilder()
                        .path("/books")
                        .queryParam("libraryId", id)
                        .build()
                        .toString()
        );

        return response;
    }


    // RESPONSE DTO
    public static class LibraryResponse {

        public Long id;
        public String name;
        public String location;
        public final java.util.Map<String, Link> _links = new java.util.HashMap<>();

        public LibraryResponse(Library library) {
            this.id = library.getId();
            this.name = library.getName();
            this.location = library.getLocation();
        }

        public void addLink(String rel, String href) {
            _links.put(rel, new Link(href, "GET","application/json"));
        }
    }
}
