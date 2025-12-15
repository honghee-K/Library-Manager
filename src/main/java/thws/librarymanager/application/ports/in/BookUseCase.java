package thws.librarymanager.application.ports.in;

import thws.librarymanager.application.domain.model.Book;

import java.util.List;
import java.util.Optional;


public interface BookUseCase {
    Book addBook(Book book);
    void updateBook(Long isbn, String title, String author, String genre);
    void deleteBook(long isbn);
    Optional<Book> getBookByIsbn(Long isbn);
    List<Book> getAllBooks(int page, int size, String author, String genre);
    void startLoanForBook(Long bookIsbn, Long loanId);

    BookStatistics getBookCounts();
}
