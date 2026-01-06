package thws.librarymanager.adapters.out.jpa.converter;

import java.util.ArrayList;

import jakarta.enterprise.context.ApplicationScoped;

import thws.librarymanager.adapters.out.jpa.entities.BookEntity;
import thws.librarymanager.adapters.out.jpa.entities.LibraryEntity;
import thws.librarymanager.adapters.out.jpa.entities.UserEntity;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.domain.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaConverter {

    public JpaConverter() {}

    /**
     * TODO : Library
     */
    // Entity -> Domain
    public Library toLibrary(LibraryEntity entity) {
        if (entity == null) {
            return null;
        }

        /*List<Book> books = entity.getBooks() != null
                ? entity.getBooks().stream()
                .map(this::toBook)
                .collect(Collectors.toList())
                : new ArrayList<>();*/

        return new Library(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                new ArrayList<>() //books
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


        /* if (library.getBooks() != null) {
            List<BookEntity> bookEntities = library.getBooks().stream()
                    .map(this::toJpaBook)
                    .collect(Collectors.toList());

            bookEntities.forEach(entity::addBook);
        }*/

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
                null, // Todo
                null // Todo
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

    /**
     * TODO : Loan
     */
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
