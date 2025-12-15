package thws.librarymanager.application.ports.in;

import thws.librarymanager.application.domain.model.Library;

import java.util.List;
import java.util.Optional;

public interface LibraryUseCase {
    // Librarian Management Use Cases
    Library addLibrary(Library library);
    void updateLibraryDetails(Long id, String name, String location);
    void deleteLibrary(Long id);
    Optional<Library> getLibraryById(Long id);
    List<Library> getAllLibraries();

    // Book Management delegation (often handled in BookService, but exposed here if needed)
    void addBookToLibrary(Long libraryId, Long bookIsbn);
    void removeBookFromLibrary(Long libraryId, Long bookIsbn);

    // Statistics requirement 1.3
    Long getTotalBookCount(Long libraryId);
}
