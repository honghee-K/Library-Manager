package thws.librarymanager.adapters.out.jpa.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import thws.librarymanager.adapters.out.jpa.entity.LoanEntity;
import thws.librarymanager.application.domain.model.Loan;
import thws.librarymanager.application.domain.model.LoanStatus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoanConverter {

    @Inject
    JpaConverter jpaConverter; // User ve Book nesneleri için yardımcı converter

    public LoanConverter() {}

    public LoanEntity toEntity(Loan loan) {
        if (loan == null) return null;

        LoanEntity entity = new LoanEntity();
        entity.setId(loan.getId());
        entity.setLoanDate(loan.getLoanDate());
        entity.setDueDate(loan.getDueDate());
        entity.setReturnDate(loan.getReturnDate());
        entity.setStatus(loan.getStatus() != null ? loan.getStatus() : LoanStatus.ACTIVE);

        if (loan.getUser() != null)
            entity.setUser(jpaConverter.toJpaUser(loan.getUser()));

     //   if (loan.getBook() != null)
       //     entity.setBook(jpaConverter.toJpaBook(loan.getBook()));

        return entity;
    }


    public Loan toDomain(LoanEntity entity) {
        if (entity == null) return null;

        return Loan.restore(
                entity.getId(),
                entity.getUser() != null ? jpaConverter.toUser(entity.getUser()) : null,
                null,//entity.getBook() != null ? jpaConverter.toBook(entity.getBook()) : null,
                entity.getLoanDate(),
                entity.getDueDate(),
                entity.getReturnDate(),
                entity.getStatus()
        );

    }


    public List<LoanEntity> toEntityList(List<Loan> loans) {
        if (loans == null || loans.isEmpty()) return Collections.emptyList();
        return loans.stream()
                .filter(Objects::nonNull)
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<Loan> toDomainList(List<LoanEntity> entities) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }


    public Loan toMinimalDomain(LoanEntity entity) {
        if (entity == null) return null;

        return  Loan.restore(
                entity.getId(),
                null, // Wenn keine Userinformationen erforderlich sind
                null,
                entity.getLoanDate(),
                entity.getDueDate(),
                entity.getReturnDate(),
                entity.getStatus()
        );
    }
}
