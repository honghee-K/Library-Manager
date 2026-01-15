package thws.librarymanager.adapters.out.jpa.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import thws.librarymanager.adapters.out.jpa.converter.JpaConverter;
import thws.librarymanager.adapters.out.jpa.entities.LoanEntity;
import thws.librarymanager.adapters.out.jpa.enums.LoanStatusJpa;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.LoanStatus;
import thws.librarymanager.application.ports.out.repository.LoanPort;

@ApplicationScoped
public class JpaLoanRepository implements LoanPort {

    @Inject
    EntityManager em;

    @Inject
    JpaConverter converter;
    @Inject
    private JpaConverter jpaConverter;
    @Inject
    private EntityManager entityManager;

    @Override
    @Transactional
    public Loan save(Loan loan) {
        LoanEntity entity = converter.toJpaLoan(loan);

        if (loan.getId() == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }

        return converter.toLoan(entity);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Loan> findById(Long id) {
        LoanEntity entity = em.find(LoanEntity.class, id);
        return entity != null ? Optional.of(converter.toLoan(entity)) : Optional.empty();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean existsActiveLoanForBook(Long isbn) {
        Long count = em.createQuery(
                        "SELECT COUNT(l) FROM LoanEntity l WHERE l.book.isbn = :isbn AND l.status = :status",
                        Long.class)
                .setParameter("isbn", isbn)
                .setParameter("status", LoanStatusJpa.ACTIVE)
                .getSingleResult();

        return count > 0;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Loan> findAll(Long userId, Long bookId, LoanStatus status, int page, int size) {
        StringBuilder jpql = new StringBuilder("SELECT l FROM LoanEntity l WHERE 1=1 ");

        if (userId != null) jpql.append("AND l.user.id = :userId ");
        if (bookId != null) jpql.append("AND l.book.id = :bookId ");
        if (status != null) jpql.append("AND l.status = :status ");

        TypedQuery<LoanEntity> query = em.createQuery(jpql.toString(), LoanEntity.class);

        if (userId != null) query.setParameter("userId", userId);
        if (bookId != null) query.setParameter("bookId", bookId);
        if (status != null) query.setParameter("status", LoanStatusJpa.valueOf(status.name()));

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList().stream().map(converter::toLoan).collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Loan> findActiveLoans() {
        return em
                .createQuery("SELECT l FROM LoanEntity l WHERE l.status = :status", LoanEntity.class)
                .setParameter("status", LoanStatusJpa.ACTIVE)
                .getResultList()
                .stream()
                .map(converter::toLoan)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Loan> findOverdueLoans(LocalDate today) {
        return em
                .createQuery(
                        "SELECT l FROM LoanEntity l " + "WHERE l.status = :status AND l.dueDate < :today",
                        LoanEntity.class)
                .setParameter("status", LoanStatusJpa.ACTIVE)
                .setParameter("today", today)
                .getResultList()
                .stream()
                .map(converter::toLoan)
                .collect(Collectors.toList());
    }
    @Override
    public List<Loan> findAll(
            Long userId,
            Long isbn,
            LoanStatus status,
            Boolean overdue,
            int page,
            int size
    ) {
        StringBuilder jpql = new StringBuilder(
                "select l from LoanEntity l where 1=1"
        );

        if (userId != null) {
            jpql.append(" and l.user.id = :userId");
        }
        if (isbn != null) {
            jpql.append(" and l.book.isbn = :isbn");
        }
        if (status != null) {
            jpql.append(" and l.status = :status");
        }
        if (Boolean.TRUE.equals(overdue)) {
            jpql.append(" and l.returnDate is null and l.dueDate < CURRENT_DATE");
        }

        jpql.append(" order by l.id");

        TypedQuery<LoanEntity> query =
                entityManager.createQuery(jpql.toString(), LoanEntity.class);

        if (userId != null) query.setParameter("userId", userId);
        if (isbn != null) query.setParameter("isbn", isbn);
        if (status != null) query.setParameter("status", status);

        query.setFirstResult(page * size);
        query.setMaxResults(size);


        return query.getResultList()
                .stream()
                .map(jpaConverter::toLoan)
                .toList();
    }

}
