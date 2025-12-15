package thws.librarymanager.application.ports.out.repository;

import thws.librarymanager.application.domain.model.Library;
import thws.librarymanager.application.domain.model.Book;
import java.util.Optional;
import java.util.List;

public interface LibraryPort {
    Library save(Library library);
    Optional<Library> getLibraryById(Long id);
    Optional<Library> getLibraryByName(String name);
    List<Library> findAllLibraries();
    void deleteLibraryById(Long id);

    Long countTotalBooks(Long libraryId);

    List<Book> findBooksInLibrary(Long libraryId);
}