package thws.librarymanager.adapters.out.jpa.repository;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import thws.librarymanager.adapters.out.jpa.entity.LibraryEntity;
import thws.librarymanager.application.domain.model.Book;
import thws.librarymanager.application.domain.model.Library;
import thws.librarymanager.application.ports.out.repository.LibraryPort;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaLibraryRepository implements PanacheRepositoryBase<LibraryEntity, Long>, LibraryPort {

    public JpaLibraryRepository() {}

    @Override
    public Library save(Library library) {
        return null;
    }

    @Override
    public Optional<Library> getLibraryById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Library> getLibraryByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Library> findAllLibraries() {
        return null;
    }

    @Override
    public void deleteLibraryById(Long id) {

    }

    @Override
    public Long countTotalBooks(Long libraryId) {
        return null;
    }

    @Override
    public List<Book> findBooksInLibrary(Long libraryId) {
        return null;
    }

}
