package thws.librarymanager.adapters.in.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import thws.librarymanager.adapters.in.rest.mapper.RestMapper;
import thws.librarymanager.adapters.in.rest.models.BookDTO;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.out.repository.LibraryPort;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {

    @Context
    UriInfo uriInfo;

    @Inject
    private BookUseCase bookUseCase;

    @Inject
    private RestMapper restMapper;

    @Inject
    LibraryPort libraryPort;

    @Inject
    public BookController(BookUseCase bookUseCase) {
        this.bookUseCase = bookUseCase;
    }

    @POST
    public Response addBook(BookDTO bookDTO) {
        Library library = libraryPort
                .getLibraryById(bookDTO.getLibraryId())
                .orElseThrow(() -> new NotFoundException("Library not found"));

        Book newBook = bookUseCase.addBook(
                bookDTO.getIsbn(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getGenre(), library);

        URI bookUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(newBook.getIsbn()))
                .build();

        return Response.created(bookUri)
                .entity(restMapper.toBookDTO(newBook, uriInfo))
                .build();
    }

    @PUT
    @Path("/{isbn}")
    public Response updateBook(@PathParam("isbn") Long isbn, BookDTO updateDTO) {
        bookUseCase.updateBook(isbn, updateDTO.getTitle(), updateDTO.getAuthor(), updateDTO.getGenre());
        Book updatedBook = bookUseCase.getBookByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Book not found after update"));
        return Response.ok(restMapper.toBookDTO(updatedBook, uriInfo)).build();
    }

    @DELETE
    @Path("/{isbn}")
    public Response deleteBook(@PathParam("isbn") Long isbn) {
        bookUseCase.deleteBook(isbn);
        return Response.noContent().build();
    }
    
    @GET
    @Path("/{isbn}")
    public Response getBookByIsbn(@PathParam("isbn") Long isbn) {
        Optional<Book> book = bookUseCase.getBookByIsbn(isbn);

        return book.map(b -> restMapper.toBookDTO(b, uriInfo))
                .map(dto -> Response.ok(dto).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response getAllBooks(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("genre") String genre,
            @QueryParam("author") String author) {

        List<Book> books = bookUseCase.getAllBooks(page, size, author, genre);

        List<BookDTO> dtos =
                books.stream().map(book -> restMapper.toBookDTO(book, uriInfo)).collect(Collectors.toList());

        return Response.ok(dtos).build();
    }

    /* @GET
    @Path("/statistics")
    public Response getBookStatistics() {
        BookStatistics stats = bookUseCase.getBookCounts();

        return Response.ok(stats).build();
    } */

}
