package thws.librarymanager.application.ports.out.repository;
import thws.librarymanager.application.domain.model.Loan;
import thws.librarymanager.application.domain.model.LoanStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.model.Loan;
import thws.librarymanager.application.domain.model.LoanStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanPort {

    Loan save(Loan loan);
    Optional<Loan> findById(Long id);
    List<Loan> findAll();
    List<Loan> findByUserId(Long userId);
    List<Loan> findByBookIsbn(Long bookIsbn);
    List<Loan> findByStatus(LoanStatus status);
    List<Loan> findOverdueLoans(LocalDate currentDate);
    Optional<Loan> findActiveLoanByBookIsbn(Long bookIsbn);

    void deleteById(Long id);
    boolean existsById(Long id);

}
