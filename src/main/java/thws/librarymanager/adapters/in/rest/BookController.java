package thws.librarymanager.adapters.in.rest;

import java.net.URI;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import thws.librarymanager.adapters.in.rest.mapper.RestMapper;
import thws.librarymanager.adapters.in.rest.models.BookDTO;
import thws.librarymanager.adapters.in.rest.util.ETagGenerator;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.in.LibraryUseCase;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController extends BaseController{

    @Context
    UriInfo uriInfo;

    @Inject
    private BookUseCase bookUseCase;

    @Inject
    private LibraryUseCase libraryUseCase;

    @Inject
    private RestMapper restMapper;


    @Inject
    public BookController(BookUseCase bookUseCase) {
        this.bookUseCase = bookUseCase;
    }

    @POST
    public Response addBook(BookDTO bookDTO) {
        if (bookDTO.getLibraryId() == null) {
            throw new BadRequestException("Library ID must not be null");
        }
        Library library = libraryUseCase.getLibraryById(bookDTO.getLibraryId())
                .orElseThrow(() -> new NotFoundException("Library not found"));

        Book newBook = bookUseCase.addBook(
                bookDTO.getIsbn(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getGenre(), library);

        URI bookUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(newBook.getIsbn()))
                .build();

        Response.ResponseBuilder rb = Response.created(bookUri);
        addLink(rb, bookUri, "self");

        return rb.entity(restMapper.toBookDTO(newBook)).build();
    }

    @PUT
    @Path("/{isbn}")
    public Response updateBook(@PathParam("isbn") Long isbn, BookDTO updateDTO) {
        bookUseCase.updateBook(isbn, updateDTO.getTitle(), updateDTO.getAuthor(), updateDTO.getGenre());

        Book updatedBook = bookUseCase.getBookByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        BookDTO responseDto = restMapper.toBookDTO(updatedBook);

        EntityTag etag = new EntityTag(ETagGenerator.fromBook(updatedBook));

        Response.ResponseBuilder rb = Response.ok(responseDto);
        addLink(rb, uriInfo.getAbsolutePath(), "self");

        CacheControl cc = new CacheControl();
        cc.setMaxAge(60);
        cc.setPrivate(true);

        return rb.tag(etag).cacheControl(cc).build();
    }

    @DELETE
    @Path("/{isbn}")
    public Response deleteBook(@PathParam("isbn") Long isbn) {
        bookUseCase.deleteBook(isbn);

        URI collectionUri = uriInfo.getBaseUriBuilder().path(BookController.class).build();
        Response.ResponseBuilder rb = Response.noContent();
        addLink(rb, collectionUri, "collection");

        return rb.build();
    }
    
    @GET
    @Path("/{isbn}")
    public Response getBookByIsbn(@PathParam("isbn") Long isbn) {
        Book book = bookUseCase.getBookByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        BookDTO dto = restMapper.toBookDTO(book);

        URI selfUri = uriInfo.getAbsolutePath();
        Response.ResponseBuilder rb = Response.ok(dto);
        addLink(rb, selfUri, "self");
        addLink(rb, selfUri, "update");
        addLink(rb, selfUri, "delete");

        if (book.getLibrary() != null) {
            addRelationLink(rb, uriInfo, LibraryController.class, "getLibraryById",
                    book.getLibrary().getId(), "library");
        }

        CacheControl cc = new CacheControl();
        cc.setMaxAge(60);
        cc.setPrivate(true);
        rb.cacheControl(cc);

        return rb.build();
    }

    @GET
    public Response getAllBooks(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("genre") String genre,
            @QueryParam("author") String author) {

        List<Book> books = bookUseCase.getAllBooks(page, size, author, genre);
        List<BookDTO> dtos = restMapper.toBookDTOs(books);

        Response.ResponseBuilder rb = Response.ok(dtos);
        addLink(rb, uriInfo.getAbsolutePath(), "self");

        CacheControl cc = new CacheControl();
        cc.setMaxAge(60);
        rb.cacheControl(cc);

        return rb.build();
    }

    /* @GET
    @Path("/statistics")
    public Response getBookStatistics() {
        BookStatistics stats = bookUseCase.getBookCounts();

        return Response.ok(stats).build();
    } */

}
