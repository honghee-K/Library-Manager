package thws.librarymanager.adapters.in.rest.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

import thws.librarymanager.adapters.in.rest.BookController;
import thws.librarymanager.adapters.in.rest.LibraryController;
import thws.librarymanager.adapters.in.rest.LoanController;
import thws.librarymanager.adapters.in.rest.models.*;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.User;

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

        String bookHref = uriInfo.getBaseUriBuilder()
                .path(BookController.class)
                .path(BookController.class, "getBookByIsbn")
                .build(book.getIsbn())
                .toString();

        dto.addLink(bookHref, "self", MediaType.APPLICATION_JSON);

        dto.addLink(bookHref, "update", MediaType.APPLICATION_JSON);

        dto.addLink(bookHref, "delete", MediaType.APPLICATION_JSON);

        if (book.getLibrary() != null) {
            String libHref = uriInfo.getBaseUriBuilder()
                    .path(LibraryController.class)
                    .path(LibraryController.class, "getLibraryById")
                    .build(book.getLibrary().getId())
                    .toString();
            dto.addLink(libHref, "library", MediaType.APPLICATION_JSON);
        }

        return dto;
    }


    public LibraryDTO toLibraryDTO(Library library, UriInfo uriInfo) {
        if (library == null) return null;

        LibraryDTO dto = new LibraryDTO();
        dto.setId(library.getId());
        dto.setName(library.getName());
        dto.setLocation(library.getLocation());

        String libraryHref = uriInfo.getBaseUriBuilder()
                .path(LibraryController.class)
                .path(LibraryController.class, "getLibraryById")
                .build(library.getId())
                .toString();

        dto.addLink(libraryHref, "self", MediaType.APPLICATION_JSON);

        //Todo

        return dto;
    }


    public LoanDTO toLoanDTO(Loan loan, UriInfo uriInfo) {
        if (loan == null) return null;

        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());

        if (loan.getUser() != null) {
            dto.setUserId(loan.getUser().getId());
            dto.setUserName(loan.getUser().getName());
        }

        if (loan.getBook() != null) {
            dto.setIsbn(loan.getBook().getIsbn());
            dto.setBookTitle(loan.getBook().getTitle());
        }

        dto.setLoanDate(loan.getLoanDate());
        dto.setDueDate(loan.getDueDate());
        dto.setReturnDate(loan.getReturnDate());
        dto.setStatus(loan.getStatus());

        String loanHref = uriInfo.getBaseUriBuilder()
                .path(LoanController.class)
                .path(LoanController.class, "getLoanById")
                .build(loan.getId())
                .toString();

        dto.addLink(loanHref, "self", MediaType.APPLICATION_JSON);

        //TODO

        return dto;
    }

    public UserDTO toUserDTO(User user, UriInfo uriInfo) {

        //TODO

        return null;
    }
}