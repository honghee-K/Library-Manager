package thws.librarymanager.adapters.out.jpa.repository;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import thws.librarymanager.adapters.out.jpa.entity.LibraryEntity;
import thws.librarymanager.application.domain.model.Library;
import thws.librarymanager.application.ports.out.repository.LibraryRepository;

@ApplicationScoped
public class JpaLibraryRepository implements PanacheRepositoryBase<LibraryEntity, Long>, LibraryRepository {

    public JpaLibraryRepository() {}

    @Override
    public Library getById(long id) {
        return null;
    }
}
