package thws.librarymanager.application.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import thws.librarymanager.application.domain.exceptions.BookAlreadyOnLoanException;
import thws.librarymanager.application.domain.exceptions.BookNotFoundException;
import thws.librarymanager.application.domain.exceptions.LoanNotFoundException;
import thws.librarymanager.application.domain.exceptions.UserNotFoundException;
import thws.librarymanager.application.domain.model.Book;
import thws.librarymanager.application.domain.model.Loan;
import thws.librarymanager.application.domain.model.LoanStatus;
import thws.librarymanager.application.domain.model.User;
import thws.librarymanager.application.ports.in.LoanUseCase;
import thws.librarymanager.application.ports.out.repository.BookPort;
import thws.librarymanager.application.ports.out.repository.LoanPort;
import thws.librarymanager.application.ports.out.repository.UserPort;

import java.time.LocalDate;
import java.util.List;


public class LoanService implements LoanUseCase {

    private final LoanPort loanPort;
    private final UserPort userPort;
    private final BookPort bookPort;


    public LoanService(LoanPort loanPort,
                       UserPort userPort,
                       BookPort bookPort) {
        this.loanPort = loanPort;
        this.userPort = userPort;
        this.bookPort = bookPort;
    }

    // ✅ Creates a new loan
    @Override
    public Loan createLoan(Long userId, Long bookId) {
        return null;

        /*User user = userPort.findById(userId)
               .orElseThrow(() -> new UserNotFoundException(userId));

        Book book = bookPort.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if (loanPort.existsActiveLoanForBook(bookId)) {
            throw new BookAlreadyOnLoanException(bookId);
        }

        LocalDate today = LocalDate.now();
        LocalDate due = today.plusDays(14);

        Loan loan = Loan.createNew(user, book, today, due);
        return loanPort.save(loan);*/
    }

    // ✅ Returns a loan
    @Override
    public Loan returnLoan(Long loanId) {

        Loan loan = loanPort.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        loan.markReturned(LocalDate.now());
        return loanPort.save(loan);
    }

    // ✅ Gets a loan by ID
    @Override
    public Loan getLoanById(Long loanId) {
        return loanPort.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
    }

    // ✅ Searches loans with filters and paging
    @Override
    public List<Loan> searchLoans(Long userId,
                                  Long bookId,
                                  LoanStatus status,
                                  int page,
                                  int size) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("page must be >= 0 and size > 0");
        }

        return loanPort.findLoans(userId, bookId, status, page, size);
    }

    // ✅ Extends the loan period (extra safety added)
    @Override
    public Loan extendLoanPeriod(Long loanId, LocalDate newDueDate) {

        Loan loan = loanPort.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
        /*
        Optional<Loan> optionalLoan = loanPort.findById(loanId);

        if (optionalLoan.isEmpty()) {
            throw new LoanNotFoundException(loanId);
        }
        Loan loan = optionalLoan.get();
        */

        if (loan.isReturned()) {
            throw new IllegalStateException("Returned loan cannot be extended");
        }

        loan.changeDueDate(newDueDate);
        return loanPort.save(loan);
    }

    // ✅ Gets all overdue loans
    @Override
    public List<Loan> getOverdueLoans() {
        return loanPort.findOverdueLoans(LocalDate.now());
    }

    // ✅ Gets all active loans
    @Override
    public List<Loan> getActiveLoans() {
        return loanPort.findActiveLoans();
    }
}
