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
public class LibraryController extends BaseController{

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
                .map(lib -> mapper.toLibraryDTO(lib))
                .collect(Collectors.toList());

        LibraryServiceLogger.logGetAll();

        Response.ResponseBuilder rb = Response.ok(dtos);

        addLink(rb, uriInfo.getAbsolutePath(), "self");

        CacheControl cc = new CacheControl();
        cc.setMaxAge(60);
        cc.setPrivate(false);
        rb.cacheControl(cc);

        return rb.build();
    }

//
/*    @GET
    public Response getAllLibraries(
            @QueryParam("location") String location,
            @QueryParam("name") String name) {


        List<Library> libraries = libraryUseCase.getAllLibraries(location, name);
        List<LibraryDTO> dtos = libraries.stream()
                .map(lib -> mapper.toLibraryDTO(lib))
                .collect(Collectors.toList());

        LibraryServiceLogger.logGetAll();

        Response.ResponseBuilder rb = Response.ok(dtos);

        addLink(rb, uriInfo.getAbsolutePath(), "self");

        CacheControl cc = new CacheControl();
        cc.setMaxAge(60);
        cc.setPrivate(false);
        rb.cacheControl(cc);

        return rb.build();
    }*/

    @GET
    @Path("/{id}")
    public Response getLibraryById(@PathParam("id") Long id) {

        Optional<Library> library = libraryUseCase.getLibraryById(id);

        if (library.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        LibraryDTO dto = mapper.toLibraryDTO(library.get());

        Response.ResponseBuilder rb = Response.ok(dto);
        URI selfUri = uriInfo.getAbsolutePath();
        addLink(rb, selfUri, "self");
        addLink(rb, selfUri, "update");
        addLink(rb, selfUri, "delete");

        URI booksInLibUri = uriInfo.getBaseUriBuilder()
                .path(BookController.class)
                .queryParam("libraryId", id)
                .build();
        addLink(rb, booksInLibUri, "books");

        CacheControl cacheControl = new CacheControl();
        cacheControl.setPrivate(true);
        cacheControl.setMaxAge(30);
        rb.cacheControl(cacheControl);

        LibraryServiceLogger.logGetAll();

        return rb.build();
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

        Response.ResponseBuilder rb = Response.created(uri);
        addLink(rb, uri, "self");

        return rb.entity(mapper.toLibraryDTO(saved)).build(); // 201 CREATED
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

        Response.ResponseBuilder rb = Response.noContent();
        addLink(rb, uriInfo.getAbsolutePath(), "self");

        return rb.build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLibrary(@PathParam("id") Long id) {

        libraryUseCase.deleteLibrary(id);

        URI collectionUri = uriInfo.getBaseUriBuilder().path(LibraryController.class).build();
        Response.ResponseBuilder rb = Response.noContent();
        addLink(rb, collectionUri, "collection");

        return rb.build();
    }


    @POST
    @Path("/{libraryId}/books/{isbn}")
    @Consumes(MediaType.WILDCARD)
    public Response addBookToLibrary(
            @PathParam("libraryId") Long libraryId,
            @PathParam("isbn") Long isbn) {

        libraryUseCase.addBookToLibrary(libraryId, isbn);

        URI libraryUri = uriInfo.getBaseUriBuilder()
                .path(LibraryController.class)
                .path(String.valueOf(libraryId))
                .build();

        Response.ResponseBuilder rb = Response.noContent();
        addLink(rb, libraryUri, "library");

        return rb.build();
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

        Response.ResponseBuilder rb = Response.ok(count);

        CacheControl cacheControl = new CacheControl();
        cacheControl.setPrivate(true);  // library’ye özel
        cacheControl.setMaxAge(30);      // 30 saniye

        rb.cacheControl(cacheControl);

        return rb.build();
    }


}
