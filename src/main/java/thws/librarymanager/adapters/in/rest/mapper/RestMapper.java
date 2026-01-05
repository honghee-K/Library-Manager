package thws.librarymanager.adapters.in.rest.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

import thws.librarymanager.adapters.in.rest.BookController;
import thws.librarymanager.adapters.in.rest.models.BookDTO;
import thws.librarymanager.adapters.in.rest.models.Link;
import thws.librarymanager.application.domain.models.Book;

@ApplicationScoped
public class RestMapper {

    public BookDTO toDTO(Book book, UriInfo uriInfo) {
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
}
