package thws.librarymanager.adapters.out.jpa.repository;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import thws.librarymanager.adapters.out.jpa.entity.LibraryEntity;
import thws.librarymanager.application.domain.model.Library;
import thws.librarymanager.application.ports.out.repository.LibraryPort;

@ApplicationScoped
public class JpaLibraryRepository implements PanacheRepositoryBase<LibraryEntity, Long>, LibraryPort {

    public JpaLibraryRepository() {}

    @Override
    public Library getById(long id) {
        return null;
    }
}
