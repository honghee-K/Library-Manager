package thws.librarymanager.application.domain.services;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.in.LibraryUseCase;
import thws.librarymanager.application.ports.out.repository.BookPort;
import thws.librarymanager.application.ports.out.repository.LibraryPort;


@ApplicationScoped
public class LibraryService implements LibraryUseCase {

    private final LibraryPort libraryPort;
    private final BookPort bookPort;

    @Inject
    public LibraryService(LibraryPort libraryPort, BookPort bookPort) {
        this.libraryPort = libraryPort;
        this.bookPort = bookPort;
    }


    @Override
    public Optional<Library> getLibraryById(Long id) {
        return libraryPort.getLibraryById(id);
    }

    @Override
    public List<Library> getAllLibraries(String location, String name) {

        return libraryPort.findAllLibraries(location, name);
    }
    @Override
    public Library addLibrary(Library library) {

        if (libraryPort.findByName(library.getName()).isPresent()) {
            throw new IllegalArgumentException(
                    "Library with name '" + library.getName() + "' already exists."
            );
        }
        return libraryPort.save(library);
    }



    @Override
    public void updateLibrary(Long id, String name, String location) {
        Library existingLibrary = libraryPort
                .getLibraryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Library not found for ID: " + id));

        Library updatedLibrary = existingLibrary.updateLibrary(name, location);

        libraryPort.save(updatedLibrary);
    }


    @Override
    public void deleteLibrary(Long id) {
        Library existingLibrary = libraryPort
                .getLibraryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Library not found for ID: " + id));
        if (libraryPort.countTotalBooks(id) > 0) {
            throw new IllegalStateException("Cannot delete library that contains registered books.");
        }


        libraryPort.deleteLibraryById(id);
    }

    @Override
    public void addBookToLibrary(Long libraryId, Long bookIsbn) {
        Library existingLibrary = libraryPort
                .getLibraryById(libraryId)
                .orElseThrow(() -> new IllegalArgumentException("Library not found for ID: " + libraryId));

        Book book = bookPort.getBookByIsbn(bookIsbn).orElseThrow(() -> new IllegalArgumentException("Book not found."));

        existingLibrary.addBook(book);
        libraryPort.save(existingLibrary);
    }

    @Override
    public void removeBookFromLibrary(Long libraryId, Long bookIsbn) {
        Library existingLibrary = libraryPort
                .getLibraryById(libraryId)
                .orElseThrow(() -> new IllegalArgumentException("Library not found for ID: " + libraryId));

        Book bookToRemove = bookPort.getBookByIsbn(bookIsbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found for ISBN: " + bookIsbn));

        if (!libraryId.equals(bookToRemove.getLibrary())) {
            throw new IllegalStateException("Book does not belong to this library.");
        }

        existingLibrary.removeBook(bookToRemove);
        libraryPort.save(existingLibrary);
    }

    @Override
    public Long getTotalBookCount(Long libraryId) {
        return libraryPort.countTotalBooks(libraryId);
    }
}
