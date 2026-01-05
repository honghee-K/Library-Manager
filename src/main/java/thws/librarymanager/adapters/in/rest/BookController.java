package thws.librarymanager.adapters.in.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import thws.librarymanager.adapters.in.rest.mapper.RestMapper;
import thws.librarymanager.adapters.in.rest.models.BookDTO;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.ports.in.BookUseCase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {

    @Inject
    private BookUseCase bookUseCase;
    @Inject
    private RestMapper restMapper;

    @Context
    UriInfo uriInfo;

    @Inject
    public BookController(BookUseCase bookUseCase) {
        this.bookUseCase = bookUseCase;
    }

       /*
    @POST
    public Response addBook(Book book) {
        Book newBook = bookUseCase.addBook(book);
        URI bookUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newBook.getIsbn())).build();
        return Response.created(bookUri).entity(toBookResponse(newBook)).build();
    }

    @PUT
    @Path("/{isbn}")
    public Response updateBook(@PathParam("isbn") Long isbn, Book updateBook) {
         Book updated = bookUseCase.updateBook(isbn, updateBook.getTitle(), updateBook.getAuthor(), updateBook.getGenre());
         return Response.ok(toBookResponse(updated)).build();
    }

    @DELETE
    @Path("/{isbn}")
    public Response deleteBook(@PathParam("isbn") Long isbn) {
        bookUseCase.deleteBook(isbn);
        return Response.noContent().build();
    }
    */

    @GET
    @Path("/{isbn}")
    public Response getBookByIsbn(@PathParam("isbn") Long isbn) {
        Optional<Book> book = bookUseCase.getBookByIsbn(isbn);

        return book.map(b -> restMapper.toDTO(b, uriInfo))
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

        List<BookDTO> dtos = books.stream()
                .map(book -> restMapper.toDTO(book, uriInfo))
                .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }



   /* @GET
    @Path("/statistics")
    public Response getBookStatistics() {
        BookStatistics stats = bookUseCase.getBookCounts();

        return Response.ok(stats).build();
    } */



}