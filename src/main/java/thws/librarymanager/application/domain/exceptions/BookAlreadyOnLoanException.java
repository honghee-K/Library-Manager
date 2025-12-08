package thws.librarymanager.application.domain.exceptions;

public class BookAlreadyOnLoanException extends Exception {
    public BookAlreadyOnLoanException(String message) {
        super(message);
    }
}