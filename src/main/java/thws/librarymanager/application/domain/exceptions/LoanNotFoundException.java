package thws.librarymanager.application.domain.exceptions;

public class LoanNotFoundException extends Exception {
    public LoanNotFoundException(String message) {
        super(message);
    }
}
