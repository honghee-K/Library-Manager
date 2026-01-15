package thws.librarymanager.application.domain.services;

import java.time.LocalDate;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
import thws.librarymanager.application.ports.in.UserUseCase;
import thws.librarymanager.application.ports.out.repository.BookPort;
import thws.librarymanager.application.ports.out.repository.LoanPort;
import thws.librarymanager.application.ports.out.repository.UserPort;
import thws.librarymanager.application.ports.out.time.TimeProvider;

@ApplicationScoped
@Transactional
public class LoanService implements LoanUseCase {

    private final LoanPort loanPort;
    private final UserPort userPort;
    private final BookPort bookPort;
    private final TimeProvider timeProvider;
    private final BookUseCase bookUseCase;
    private final UserUseCase userUseCase;
    @Inject
    public LoanService(LoanPort loanPort, UserPort userPort, BookPort bookPort, BookUseCase bookUseCase, UserUseCase userUseCase, TimeProvider timeProvider) {

        this.loanPort = loanPort;
        this.userPort = userPort;
        this.bookPort = bookPort;
        this.bookUseCase = bookUseCase;
        this.userUseCase= userUseCase;
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

        bookUseCase.startLoanForBook(book.getIsbn(), loan);
        userUseCase.addLoanToUser(user.getId(), loan);




        return loanPort.save(loan);
    }

   @Override
    public Loan returnLoan(Long loanId) {

        Loan loan = loanPort.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));

        loan.setReturned(timeProvider.today());

        bookUseCase.endLoanForBook(loan.getBook().getIsbn(), loan);
        userUseCase.removeLoanFromUser(loan.getUser().getId(), loan);

        return loanPort.save(loan);
    }

    @Override
    public Loan getLoanById(Long loanId) {
        return loanPort.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));
    }

    @Override
    public List<Loan> getAllLoans(
            Long userId,
            Long isbn,
            LoanStatus status,
            Boolean overdue,
            int page,
            int size
    ) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("page must be >= 0 and size > 0");
        }

        return loanPort.findAll(userId, isbn, status, overdue, page, size);
    }
}
