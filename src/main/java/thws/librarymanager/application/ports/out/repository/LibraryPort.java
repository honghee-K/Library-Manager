package thws.librarymanager.application.ports.out.repository;

import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.Library;

public interface LibraryPort {
    Library save(Library library);

    Optional<Library> getLibraryById(Long id);

    // Optional<Library> getLibraryByName(String name);

    List<Library> findAllLibraries();

    /*void deleteLibraryById(Long id);

    Long countTotalBooks(Long libraryId);

    List<Book> findBooksInLibrary(Long libraryId);*/
}
