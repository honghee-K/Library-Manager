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
import thws.librarymanager.adapters.in.rest.util.LibraryServiceLogger;
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

        LibraryServiceLogger.logGetAll();

        return Response.ok(dtos)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=60")
                .build();
    }



    @GET
    @Path("/{id}")
    public Response getLibraryById(@PathParam("id") Long id) {

        Optional<Library> library = libraryUseCase.getLibraryById(id);

        if (library.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        LibraryDTO dto = mapper.toLibraryDTO(library.get(), uriInfo);

        CacheControl cacheControl = new CacheControl();
        cacheControl.setPrivate(true);
        cacheControl.setMaxAge(30);

        LibraryServiceLogger.logGetAll();

        return Response.ok(dto)
                .cacheControl(cacheControl)
                .build();
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



    @PUT
    @Path("/{id}")
    public Response updateLibrary(
            @PathParam("id") Long id,
            LibraryDTO dto) {

        libraryUseCase.updateLibrary(
                id,
                dto.getName(),
                dto.getLocation()
        );

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLibrary(@PathParam("id") Long id) {

        libraryUseCase.deleteLibrary(id);

        return Response.noContent().build();
    }


    @POST
    @Path("/{libraryId}/books/{isbn}")
    @Consumes(MediaType.WILDCARD)
    public Response addBookToLibrary(
            @PathParam("libraryId") Long libraryId,
            @PathParam("isbn") Long isbn) {

        libraryUseCase.addBookToLibrary(libraryId, isbn);

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{libraryId}/books/{isbn}")
    @Consumes(MediaType.WILDCARD)
    public Response removeBookFromLibrary(
            @PathParam("libraryId") Long libraryId,
            @PathParam("isbn") Long isbn) {

        libraryUseCase.removeBookFromLibrary(libraryId, isbn);

        return Response.noContent().build();
    }

    @GET
    @Path("/{libraryId}/books/count")
    public Response getTotalBookCount(
            @PathParam("libraryId") Long libraryId) {

        Long count = libraryUseCase.getTotalBookCount(libraryId);

        CacheControl cacheControl = new CacheControl();
        cacheControl.setPrivate(true);  // library’ye özel
        cacheControl.setMaxAge(30);      // 30 saniye

        return Response.ok(count)
                .cacheControl(cacheControl)
                .build();
    }


}
