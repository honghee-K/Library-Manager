/*
package thws.librarymanager.application.domain.services;


@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    LoanPort loanPort;

    @Mock
    UserPort userPort;

    @Mock
    BookPort bookPort;

    @Mock
    BookUseCase bookUseCase;

    @InjectMocks
    LoanService loanService;

    private User user;
    private Book book;

    @BeforeEach
    void init() {
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
        when(userPort.findById(1L)).thenReturn(Optional.of(user));
        when(bookPort.getBookByIsbn(10L)).thenReturn(Optional.of(book));
        when(loanPort.existsActiveLoanForBook(10L)).thenReturn(false);
        when(loanPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan loan = loanService.createLoan(1L, 10L);

        assertNotNull(loan);
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        assertEquals(user, loan.getUser());
        assertEquals(book, loan.getBook());
        assertEquals(LocalDate.now().plusDays(14), loan.getDueDate());

        verify(bookUseCase).startLoanForBook(10L, loan);
        verify(loanPort).save(loan);
    }

    @Test
    void createLoan_userNotFound() {
        when(userPort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> loanService.createLoan(1L, 10L));
    }

    @Test
    void createLoan_bookNotFound() {
        when(userPort.findById(1L)).thenReturn(Optional.of(user));
        when(bookPort.getBookByIsbn(10L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> loanService.createLoan(1L, 10L));
    }

    @Test
    void createLoan_bookAlreadyOnLoan() {
        when(userPort.findById(1L)).thenReturn(Optional.of(user));
        when(bookPort.getBookByIsbn(10L)).thenReturn(Optional.of(book));
        when(loanPort.existsActiveLoanForBook(10L)).thenReturn(true);

        assertThrows(BookAlreadyOnLoanException.class,
                () -> loanService.createLoan(1L, 10L));

        verify(loanPort, never()).save(any());
    }

    // ================= RETURN LOAN =================

    @Test
    void returnLoan_success() {
        Loan loan = Loan.createLoan(
                user,
                book,
                LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(10)
        );

        book.startLoan(loan);

        when(loanPort.findById(5L)).thenReturn(Optional.of(loan));
        when(loanPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan returned = loanService.returnLoan(5L);

        assertTrue(returned.isReturned());
        assertNotNull(returned.getReturnDate());
        verify(loanPort).save(loan);
    }

    @Test
    void returnLoan_notFound() {
        when(loanPort.findById(5L)).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class,
                () -> loanService.returnLoan(5L));
    }

    // ================= EXTEND LOAN =================

    @Test
    void extendLoan_success() {
        Loan loan = Loan.createLoan(
                user,
                book,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        when(loanPort.findById(3L)).thenReturn(Optional.of(loan));
        when(loanPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        LocalDate newDueDate = LocalDate.now().plusDays(21);
        Loan updated = loanService.extendLoanPeriod(3L, newDueDate);

        assertEquals(newDueDate, updated.getDueDate());
    }

    @Test
    void extendLoan_alreadyReturned() {
        Loan loan = Loan.createLoan(
                user,
                book,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );
        loan.setReturned(LocalDate.now());

        when(loanPort.findById(3L)).thenReturn(Optional.of(loan));

        assertThrows(IllegalStateException.class,
                () -> loanService.extendLoanPeriod(3L, LocalDate.now().plusDays(10)));
    }

    // ================= SEARCH =================

    @Test
    void searchLoans_success() {
        when(loanPort.findAll(null, null, null, 0, 10))
                .thenReturn(List.of(mock(Loan.class)));

        List<Loan> result = loanService.searchLoans(null, null, null, 0, 10);

        assertEquals(1, result.size());
        verify(loanPort).findAll(null, null, null, 0, 10);
    }

    @Test
    void searchLoans_invalidPaging() {
        assertThrows(IllegalArgumentException.class,
                () -> loanService.searchLoans(null, null, null, -1, 10));

        assertThrows(IllegalArgumentException.class,
                () -> loanService.searchLoans(null, null, null, 0, 0));
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
}
*/
