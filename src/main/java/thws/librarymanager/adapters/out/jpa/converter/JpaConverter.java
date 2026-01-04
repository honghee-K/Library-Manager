package thws.librarymanager.adapters.out.jpa.converter;

import jakarta.enterprise.context.ApplicationScoped;
import thws.librarymanager.adapters.out.jpa.entities.BookEntity;
import thws.librarymanager.adapters.out.jpa.entities.LibraryEntity;
import thws.librarymanager.adapters.out.jpa.entities.LoanEntity;
import thws.librarymanager.adapters.out.jpa.entities.UserEntity;
import thws.librarymanager.adapters.out.jpa.enums.LoanStatusJpa;
import thws.librarymanager.application.domain.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaConverter {

    public JpaConverter() {}


    // Entity -> Domain
    public Library toLibrary(LibraryEntity entity) {
        if (entity == null) {
            return null;
        }

        List<Book> books = entity.getBooks() != null
                ? entity.getBooks().stream()
                .map(this::toBook)
                .collect(Collectors.toList())
                : new ArrayList<>();

        return new Library(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                books
        );
    }

    // Domain -> Entity
    public LibraryEntity toJpaLibrary(Library library) {
        if (library == null) {
            return null;
        }

        LibraryEntity entity = new LibraryEntity();
        entity.setId(library.getId());
        entity.setName(library.getName());
        entity.setLocation(library.getLocation());

        if (library.getBooks() != null) {
            List<BookEntity> bookEntities = library.getBooks().stream()
                    .map(this::toJpaBook)
                    .collect(Collectors.toList());

            bookEntities.forEach(entity::addBook);
        }

        return entity;
    }

    // Entity -> Domain (bringen aus DB)
    public Book toBook(BookEntity entity) {
        return new Book(
                entity.getId(),
                entity.getIsbn(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getGenre(),
                null, //Todo
                null //Todo
        );
    }

    // Domain -> Entity (speichern ins DB)
    public BookEntity toJpaBook(Book book) {
        BookEntity entity = new BookEntity();
        entity.setId(book.getId());
        entity.setIsbn(book.getIsbn());
        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setGenre(book.getGenre());
        /*if (book.getLibrary() != null) {
            entity.setLibrary(toJpaLibrary(book.getLibrary()));
        }
        if (book.getCurrentLoan() != null) {
            entity.setCurrentLoan(toJpaLoan(book.getCurrentLoan()));
        }*/
        return entity;
    }


    // Loan: Domain -> Entity
    public LoanEntity toJpaLoan(Loan loan) {
        if (loan == null) {
            return null;
        }

        LoanEntity entity = new LoanEntity();
        entity.setId(loan.getId());

        entity.setUserId(loan.getUserId());
        entity.setBookId(loan.getBookId());

        entity.setLoanDate(loan.getLoanDate());
        entity.setDueDate(loan.getDueDate());
        entity.setReturnDate(loan.getReturnDate());

        entity.setStatus(LoanStatusJpa.valueOf(loan.getStatus().name()));

        return entity;
    }


    // Loan: Entity -> Domain
    public Loan toLoan(LoanEntity entity) {
        if (entity == null) {
            return null;
        }

        return Loan.restore(
                entity.getId(),
                entity.getUserId(),
                entity.getBookId(),
                entity.getLoanDate(),
                entity.getDueDate(),
                entity.getReturnDate(),
                LoanStatus.valueOf(entity.getStatus().name())
        );
    }


    public User toUser(UserEntity userEntity) {

        return new User(userEntity.getId(), userEntity.getName(), userEntity.getEmail());
    }

    public UserEntity toJpaUser(User user) {

        UserEntity result = new UserEntity();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());

        return result;
    }



}
