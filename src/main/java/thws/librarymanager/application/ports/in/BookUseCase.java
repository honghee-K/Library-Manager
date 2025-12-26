package thws.librarymanager.application.ports.in;

import thws.librarymanager.application.domain.model.Book;
import thws.librarymanager.application.domain.model.Library;
import thws.librarymanager.application.domain.model.Loan;

import java.util.List;
import java.util.Optional;


public interface BookUseCase {
    Book addBook(Long isbn, String title, String author, String genre, Library library);

    void updateBook(Long isbn, String title, String author, String genre);

    void deleteBook(long isbn);

    Optional<Book> getBookByIsbn(Long isbn);

    List<Book> getAllBooks(int page, int size, String author, String genre);

    void startLoanForBook(Long isbn, Loan loan);

    BookStatistics getBookCounts();
}
