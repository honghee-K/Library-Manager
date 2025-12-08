package thws.librarymanager.application.ports.out.repository;

import thws.librarymanager.application.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookPort {
    Book save(Book book); //C oder U

    Optional<Book> getBookByIsbn(Long isbn); //R

    void deleteByIsbn(Long isbn); //D

    List<Book> findAll(int page, int size, String genre, String author); //R(Filtering oder Paging)
    List<Book> findAllForStatistics();
}
