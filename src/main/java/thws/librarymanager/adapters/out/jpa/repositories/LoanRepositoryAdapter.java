package thws.librarymanager.adapters.out.jpa.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import thws.librarymanager.adapters.out.jpa.converter.LoanConverter;
import thws.librarymanager.adapters.out.jpa.entities.LoanEntity;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.LoanStatus;
import thws.librarymanager.application.ports.out.repository.LoanPort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoanRepositoryAdapter implements LoanPort {

    @Inject
    EntityManager em;

    @Inject
    LoanConverter converter;

    // -------------------- SAVE --------------------
    @Override
    @Transactional
    public Loan save(Loan loan) {
        LoanEntity entity = converter.toEntity(loan);

        if (loan.getId() == null) {
            em.persist(entity); // (INSERT)
        } else {
            entity = em.merge(entity); // (UPDATE)
        }

        return converter.toDomain(entity);
    }


    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Loan> findById(Long id) {
        LoanEntity entity = em.find(LoanEntity.class, id);
        return entity != null
                ? Optional.of(converter.toDomain(entity))
                : Optional.empty();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean existsActiveLoanForBook(Long bookId) {
        Long count = em.createQuery(
                        "SELECT COUNT(l) FROM LoanEntity l " +
                                "WHERE l.book.id = :bookId AND l.status = :status",
                        Long.class)
                .setParameter("bookId", bookId)
                .setParameter("status", LoanStatus.ACTIVE)
                .getSingleResult();

        return count > 0;
    }

    // -------------------- FIND LOANS WITH FILTERS --------------------
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Loan> findLoans(Long userId, Long bookId, LoanStatus status, int page, int size) {
        StringBuilder jpql = new StringBuilder("SELECT l FROM LoanEntity l WHERE 1=1 ");

        if (userId != null) jpql.append("AND l.user.id = :userId ");
        if (bookId != null) jpql.append("AND l.book.id = :bookId ");
        if (status != null) jpql.append("AND l.status = :status ");

        TypedQuery<LoanEntity> query = em.createQuery(jpql.toString(), LoanEntity.class);

        if (userId != null) query.setParameter("userId", userId);
        if (bookId != null) query.setParameter("bookId", bookId);
        if (status != null) query.setParameter("status", status);

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList().stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Loan> findActiveLoans() {
        return em.createQuery(
                        "SELECT l FROM LoanEntity l WHERE l.status = :status",
                        LoanEntity.class)
                .setParameter("status", LoanStatus.ACTIVE)
                .getResultList()
                .stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Loan> findOverdueLoans(LocalDate today) {
        return em.createQuery(
                        "SELECT l FROM LoanEntity l " +
                                "WHERE l.status = :status AND l.dueDate < :today",
                        LoanEntity.class)
                .setParameter("status", LoanStatus.ACTIVE)
                .setParameter("today", today)
                .getResultList()
                .stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
}
