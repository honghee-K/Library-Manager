package thws.librarymanager.adapters.out.jpa.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thws.librarymanager.application.domain.models.*;
import thws.librarymanager.application.ports.out.repository.BookPort;
import thws.librarymanager.application.ports.out.repository.LibraryPort;
import thws.librarymanager.application.ports.out.repository.UserPort;

@QuarkusTest
class JpaLoanRepositoryIT {

    @Inject
    JpaLoanRepository loanRepository;

    @Inject
    LibraryPort libraryPort;

    @Inject
    BookPort bookPort;

    @Inject
    UserPort userPort;

    @Inject
    EntityManager em;

    private User user;
    private Book book;
    private Library library;

    @BeforeEach
    @Transactional
    void cleanDbAndInit() {
        em.createQuery("DELETE FROM LoanEntity").executeUpdate();
        em.createQuery("DELETE FROM BookEntity").executeUpdate();
        em.createQuery("DELETE FROM UserEntity").executeUpdate();
        em.createQuery("DELETE FROM LibraryEntity").executeUpdate();

        library = libraryPort.save(new Library(null, "Central Library", "Berlin", null));

        user = userPort.save(new User(null, "Max Mustermann", "max@test.de"));

        book = bookPort.save(new Book(null, 1111L, "Test Book", "Author", "Genre", library, null));
    }

    @Test
    void save_and_findById() {
        Loan loan = Loan.createLoan(user, book, LocalDate.now(), LocalDate.now().plusDays(14));

        Loan saved = loanRepository.save(loan);

        assertNotNull(saved.getId());

        Optional<Loan> found = loanRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(LoanStatus.ACTIVE, found.get().getStatus());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        Optional<Loan> found = loanRepository.findById(99999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void existsActiveLoanForBook_returnsTrue_whenActiveLoanExists() {
        loanRepository.save(
                Loan.createLoan(user, book, LocalDate.now(), LocalDate.now().plusDays(7)));

        boolean exists = loanRepository.existsActiveLoanForBook(book.getIsbn());

        assertTrue(exists);
    }

    @Test
    void existsActiveLoanForBook_returnsFalse_whenNoLoanExists() {
        boolean exists = loanRepository.existsActiveLoanForBook(book.getIsbn());

        assertFalse(exists);
    }

    @Test
    void findActiveLoans_returnsOnlyActiveLoans() {
        loanRepository.save(
                Loan.createLoan(user, book, LocalDate.now(), LocalDate.now().plusDays(7)));

        List<Loan> activeLoans = loanRepository.findActiveLoans();

        assertEquals(1, activeLoans.size());
        assertEquals(LoanStatus.ACTIVE, activeLoans.get(0).getStatus());
    }

    @Test
    void findOverdueLoans_returnsOnlyOverdueLoans() {
        Loan overdue = Loan.createLoan(
                user, book, LocalDate.now().minusDays(20), LocalDate.now().minusDays(5));

        loanRepository.save(overdue);

        List<Loan> overdueLoans = loanRepository.findOverdueLoans(LocalDate.now());

        assertEquals(1, overdueLoans.size());
        assertTrue(overdueLoans.get(0).isOverdue(LocalDate.now()));
    }

    @Test
    void findAll_withFiltersAndPaging() {
        loanRepository.save(
                Loan.createLoan(user, book, LocalDate.now(), LocalDate.now().plusDays(10)));

        List<Loan> result = loanRepository.findAll(user.getId(), book.getIsbn(), LoanStatus.ACTIVE, false, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void findAll_shouldApplyPagingCorrectly() {
        for (int i = 0; i < 15; i++) {
            Book b = bookPort.save(new Book(null, 2000L + i, "Book " + i, "A", "G", library, null));

            loanRepository.save(
                    Loan.createLoan(user, b, LocalDate.now(), LocalDate.now().plusDays(7)));
        }

        List<Loan> firstPage = loanRepository.findAll(null, null, LoanStatus.ACTIVE, false, 0, 10);

        List<Loan> secondPage = loanRepository.findAll(null, null, LoanStatus.ACTIVE, false, 1, 10);

        assertEquals(10, firstPage.size());
        assertEquals(5, secondPage.size());
    }
}
