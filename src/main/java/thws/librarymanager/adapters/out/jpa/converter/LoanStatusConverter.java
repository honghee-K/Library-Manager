package thws.librarymanager.adapters.out.jpa.converter;

import jakarta.enterprise.context.ApplicationScoped;

import thws.librarymanager.adapters.out.jpa.enums.LoanStatusJpa;
import thws.librarymanager.application.domain.models.LoanStatus;

@ApplicationScoped
public class LoanStatusConverter {
    public LoanStatusJpa toJpa(LoanStatus status) {
        return status == null ? null : LoanStatusJpa.valueOf(status.name());
    }

    public LoanStatus toDomain(LoanStatusJpa status) {
        return status == null ? null : LoanStatus.valueOf(status.name());
    }
}
