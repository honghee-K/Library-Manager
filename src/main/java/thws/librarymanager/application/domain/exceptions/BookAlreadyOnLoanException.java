package thws.librarymanager.application.domain.exceptions;

public class BookAlreadyOnLoanException extends RuntimeException {

    public BookAlreadyOnLoanException(Long bookId) {
        super("Book is already on loan with id: " + bookId);
    }
}
