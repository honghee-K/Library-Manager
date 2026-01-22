package thws.librarymanager.adapters.in.rest;

import java.net.URI;
import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import thws.librarymanager.adapters.in.rest.mapper.RestMapper;
import thws.librarymanager.adapters.in.rest.models.LibraryDTO;
import thws.librarymanager.adapters.in.rest.services.JwtAuthService;
import thws.librarymanager.adapters.in.rest.util.ETagGenerator;
import thws.librarymanager.adapters.in.rest.util.LibraryServiceLogger;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.in.LibraryUseCase;

@Path("/libraries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LibraryController extends BaseController {

    @Inject
    LibraryUseCase libraryUseCase;

    @Inject
    RestMapper mapper;

    @Context
    UriInfo uriInfo;

    @Context
    Request request;

    @RolesAllowed(JwtAuthService.Librarian_ROLE)
    @GET
    public Response getAllLibraries(@QueryParam("location") String location, @QueryParam("name") String name) {

        List<LibraryDTO> dtos = libraryUseCase.getAllLibraries(location, name).stream()
                .map(mapper::toLibraryDTO)
                .toList();

        LibraryServiceLogger.logGetAll();

        Response.ResponseBuilder rb = Response.ok(dtos);
        addLink(rb, uriInfo.getAbsolutePath(), "self");

        CacheControl cc = new CacheControl();
        cc.setPrivate(true);
        cc.setMaxAge(30);
        rb.cacheControl(cc);

        return rb.build();
    }

    @RolesAllowed(JwtAuthService.Librarian_ROLE)
    @GET
    @Path("/{id}")
    @Transactional
    public Response getLibraryById(@PathParam("id") Long id) {

        Library library =
                libraryUseCase.getLibraryById(id).orElseThrow(() -> new NotFoundException("Library not found"));

        EntityTag etag = new EntityTag(ETagGenerator.fromLibrary(library));

        Response.ResponseBuilder precond = request.evaluatePreconditions(etag);

        if (precond != null) {
            return precond.build();
        }

        LibraryDTO dto = mapper.toLibraryDTO(library);
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

        LibraryServiceLogger.logGetAll();

        return rb.tag(etag).build();
    }

    @RolesAllowed(JwtAuthService.Librarian_ROLE)
    @POST
    @Transactional
    public Response addLibrary(LibraryDTO dto) {

        Library library = new Library(null, dto.getName(), dto.getLocation(), null);

        Library saved = libraryUseCase.addLibrary(library);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(saved.getId()))
                .build();

        Response.ResponseBuilder rb = Response.created(uri);
        addLink(rb, uri, "self");

        return rb.entity(mapper.toLibraryDTO(saved)).build();
    }

    @RolesAllowed(JwtAuthService.Librarian_ROLE)
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateLibrary(@PathParam("id") Long id, LibraryDTO dto) {

        Library existing =
                libraryUseCase.getLibraryById(id).orElseThrow(() -> new NotFoundException("Library not found"));

        EntityTag etag = new EntityTag(ETagGenerator.fromLibrary(existing));
        Response.ResponseBuilder precond = request.evaluatePreconditions(etag);

        if (precond != null) {
            return precond.build();
        }

        libraryUseCase.updateLibrary(id, dto.getName(), dto.getLocation());

        Response.ResponseBuilder rb = Response.noContent();
        addLink(rb, uriInfo.getAbsolutePath(), "self");

        EntityTag newTag = new EntityTag(
                ETagGenerator.fromLibrary(libraryUseCase.getLibraryById(id).get()));

        return rb.tag(newTag).build();
    }

    @RolesAllowed(JwtAuthService.Librarian_ROLE)
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteLibrary(@PathParam("id") Long id) {

        libraryUseCase.deleteLibrary(id);
        return Response.noContent().build();
    }

    @RolesAllowed(JwtAuthService.Librarian_ROLE)
    @POST
    @Path("/{libraryId}/books/{isbn}")
    @Consumes(MediaType.WILDCARD)
    @Transactional
    public Response addBookToLibrary(@PathParam("libraryId") Long libraryId, @PathParam("isbn") Long isbn) {

        libraryUseCase.addBookToLibrary(libraryId, isbn);

        URI libraryUri = uriInfo.getBaseUriBuilder()
                .path(LibraryController.class)
                .path(String.valueOf(libraryId))
                .build();

        Response.ResponseBuilder rb = Response.noContent();
        addLink(rb, libraryUri, "library");

        return rb.build();
    }

    @RolesAllowed(JwtAuthService.Librarian_ROLE)
    @DELETE
    @Path("/{libraryId}/books/{isbn}")
    @Consumes(MediaType.WILDCARD)
    @Transactional
    public Response removeBookFromLibrary(@PathParam("libraryId") Long libraryId, @PathParam("isbn") Long isbn) {

        libraryUseCase.removeBookFromLibrary(libraryId, isbn);
        return Response.noContent().build();
    }

    @RolesAllowed(JwtAuthService.Librarian_ROLE)
    @GET
    @Path("/{libraryId}/books/count")
    public Response getTotalBookCount(@PathParam("libraryId") Long libraryId) {

        Long count = libraryUseCase.getTotalBookCount(libraryId);

        Response.ResponseBuilder rb = Response.ok(count);

        CacheControl cacheControl = new CacheControl();
        cacheControl.setPrivate(true);
        cacheControl.setMaxAge(30);
        rb.cacheControl(cacheControl);

        return rb.build();
    }
}
