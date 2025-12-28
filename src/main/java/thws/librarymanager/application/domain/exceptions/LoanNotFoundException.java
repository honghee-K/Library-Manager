package thws.librarymanager.application.domain.exceptions;

// Thrown when a loan with the given ID cannot be found
public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(Long loanId) {
        super("Loan not found with id: " + loanId);
    }
}
