package thws.librarymanager.application.domain.services;

import java.time.LocalDate;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import thws.librarymanager.application.domain.exceptions.BookAlreadyOnLoanException;
import thws.librarymanager.application.domain.exceptions.BookNotFoundException;
import thws.librarymanager.application.domain.exceptions.LoanNotFoundException;
import thws.librarymanager.application.domain.exceptions.UserNotFoundException;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.LoanStatus;
import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.in.LoanUseCase;
import thws.librarymanager.application.ports.out.repository.BookPort;
import thws.librarymanager.application.ports.out.repository.LoanPort;
import thws.librarymanager.application.ports.out.repository.UserPort;
import thws.librarymanager.application.ports.out.time.TimeProvider;

@ApplicationScoped
public class LoanService implements LoanUseCase {

    private final LoanPort loanPort;
    private final UserPort userPort;
    private final BookPort bookPort;
    private final TimeProvider timeProvider;




    private final BookUseCase bookUseCase;
    @Inject
    public LoanService(LoanPort loanPort, UserPort userPort, BookPort bookPort, BookUseCase bookUseCase,TimeProvider timeProvider) {

        this.loanPort = loanPort;
        this.userPort = userPort;
        this.bookPort = bookPort;
        this.bookUseCase = bookUseCase;
        this.timeProvider = timeProvider;

    }

    @Override
    public Loan createLoan(User user, Book book) {

        if (loanPort.existsActiveLoanForBook(book.getIsbn())) {
            throw new BookAlreadyOnLoanException(book.getIsbn());
        }

        LocalDate today = timeProvider.today();
        LocalDate due = today.plusDays(14);
        Loan loan = Loan.createLoan(user, book, today, due);

        loanPort.save(loan);

        bookUseCase.startLoanForBook(book.getIsbn(), loan);
        return loan;
    }

   @Override
    public Loan returnLoan(Long loanId) {

        Loan loan = loanPort.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));

        loan.setReturned(timeProvider.today());


       //bookUseCase.endLoanForBook(book.getIsbn());



       return loanPort.save(loan);
    }

    @Override
    public Loan getLoanById(Long loanId) {
        return loanPort.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));
    }

 /*   @Override
    public List<Loan> searchLoans(Long userId, Long bookId, LoanStatus status, int page, int size) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("page must be >= 0 and size > 0");
        }

        return loanPort.findLoans(userId, bookId, status, page, size);
    }

    @Override
    public Loan extendLoanPeriod(Long loanId, LocalDate newDueDate) {

        Loan loan = loanPort.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));

        if (loan.isReturned()) {
            throw new IllegalStateException("Returned loan cannot be extended");
        }

        loan.changeDueDate(newDueDate);
        return loanPort.save(loan);
    }*/
}
