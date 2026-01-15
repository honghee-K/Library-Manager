package thws.librarymanager.adapters.in.rest.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

import thws.librarymanager.adapters.in.rest.models.*;
import thws.librarymanager.application.domain.models.*;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RestMapper {


    //TODO: Jede andere Links in HEADER speichern (siehe das FOTO)
    public BookDTO toBookDTO(Book book) {
        if (book == null) return null;

        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setGenre(book.getGenre());
        dto.setOnLoan(book.isOnLoan());

        if (book.getLibrary() != null) {
            dto.setLibraryId(book.getLibrary().getId());
        }

        return dto;
    }


    public LibraryDTO toLibraryDTO(Library library) {
        if (library == null) return null;

        LibraryDTO dto = new LibraryDTO();
        dto.setId(library.getId());
        dto.setName(library.getName());
        dto.setLocation(library.getLocation());

        return dto;
    }


    public LoanDTO toLoanDTO(Loan loan) {
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

        return dto;
    }

    public UserDTO toUserDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }

    public List<UserDTO> toUserDTOs(List<User> users) {
        return users.stream().map(this::toUserDTO).collect(Collectors.toList());
    }

    public List<BookDTO> toBookDTOs(List<Book> books) {
        return books.stream().map(this::toBookDTO).collect(Collectors.toList());
    }

    public List<LibraryDTO> toLibraryDTOs(List<Library> libraries) {
        return libraries.stream().map(this::toLibraryDTO).collect(Collectors.toList());
    }
}