package thws.librarymanager.adapters.in.rest.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

import thws.librarymanager.adapters.in.rest.BookController;
import thws.librarymanager.adapters.in.rest.LibraryController;
import thws.librarymanager.adapters.in.rest.models.BookDTO;
import thws.librarymanager.adapters.in.rest.models.LibraryDTO;
import thws.librarymanager.adapters.in.rest.models.Link;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;

@ApplicationScoped
public class RestMapper {

    public BookDTO toBookDTO(Book book, UriInfo uriInfo) {
        if (book == null) return null;

        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setGenre(book.getGenre());
        dto.setOnLoan(book.isOnLoan());

        String selfHref = uriInfo.getBaseUriBuilder()
                .path(BookController.class)
                .path(BookController.class, "getBookByIsbn")
                .build(book.getIsbn())
                .toString();

        dto.setSelfLink(new Link(selfHref, "self", MediaType.APPLICATION_JSON));

        return dto;
    }



    public LibraryDTO toLibraryDTO(Library library, UriInfo uriInfo) {
        if (library == null) return null;

        LibraryDTO dto = new LibraryDTO();
        dto.setId(library.getId());
        dto.setName(library.getName());
        dto.setLocation(library.getLocation());

        String selfHref = uriInfo.getBaseUriBuilder()
                .path(LibraryController.class)
                .path(LibraryController.class, "getLibraryById")
                .build(library.getId())
                .toString();

        dto.setSelfLink(new Link(selfHref, "self", MediaType.APPLICATION_JSON));

        return dto;
    }
}
