package thws.librarymanager.application.domain.service;

import thws.librarymanager.application.domain.model.Book;
import thws.librarymanager.application.ports.in.BookStatistics;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.out.repository.BookPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class BookService implements BookUseCase {


    private final BookPort persistBookPort;

    public BookService(BookPort persistence) {
        this.persistBookPort = persistence;
    }

    @Override
    public Book addBook(Book book) {
        if (persistBookPort.getBookByIsbn(book.getIsbn()).isPresent()) { //.isPresent() -> Methode of Optional
            throw new IllegalArgumentException("Book with this ISBN already exists.");
        }

        return persistBookPort.save(book);
    }


    @Override
    public Optional<Book> getBookByIsbn(Long isbn) {
        return persistBookPort.getBookByIsbn(isbn);
    }

    @Override
    public List<Book> getAllBooks(int page, int size, String author, String genre) {
        return persistBookPort.findAll(page, size, author, genre);
    }

    @Override
    public void updateBook(Long isbn, String title, String author, String genre) {
        // 1. Existing Book Lookup
        Book existing = persistBookPort.getBookByIsbn(isbn).orElse(null); // Optional 대신 null을 명시적으로 사용

        if (existing == null) {
            throw new IllegalArgumentException("Book not found for ISBN: " + isbn);
        }

        // 2. Business Rule Validation
        if (existing.isOnLoan()) {
            throw new IllegalStateException("Cannot update book that is currently on loan.");
        }

        // 3. Use the Domain Model's Behavior Method
        existing.updateBook(title, author, genre);


        // 4. Save
        persistBookPort.save(existing);
    }


    @Override
    public void deleteBook(long isbn) {
        Book existing = persistBookPort.getBookByIsbn(isbn).orElse(null);

        if (existing == null)
            throw new IllegalArgumentException("Book not found for ISBN: " + isbn);

        if (existing.isOnLoan())
            throw new IllegalStateException("Cannot delete book that is on loan.");

        persistBookPort.deleteByIsbn(isbn);
    }


    @Override
    public BookStatistics getBookCounts() {
        // 1. Retrieve all book data via BookPort.
        List<Book> allBooks = persistBookPort.findAllForStatistics();

        long totalBooks = allBooks.size();

        // 2. Calculate counts by genre/author using the Stream API.
        Map<String, Long> countByGenre = allBooks.stream()
                .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));

        Map<String, Long> countByAuthor = allBooks.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));

        // 3. Call the constructor of the BookStatistics class and return the object.
        return new BookStatistics(totalBooks, countByGenre, countByAuthor);
    }

}
