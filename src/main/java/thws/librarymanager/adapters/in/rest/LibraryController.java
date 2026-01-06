package thws.librarymanager.adapters.in.rest;


import java.net.URI;
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
    public Response getAllLibraries(
            @QueryParam("location") String location,
            @QueryParam("name") String name
    ) {

        List<LibraryDTO> dtos = libraryUseCase
                .getAllLibraries(location, name)
                .stream()
                .map(lib -> mapper.toLibraryDTO(lib, uriInfo))
                .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    public Response getLibraryById(@PathParam("id") Long id) {

        Optional<Library> library = libraryUseCase.getLibraryById(id);

        return library
                .map(lib -> mapper.toLibraryDTO(lib, uriInfo))
                .map(dto -> Response.ok(dto).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
    @POST
    public Response addLibrary(LibraryDTO dto) {

        Library library = new Library(
                null,
                dto.getName(),
                dto.getLocation(),
                null
        );

        Library saved = libraryUseCase.addLibrary(library);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(saved.getId()))
                .build();

        return Response
                .created(uri) // 201 CREATED
                .entity(mapper.toLibraryDTO(saved, uriInfo))
                .build();
    }
}
