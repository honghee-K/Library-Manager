package thws.librarymanager.application.domain.service;

import thws.librarymanager.application.domain.exceptions.BookAlreadyOnLoanException;
import thws.librarymanager.application.domain.exceptions.BookNotFoundException;
import thws.librarymanager.application.domain.exceptions.LoanNotFoundException;
import thws.librarymanager.application.domain.exceptions.UserNotFoundException;
import thws.librarymanager.application.domain.model.Loan;
import thws.librarymanager.application.domain.model.LoanStatus;
import thws.librarymanager.application.domain.model.User;
import thws.librarymanager.application.ports.in.LoanUseCase;
import thws.librarymanager.application.domain.model.Book;

//import java.awt.print.Book;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import thws.librarymanager.application.domain.exceptions.*;
import thws.librarymanager.application.domain.model.Book;
import thws.librarymanager.application.domain.model.Loan;
import thws.librarymanager.application.domain.model.LoanStatus;
import thws.librarymanager.application.domain.model.User;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.in.LoanUseCase;
import thws.librarymanager.application.ports.in.UserUseCase;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoanService implements LoanUseCase {

    private final Map<Long, Loan> loanStore = new HashMap<>();
    private Long loanIdCounter = 1L;

    private final UserUseCase userUseCase;
    private final BookUseCase bookUseCase;

    public LoanService(UserUseCase userUseCase, BookUseCase bookUseCase) {
        this.userUseCase = userUseCase;
        this.bookUseCase = bookUseCase;
    }


    @Override
    public Loan createLoan(Long userId, String bookIsbn)
            throws UserNotFoundException,
            BookNotFoundException,
            BookAlreadyOnLoanException {

        User user = userUseCase.getById(userId);
        if (user == null)
            throw new UserNotFoundException("User not found with id: " + userId);

        long isbn = Long.parseLong(bookIsbn);
        Book book = bookUseCase.getByIsbn(isbn);
        if (book == null)
            throw new BookNotFoundException("Book not found with isbn: " + bookIsbn);

        if (book.getCurrentLoan() != null &&
                book.getCurrentLoan().getStatus() == LoanStatus.ACTIVE) {

            throw new BookAlreadyOnLoanException("Book is already on loan.");
        }

        Loan loan = user.startLoan(book, 14);

        loanStore.put(loanIdCounter++, loan);

        return loan;
    }


    @Override
    public void returnBook(Long loanId)
            throws LoanNotFoundException {

        Loan loan = getNotNullLoan(loanId);

        loan.getUser().endLoan(loan);

        loan.getBook().setCurrentLoan(null);
    }


    @Override
    public Loan getLoanById(Long loanId)
            throws LoanNotFoundException {

        return getNotNullLoan(loanId);
    }


    @Override
    public List<Loan> getActiveLoans() {
        return loanStore.values().stream()
                .filter(l -> l.getStatus() == LoanStatus.ACTIVE)
                .collect(Collectors.toList());
    }


    @Override
    public List<Loan> getOverdueLoans() {
        return loanStore.values().stream()
                .filter(l ->
                        l.getStatus() == LoanStatus.ACTIVE &&
                                l.getDueDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    // GUARD METHOD
    private Loan getNotNullLoan(Long loanId)
            throws LoanNotFoundException {

        Loan loan = loanStore.get(loanId);

        if (loan == null)
            throw new LoanNotFoundException("Loan not found with id: " + loanId);

        return loan;
    }
}
