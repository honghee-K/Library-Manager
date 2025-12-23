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
import thws.librarymanager.application.ports.in.BookUseCase;
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

    private final BookUseCase bookUseCase;


    public LoanService(LoanPort loanPort,
                       UserPort userPort,
                       BookPort bookPort,
                       BookUseCase bookUseCase) {

        this.loanPort = loanPort;
        this.userPort = userPort;
        this.bookPort = bookPort;
        this.bookUseCase = bookUseCase;
    }

    @Override
    public Loan createLoan(Long userId, Long bookId) {


        User user = userPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Book book = bookPort.getBookByIsbn(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if (loanPort.existsActiveLoanForBook(bookId)) {
            throw new BookAlreadyOnLoanException(bookId);
        }

        LocalDate today = LocalDate.now();
        LocalDate due = today.plusDays(14);

        Loan loan = Loan.createLoan(user, book, today, due);
        loanPort.save(loan);

        bookUseCase.startLoanForBook(bookId, loan.getId());
        return loan;
    }

    @Override
    public Loan returnLoan(Long loanId) {

        Loan loan = loanPort.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        loan.setReturned(LocalDate.now());
        return loanPort.save(loan);
    }

    @Override
    public Loan getLoanById(Long loanId) {
        return loanPort.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
    }

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

    @Override
    public Loan extendLoanPeriod(Long loanId, LocalDate newDueDate) {

        Loan loan = loanPort.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        if (loan.isReturned()) {
            throw new IllegalStateException("Returned loan cannot be extended");
        }

        loan.changeDueDate(newDueDate);
        return loanPort.save(loan);
    }
}
