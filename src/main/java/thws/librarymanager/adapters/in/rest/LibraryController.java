package thws.librarymanager.adapters.in.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import thws.librarymanager.adapters.in.rest.mapper.RestMapper;
import thws.librarymanager.adapters.in.rest.models.LibraryDTO;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.in.LibraryUseCase;

@Path("/libraries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LibraryController {

    @Inject
    LibraryUseCase libraryUseCase;

    @Inject
    RestMapper mapper;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAllLibraries() {

        List<LibraryDTO> dtos = libraryUseCase.getAllLibraries().stream()
                .map(library -> mapper.toLibraryDTO(library, uriInfo))
                .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    public Response getLibraryById(@PathParam("id") Long id) {

        Optional<Library> library = libraryUseCase.getLibraryById(id);

        return library.map(l -> mapper.toLibraryDTO(l, uriInfo))
                .map(dto -> Response.ok(dto).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
