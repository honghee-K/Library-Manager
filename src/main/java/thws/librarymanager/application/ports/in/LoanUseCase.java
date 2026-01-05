package thws.librarymanager.application.ports.in;

import java.time.LocalDate;
import java.util.List;

import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.LoanStatus;

public interface LoanUseCase {

    Loan createLoan(Long userId, Long bookId);

    Loan returnLoan(Long loanId);

    Loan getLoanById(Long loanId);

    List<Loan> searchLoans(Long userId, Long bookId, LoanStatus status, int page, int size);

    Loan extendLoanPeriod(Long loanId, LocalDate newDueDate);
}
