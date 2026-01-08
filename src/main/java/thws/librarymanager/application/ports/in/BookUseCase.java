package thws.librarymanager.application.ports.in;

import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.domain.models.Loan;

public interface BookUseCase {
    Book addBook(Long isbn, String title, String author, String genre, Library library);

    void updateBook(Long isbn, String title, String author, String genre);

    void deleteBook(long isbn);

    Optional<Book> getBookByIsbn(Long isbn);

    List<Book> getAllBooks(int page, int size, String author, String genre);

    void startLoanForBook(Long isbn, Loan loan);
    void endLoanForBook(Long isbn, Loan loan);

    BookStatistics getBookCounts();
}
