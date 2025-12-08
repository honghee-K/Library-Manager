package thws.librarymanager.application.ports.in;

import thws.librarymanager.application.domain.exceptions.*;
import thws.librarymanager.application.domain.model.Loan;

import java.util.List;

public interface LoanUseCase {

    Loan createLoan(Long userId, String bookIsbn)
            throws UserNotFoundException,
            BookNotFoundException,
            BookAlreadyOnLoanException;

    void returnBook(Long loanId)
            throws LoanNotFoundException;

    Loan getLoanById(Long loanId)
            throws LoanNotFoundException;

    List<Loan> getActiveLoans();

    List<Loan> getOverdueLoans();


}
