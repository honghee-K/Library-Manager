package thws.librarymanager.application.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import thws.librarymanager.application.domain.model.Book;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.out.repository.BookPort;



public class BookService implements BookUseCase {


    private final BookPort persistBook;

    public BookService(BookPort persistence) {
        this.persistBook = persistence;
    }

    @Override
    public Book create(Book book) {
        persistBook.save(book);
        return book;
    }

    @Override
    public Book getByIsbn(long isbn) {
        return persistBook.findByIsbn(isbn);
    }

    @Override
    public void update(Book book) {
        Book existing = persistBook.findByIsbn(book.getIsbn());

        if (existing == null)
            throw new IllegalArgumentException("Book not found.");

        if (existing.isOnLoan())
            throw new IllegalStateException("Cannot update book that is currently on loan.");

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setGenre(book.getGenre());
        existing.setLibrary(book.getLibrary());

        persistBook.save(existing);
    }


    @Override
    public void delete(long isbn) {
        Book existing = persistBook.findByIsbn(isbn);

        if (existing == null)
            throw new IllegalArgumentException("Book not found.");

        if (existing.isOnLoan())
            throw new IllegalStateException("Cannot delete book that is on loan.");

        persistBook.deleteByIsbn(isbn);
    }

}
