package thws.librarymanager.application.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thws.librarymanager.application.domain.models.*;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.out.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock LoanPort loanPort;
    @Mock UserPort userPort;
    @Mock BookPort bookPort;
    @Mock BookUseCase bookUseCase;

    @InjectMocks
    LoanService loanService;

    private Long userId;
    private Long bookId;

    @BeforeEach
    void init() {
        userId = 1L;
        bookId = 10L;
    }

    @Test
    void createLoan_success() {
        when(userPort.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(bookPort.getBookByIsbn(bookId)).thenReturn(Optional.of(mock(Book.class)));
        when(loanPort.existsActiveLoanForBook(bookId)).thenReturn(false);
        when(loanPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan loan = loanService.createLoan(userId, bookId);

        assertEquals(userId, loan.getUserId());
        assertEquals(bookId, loan.getBookId());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        assertEquals(LocalDate.now().plusDays(14), loan.getDueDate());

        verify(loanPort).save(any());
    }

    @Test
    void returnLoan_success() {
        Loan loan = Loan.createLoan(
                userId,
                bookId,
                LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(10)
        );

        when(loanPort.findById(5L)).thenReturn(Optional.of(loan));
        when(bookPort.getBookByIsbn(bookId))
                .thenReturn(Optional.of(mock(Book.class)));
        when(loanPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan returned = loanService.returnLoan(5L);

        assertTrue(returned.isReturned());
        assertNotNull(returned.getReturnDate());
    }

    @Test
    void extendLoan_success() {
        Loan loan = Loan.createLoan(
                userId,
                bookId,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        when(loanPort.findById(3L)).thenReturn(Optional.of(loan));
        when(loanPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan updated = loanService.extendLoanPeriod(3L, LocalDate.now().plusDays(21));

        assertEquals(LocalDate.now().plusDays(21), updated.getDueDate());
    }
}
