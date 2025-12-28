package thws.librarymanager.adapters.out.jpa.converter;

import jakarta.enterprise.context.ApplicationScoped;
import thws.librarymanager.adapters.out.jpa.entities.BookEntity;
import thws.librarymanager.adapters.out.jpa.entities.UserEntity;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.User;

@ApplicationScoped
public class JpaConverter {

    public JpaConverter() {}


    /**
     TODO : Library
     */

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

    /**
     TODO : Loan
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
