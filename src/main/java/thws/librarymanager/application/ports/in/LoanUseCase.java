package thws.librarymanager.application.ports.in;

import java.time.LocalDate;
import java.util.List;

import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.LoanStatus;
import thws.librarymanager.application.domain.models.User;

public interface LoanUseCase {

    Loan createLoan(User user, Book book);

    Loan returnLoan(Long loanId); //TODO

    Loan getLoanById(Long loanId);

/*    List<Loan> searchLoans(Long userId, Long bookId, LoanStatus status, int page, int size);

    Loan extendLoanPeriod(Long loanId, LocalDate newDueDate);*/ //WEG!!!
}
