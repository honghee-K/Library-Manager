
package thws.librarymanager.application.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thws.librarymanager.application.domain.exceptions.BookAlreadyOnLoanException;
import thws.librarymanager.application.domain.exceptions.LoanNotFoundException;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.LoanStatus;
import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.in.UserUseCase;
import thws.librarymanager.application.ports.out.repository.BookPort;
import thws.librarymanager.application.ports.out.repository.LoanPort;
import thws.librarymanager.application.ports.out.repository.UserPort;
import thws.librarymanager.application.ports.out.time.TimeProvider;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    LoanPort loanPort;

    @Mock
    BookPort bookPort;

    @Mock
    UserPort userPort;

    @Mock
    BookUseCase bookUseCase;

    @Mock
    UserUseCase userUseCase;

    @Mock
    TimeProvider timeProvider;

    @InjectMocks
    LoanService loanService;

    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Ali", "ali@mail.com");
        book = new Book(
                10L,
                1234567890L,
                "Clean Code",
                "Robert Martin",
                "Software",
                null,
                null
        );
    }

    // ================= CREATE LOAN =================

    @Test
    void createLoan_success() {
        when(timeProvider.today()).thenReturn(LocalDate.of(2026, 1, 1));
        when(loanPort.existsActiveLoanForBook(book.getIsbn())).thenReturn(false);
        when(loanPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan loan = loanService.createLoan(user, book);

        assertNotNull(loan);
        assertEquals(user, loan.getUser());
        assertEquals(book, loan.getBook());
        assertEquals(LocalDate.of(2026, 1, 15), loan.getDueDate());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());

        verify(bookUseCase).startLoanForBook(book.getIsbn(), loan);
        verify(userUseCase).addLoanToUser(user.getId(), loan);
        verify(loanPort).save(loan);
    }

    @Test
    void createLoan_bookAlreadyOnLoan() {
        when(loanPort.existsActiveLoanForBook(book.getIsbn())).thenReturn(true);

        assertThrows(BookAlreadyOnLoanException.class,
                () -> loanService.createLoan(user, book));

        verify(loanPort, never()).save(any());
    }

    // ================= RETURN LOAN =================

    @Test
    void returnLoan_success() {
        Loan loan = Loan.createLoan(
                user,
                book,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(10)
        );

        when(loanPort.findById(5L)).thenReturn(Optional.of(loan));
        when(timeProvider.today()).thenReturn(LocalDate.of(2026, 1, 20));
        when(loanPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan returned = loanService.returnLoan(5L);

        assertTrue(returned.isReturned());
        assertEquals(LocalDate.of(2026, 1, 20), returned.getReturnDate());

        verify(bookUseCase).endLoanForBook(book.getIsbn(), loan);
        verify(userUseCase).removeLoanFromUser(user.getId(), loan);
        verify(loanPort).save(loan);
    }

    @Test
    void returnLoan_notFound() {
        when(loanPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class,
                () -> loanService.returnLoan(99L));
    }

    // ================= GET BY ID =================

    @Test
    void getLoanById_success() {
        Loan loan = mock(Loan.class);
        when(loanPort.findById(1L)).thenReturn(Optional.of(loan));

        Loan result = loanService.getLoanById(1L);

        assertEquals(loan, result);
    }

    @Test
    void getLoanById_notFound() {
        when(loanPort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class,
                () -> loanService.getLoanById(1L));
    }

    // ================= GET ALL LOANS =================

    @Test
    void getAllLoans_success() {
        when(loanPort.findAll(null, null, null, null, 0, 10))
                .thenReturn(List.of(mock(Loan.class)));

        List<Loan> result =
                loanService.getAllLoans(null, null, null, null, 0, 10);

        assertEquals(1, result.size());
        verify(loanPort).findAll(null, null, null, null, 0, 10);
    }

    @Test
    void getAllLoans_invalidPaging() {
        assertThrows(IllegalArgumentException.class,
                () -> loanService.getAllLoans(null, null, null, null, -1, 10));

        assertThrows(IllegalArgumentException.class,
                () -> loanService.getAllLoans(null, null, null, null, 0, 0));
    }
}
