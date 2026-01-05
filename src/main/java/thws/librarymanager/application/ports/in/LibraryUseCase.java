package thws.librarymanager.application.ports.in;

import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.Library;

public interface LibraryUseCase {

    Optional<Library> getLibraryById(Long id);

    List<Library> getAllLibraries();

    /*
    Library addLibrary(Library library);

    void updateLibraryDetails(Long id, String name, String location);

    void deleteLibrary(Long id);

    void addBookToLibrary(Long libraryId, Long bookIsbn);

    void removeBookFromLibrary(Long libraryId, Long bookIsbn);

    Long getTotalBookCount(Long libraryId);*/
}
