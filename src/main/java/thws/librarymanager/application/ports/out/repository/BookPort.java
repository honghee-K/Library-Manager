package thws.librarymanager.application.ports.out.repository;

import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.Book;

public interface BookPort {
    /*    Book save(Book book);*/

    Optional<Book> getBookByIsbn(Long isbn);

    /*    void deleteByIsbn(Long isbn);*/

    List<Book> findAll(int page, int size, String author, String genre);

    /*    List<Book> findAllForStatistics();*/
}
