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
    List<Loan> getAllLoans(
            Long userId,
            Long isbn,
            LoanStatus status,
            Boolean overdue,
            int page,
            int size
    );

}
