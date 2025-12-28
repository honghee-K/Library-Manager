package thws.librarymanager.application.domain.exceptions;

// Thrown when a book is already on active loan
public class BookAlreadyOnLoanException extends RuntimeException {

    public BookAlreadyOnLoanException(Long bookId) {
        super("Book is already on loan with id: " + bookId);
    }
}
