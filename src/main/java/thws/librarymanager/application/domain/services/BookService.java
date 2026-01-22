package thws.librarymanager.application.domain.services;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.out.repository.BookPort;
import thws.librarymanager.application.ports.out.repository.LoanPort;

@ApplicationScoped
public class BookService implements BookUseCase {

    private final BookPort persistBookPort;
    private final LoanPort loanPort;

    @Inject
    public BookService(BookPort persistence, LoanPort loanPort) {
        this.persistBookPort = persistence;
        this.loanPort = loanPort;
    }

    @Override
    public Book addBook(Long isbn, String title, String author, String genre, Library library) {

        if (persistBookPort.getBookByIsbn(isbn).isPresent()) {
            throw new IllegalArgumentException("Book with this ISBN already exists.");
        }

        Book book = new Book(null, isbn, title, author, genre, library, null);

        library.addBook(book);

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
    @Transactional
    public void addBookToLibrary(Long isbn, Library library) {
        Book book =
                persistBookPort.getBookByIsbn(isbn).orElseThrow(() -> new IllegalArgumentException("Book not found"));

        book.setLibrary(library);

        if (library != null) {
            library.addBook(book);
        }

        persistBookPort.save(book);
    }

    @Override
    @Transactional
    public void startLoanForBook(Long bookIsbn, Loan loan) {
        Book book = persistBookPort
                .getBookByIsbn(bookIsbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found for ISBN: " + bookIsbn));

        book.startLoan(loan);

        persistBookPort.save(book);
    }

    @Override
    @Transactional
    public void endLoanForBook(Long bookIsbn, Loan loan) {
        Book book = persistBookPort
                .getBookByIsbn(bookIsbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found for ISBN: " + bookIsbn));

        book.endLoan(loan);

        persistBookPort.save(book);
    }

    @Override
    @Transactional
    public void updateBook(Long isbn, String title, String author, String genre) {
        Book existing = persistBookPort.getBookByIsbn(isbn).orElse(null);

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
    @Transactional
    public void deleteBook(long isbn) {
        Book existing = persistBookPort.getBookByIsbn(isbn).orElse(null);

        if (existing == null) throw new IllegalArgumentException("Book not found for ISBN: " + isbn);

        if (existing.isOnLoan()) throw new IllegalStateException("Cannot delete book that is on loan.");

        persistBookPort.deleteByIsbn(isbn);
    }
}
