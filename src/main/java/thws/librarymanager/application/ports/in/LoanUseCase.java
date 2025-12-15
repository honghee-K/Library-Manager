package thws.librarymanager.application.ports.in;

import thws.librarymanager.application.domain.model.Loan;
import thws.librarymanager.application.domain.model.LoanStatus;

import java.time.LocalDate;
import java.util.List;

public interface LoanUseCase {

    Loan createLoan(Long userId, Long bookId);
    Loan returnLoan(Long loanId);
    Loan getLoanById(Long loanId);
    List<Loan> searchLoans(Long userId,
                           Long bookId,
                           LoanStatus status,
                           int page,
                           int size);

    Loan extendLoanPeriod(Long loanId, LocalDate newDueDate);

}
