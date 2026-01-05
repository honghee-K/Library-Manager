package thws.librarymanager.application.ports.out.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.LoanStatus;

public interface LoanPort {

    Loan save(Loan loan);

    Optional<Loan> findById(Long id);

    boolean existsActiveLoanForBook(Long bookId);

    List<Loan> findLoans(Long userId, Long bookId, LoanStatus status, int page, int size);

    List<Loan> findActiveLoans();

    List<Loan> findOverdueLoans(LocalDate today);
}
