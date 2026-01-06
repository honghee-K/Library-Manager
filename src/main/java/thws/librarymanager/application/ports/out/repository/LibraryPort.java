package thws.librarymanager.application.ports.out.repository;

import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.Library;

public interface LibraryPort {
    Library save(Library library);

    Optional<Library> getLibraryById(Long id);
    List<Library> findAllLibraries(String location, String name);
    Optional<Library> findByName(String name);

    //Optional<Library> getLibraryByName(String name);

    /*void deleteLibraryById(Long id);

    Long countTotalBooks(Long libraryId);

    List<Book> findBooksInLibrary(Long libraryId);*/
}
