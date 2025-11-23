package thws.librarymanager.application.ports.out.repository;

import thws.librarymanager.application.domain.model.Book;

public interface BookPort {
    void save(Book book);

    Book findByIsbn(Long isbn);

    void deleteByIsbn(Long isbn);
}
