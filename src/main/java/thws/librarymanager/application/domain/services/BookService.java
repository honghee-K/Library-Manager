package thws.librarymanager.application.domain.services;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.out.repository.BookPort;

@ApplicationScoped
public class BookService implements BookUseCase {

    private final BookPort persistBookPort;

    @Inject
    public BookService(BookPort persistence) {
        this.persistBookPort = persistence;
    }

    @Override
    public Book addBook(Long isbn, String title, String author, String genre, Library library) {

        if (persistBookPort.getBookByIsbn(isbn).isPresent()) { // .isPresent() -> Methode of Optional
            throw new IllegalArgumentException("Book with this ISBN already exists.");
        }

        Book book = new Book(null, isbn, title, author, genre, library, null);

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

    /*

        @Override
        public void startLoanForBook(Long bookIsbn, Loan loan) {
            Book book = persistBookPort.getBookByIsbn(bookIsbn)
                    .orElseThrow(() -> new IllegalArgumentException("Book not found for ISBN: " + bookIsbn));

            book.startLoan(loan);

            persistBookPort.save(book);
        }

        @Override
        public void updateBook(Long isbn, String title, String author, String genre) {
            Book existing = persistBookPort.getBookByIsbn(isbn).orElse(null); // Optional 대신 null을 명시적으로 사용

            if (existing == null) {
                throw new IllegalArgumentException("Book not found for ISBN: " + isbn);
            }

            if (existing.isOnLoan()) {
                throw new IllegalStateException("Cannot update book that is currently on loan.");
            }

            existing.updateBook(title, author, genre);

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
            List<Book> allBooks = persistBookPort.findAllForStatistics();

            long totalBooks = allBooks.size();

            Map<String, Long> countByGenre = allBooks.stream()
                    .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));

            Map<String, Long> countByAuthor = allBooks.stream()
                    .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));

            return new BookStatistics(totalBooks, countByGenre, countByAuthor);
        }
    */

}
