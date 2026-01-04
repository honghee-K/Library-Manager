package thws.librarymanager.adapters.out.jpa.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import thws.librarymanager.adapters.out.jpa.entities.LoanEntity;
import thws.librarymanager.adapters.out.jpa.enums.LoanStatusJpa;
import thws.librarymanager.adapters.out.jpa.converter.JpaConverter;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.LoanStatus;
import thws.librarymanager.application.ports.out.repository.LoanPort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaLoanRepository implements LoanPort {

    @Inject
    EntityManager em;

    @Inject
    JpaConverter converter;

    // -------------------- SAVE --------------------
    @Override
    @Transactional
    public Loan save(Loan loan) {
        LoanEntity entity = converter.toJpaLoan(loan);

        if (loan.getId() == null) {
            em.persist(entity); // INSERT
        } else {
            entity = em.merge(entity); // UPDATE
        }

        return converter.toLoan(entity);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Loan> findById(Long id) {
        LoanEntity entity = em.find(LoanEntity.class, id);
        return entity != null
                ? Optional.of(converter.toLoan(entity))
                : Optional.empty();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean existsActiveLoanForBook(Long bookId) {
        Long count = em.createQuery(
                        "SELECT COUNT(l) FROM LoanEntity l " +
                                "WHERE l.bookId = :bookId AND l.status = :status",
                        Long.class)
                .setParameter("bookId", bookId)
                .setParameter("status", LoanStatusJpa.ACTIVE)
                .getSingleResult();

        return count > 0;
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Loan> findLoans(Long userId,
                                Long bookId,
                                LoanStatus status,
                                int page,
                                int size) {

        StringBuilder jpql = new StringBuilder("SELECT l FROM LoanEntity l WHERE 1=1 ");

        if (userId != null) jpql.append("AND l.userId = :userId ");
        if (bookId != null) jpql.append("AND l.bookId = :bookId ");
        if (status != null) jpql.append("AND l.status = :status ");

        TypedQuery<LoanEntity> query = em.createQuery(jpql.toString(), LoanEntity.class);

        if (userId != null) query.setParameter("userId", userId);
        if (bookId != null) query.setParameter("bookId", bookId);
        if (status != null) query.setParameter("status", LoanStatusJpa.valueOf(status.name()));

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList()
                .stream()
                .map(converter::toLoan)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Loan> findActiveLoans() {
        return em.createQuery(
                        "SELECT l FROM LoanEntity l WHERE l.status = :status",
                        LoanEntity.class)
                .setParameter("status", LoanStatusJpa.ACTIVE)
                .getResultList()
                .stream()
                .map(converter::toLoan)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Loan> findOverdueLoans(LocalDate today) {
        return em.createQuery(
                        "SELECT l FROM LoanEntity l " +
                                "WHERE l.status = :status AND l.dueDate < :today",
                        LoanEntity.class)
                .setParameter("status", LoanStatusJpa.ACTIVE)
                .setParameter("today", today)
                .getResultList()
                .stream()
                .map(converter::toLoan)
                .collect(Collectors.toList());
    }
}
