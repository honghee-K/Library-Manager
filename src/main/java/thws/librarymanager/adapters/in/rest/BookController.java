import java.net.URI;
import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//// DTOs (Data Transfer Objects) are needed, but we assume the domain model is used for simplicity.
//
//@Path("/books")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class BookController {
//
//    private final BookUseCase bookUseCase;
//    @Context
//    UriInfo uriInfo; // Used for HATEOAS link creation
//
//    @Inject
//    public BookController(BookUseCase bookUseCase) {
//        this.bookUseCase = bookUseCase;
//    }
//
//    // Create (C)
//    @POST
//    public Response addBook(Book book) {
//        Book newBook = bookUseCase.addBook(book);
//        URI bookUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newBook.getIsbn())).build();
//
//        // HATEOAS: Returns the URI of the created resource.
//        return Response.created(bookUri).entity(toBookResponse(newBook)).build();
//    }
//
//    // Read (R) with Filtering and Paging
//    @GET
//    public Response getAllBooks(
//            @QueryParam("page") @DefaultValue("0") int page,
//            @QueryParam("size") @DefaultValue("10") int size,
//            @QueryParam("genre") String genre,
//            @QueryParam("author") String author) {
//
//        List<Book> books = bookUseCase.getAllBooks(page, size, genre, author);
//
//        // Convert to DTO and add HATEOAS links.
//        List<BookResponse> bookResponses = books.stream()
//                .map(this::toBookResponse)
//                .collect(Collectors.toList());
//
//        // Assuming a simple Paging Response Object
//        // Actual implementation should include total items and next/prev page links.
//        return Response.ok(bookResponses).build();
//    }
//
//    // Read (R) by ID
//    @GET
//    @Path("/{isbn}")
//    public Response getBookByIsbn(@PathParam("isbn") Long isbn) {
//        Optional<Book> book = bookUseCase.getBookByIsbn(isbn);
//
//        return book.map(b -> Response.ok(toBookResponse(b)).build())
//                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
//    }
//
//    // Update (U)
//    @PUT
//    @Path("/{isbn}")
//    public Response updateBook(@PathParam("isbn") Long isbn, Book updateBook) {
//        Book updated = bookUseCase.updateBook(isbn, updateBook.getTitle(), updateBook.getAuthor(), updateBook.getGenre());
//
//        return Response.ok(toBookResponse(updated)).build();
//    }
//
//    // Delete (D)
//    @DELETE
//    @Path("/{isbn}")
//    public Response deleteBook(@PathParam("isbn") Long isbn) {
//        bookUseCase.deleteBook(isbn);
//        return Response.noContent().build();
//    }
//
//    // Internal Helper Class: Adds HATEOAS links to the response.
//    private BookResponse toBookResponse(Book book) {
//        BookResponse response = new BookResponse(book);
//        Long isbn = book.getIsbn();
//
//        // Self Link
//        response.addLink("self", uriInfo.getBaseUriBuilder().path(BookController.class).path(BookController.class, "getBookByIsbn").build(isbn).toString());
//        // Update Link
//        response.addLink("update", uriInfo.getBaseUriBuilder().path(BookController.class).path(BookController.class, "updateBook").build(isbn).toString(), "PUT");
//        // Delete Link
//        response.addLink("delete", uriInfo.getBaseUriBuilder().path(BookController.class).path(BookController.class, "deleteBook").build(isbn).toString(), "DELETE");
//        // Loan Status Link
//        if (book.isOnLoan()) {
//            response.addLink("current_loan", uriInfo.getBaseUriBuilder().path("/loans").path(String.valueOf(book.getLoanId())).build().toString());
//        } else {
//            response.addLink("start_loan", uriInfo.getBaseUriBuilder().path("/loans").build().toString(), "POST"); // connect to LoanController
//        }
//
//        return response;
//    }
//
//    // DTO (Data Transfer Object) - Response Object
//    public static class BookResponse {
//        public Long isbn;
//        public String title;
//        public String author;
//        public String genre;
//        public Long libraryId;
//        public boolean isOnLoan;
//        public final java.util.Map<String, Link> _links = new java.util.HashMap<>();
//
//        public BookResponse(Book book) {
//            this.isbn = book.getIsbn();
//            this.title = book.getTitle();
//            this.author = book.getAuthor();
//            this.genre = book.getGenre();
//            this.libraryId = book.getLibraryId();
//            this.isOnLoan = book.isOnLoan();
//        }
//
//        public void addLink(String rel, String href, String method) {
//            _links.put(rel, new Link(href, method));
//        }
//        public void addLink(String rel, String href) {
//            addLink(rel, href, "GET");
//        }
//
//        public static class Link {
//            public String href;
//            public String method;
//            public Link(String href, String method) {
//                this.href = href;
//                this.method = method;
//            }
//        }
//    }
//}