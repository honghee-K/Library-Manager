package thws.librarymanager.adapters.out.jpa.converter;

import java.util.ArrayList;

import jakarta.enterprise.context.ApplicationScoped;

import thws.librarymanager.adapters.out.jpa.entities.*;
import thws.librarymanager.adapters.out.jpa.enums.LoanStatusJpa;
import thws.librarymanager.application.domain.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaConverter {

    public JpaConverter() {}


    public Library toLibraryMinimal(LibraryEntity entity) {
        if (entity == null) return null;
        return new Library(entity.getId(), entity.getName(), entity.getLocation(), new ArrayList<>());
    }

    public Library toLibrary(LibraryEntity entity) {
        if (entity == null) {
            return null;
        }

        List<Book> books = entity.getBooks() != null
                ? entity.getBooks().stream()
                .map(this::toBookMinimal)
                .collect(Collectors.toList())
                : new ArrayList<>();

        return new Library(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                books
        );

    }

    public LibraryEntity toJpaLibraryMinimal(Library library) {
        if (library == null) return null;
        LibraryEntity entity = new LibraryEntity();
        entity.setId(library.getId());
        entity.setName(library.getName());
        entity.setLocation(library.getLocation());
        return entity;
    }

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
                    .map(this::toJpaBookMinimal)
                    .collect(Collectors.toList());

            bookEntities.forEach(entity::addBook);
        }

        return entity;
    }

    public Book toBookMinimal(BookEntity entity) {
        if (entity == null) return null;
        return new Book(
                entity.getId(),
                entity.getIsbn(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getGenre(),
                null,
                null
        );
    }

    public Book toBook(BookEntity entity) {
        if (entity == null) return null;

        return new Book(
                entity.getId(),
                entity.getIsbn(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getGenre(),
                toLibraryMinimal(entity.getLibrary()),
                toLoan(entity.getCurrentLoan())
                );
    }

    public BookEntity toJpaBookMinimal(Book book) {
        if (book == null) return null;
        BookEntity entity = new BookEntity();
        entity.setId(book.getId());
        entity.setIsbn(book.getIsbn());
        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setGenre(book.getGenre());
        entity.setLibrary(toJpaLibraryMinimal(book.getLibrary()));
        return entity;
    }

    public BookEntity toJpaBook(Book book) {
        if (book == null) return null;

        BookEntity entity = new BookEntity();
        entity.setId(book.getId());
        entity.setIsbn(book.getIsbn());
        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setGenre(book.getGenre());

        if (book.getLibrary() != null) {
            entity.setLibrary(toJpaLibraryMinimal(book.getLibrary()));
        }
        if (book.getCurrentLoan() != null) {
            entity.setCurrentLoan(toJpaLoan(book.getCurrentLoan()));
        }
        return entity;
    }

    private LoanStatusJpa toJpaLoanStatus(LoanStatus status) {
        if (status == null) return null;
        return LoanStatusJpa.valueOf(status.name());
    }
    private LoanStatus toLoanStatus(LoanStatusJpa jpaStatus) {
        if (jpaStatus == null) return null;
        return LoanStatus.valueOf(jpaStatus.name());
    }

    public Loan toLoan(LoanEntity entity) {
        if (entity == null) return null;

        Book bookInLoan = new Book(
                entity.getBook().getId(),
                entity.getBook().getIsbn(),
                entity.getBook().getTitle(),
                entity.getBook().getAuthor(),
                entity.getBook().getGenre(),
                toLibraryMinimal(entity.getBook().getLibrary()),
                null
        );

        return Loan.restore(
                entity.getId(),
                toUser(entity.getUser()),
                bookInLoan,
                entity.getLoanDate(),
                entity.getDueDate(),
                entity.getReturnDate(),
                toLoanStatus(entity.getStatus())
        );
    }


        public LoanEntity toJpaLoan(Loan loan) {
        if (loan == null) return null;

        LoanEntity entity = new LoanEntity();
        entity.setId(loan.getId());
        entity.setLoanDate(loan.getLoanDate());
        entity.setDueDate(loan.getDueDate());
        entity.setReturnDate(loan.getReturnDate());
        entity.setStatus(toJpaLoanStatus(loan.getStatus()));

        if (loan.getUser() != null) {
            entity.setUser(toJpaUser(loan.getUser()));
        }

        if (loan.getBook() != null) {
            entity.setBook(toJpaBookMinimal(loan.getBook()));
        }

        return entity;
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

    public Librarian toLibrarian(LibrarianEntity entity) {
        if (entity == null) return null;
        return new Librarian(entity.getId(), entity.getName());
    }

    public LibrarianEntity toJpaLibrarian(Librarian librarian) {
        if (librarian == null) return null;
        LibrarianEntity entity = new LibrarianEntity();
        entity.setId(librarian.getId());
        entity.setName(librarian.getName());
        return entity;
    }
}
