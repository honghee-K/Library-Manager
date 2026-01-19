package thws.librarymanager.adapters.out.jpa.repositories;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
class JpaLibraryRepositoryIT {

    @Inject
    JpaLibraryRepository libraryRepository;
    @Inject
    JpaBookRepository bookRepository;
    @Inject
    EntityManager em;
    @BeforeEach
    @Transactional
    void cleanDb() {
        em.createQuery("DELETE FROM BookEntity").executeUpdate();
        em.createQuery("DELETE FROM LibraryEntity").executeUpdate();
    }

    @Test
    void save_shouldMerge_whenIdExists() {
        Library original =
                libraryRepository.save(
                        new Library(null, "Central Library", "Berlin", null)
                );

        assertNotNull(original.getId());

        Library updated =
                new Library(
                        original.getId(),
                        "Central Library UPDATED",
                        "Munich",
                        null
                );

        Library result = libraryRepository.save(updated);

        Optional<Library> reloaded =
                libraryRepository.getLibraryById(original.getId());

        assertTrue(reloaded.isPresent());
        assertEquals("Central Library UPDATED", reloaded.get().getName());
        assertEquals("Munich", reloaded.get().getLocation());
    }

    @Test
    void getLibraryById_shouldReturnLibrary() {
        Library saved =
                libraryRepository.save(
                        new Library(null, "ID Test", "Berlin", null)
                );

        Optional<Library> found =
                libraryRepository.getLibraryById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("ID Test", found.get().getName());
    }


    @Test
    void findByName() {
        libraryRepository.save(
                new Library(null, "City Library", "Hamburg", null)
        );

        Optional<Library> found =
                libraryRepository.findByName("City Library");

        assertTrue(found.isPresent());
        assertEquals("Hamburg", found.get().getLocation());
    }

    @Test
    void findAllLibraries_withFilter() {
        libraryRepository.save(
                new Library(null, "Lib1", "Berlin", null)
        );
        libraryRepository.save(
                new Library(null, "Lib2", "Munich", null)
        );

        List<Library> berlinLibraries =
                libraryRepository.findAllLibraries("Berlin", null);

        assertEquals(1, berlinLibraries.size());
    }

    @Test
    void deleteLibraryById() {
        Library lib =
                libraryRepository.save(
                        new Library(null, "Delete Me", "Berlin", null)
                );

        libraryRepository.deleteLibraryById(lib.getId());

        Optional<Library> found =
                libraryRepository.getLibraryById(lib.getId());

        assertTrue(found.isEmpty());
    }

    @Test
    void countTotalBooks_initiallyZero() {
        Library lib =
                libraryRepository.save(
                        new Library(null, "Books Lib", "Berlin", null)
                );

        Long count =
                libraryRepository.countTotalBooks(lib.getId());

        assertEquals(0L, count);
    }
    @Test
    void countTotalBooks_withBooks() {
        Library lib =
                libraryRepository.save(
                        new Library(null, "Books Lib", "Berlin", null)
                );

        bookRepository.save(
                new Book(null, 1111L, "Book1", "Author", "Genre", lib, null)
        );
        bookRepository.save(
                new Book(null, 2222L, "Book2", "Author", "Genre", lib, null)
        );

        Long count =
                libraryRepository.countTotalBooks(lib.getId());

        assertEquals(2L, count);
    }
    @Test
    void findBooksInLibrary_returnsOnlyBooksOfThatLibrary() {
        Library lib1 =
                libraryRepository.save(
                        new Library(null, "Lib1", "Berlin", null)
                );

        Library lib2 =
                libraryRepository.save(
                        new Library(null, "Lib2", "Munich", null)
                );

        bookRepository.save(
                new Book(null, 1111L, "Book1", "Author", "Genre", lib1, null)
        );
        bookRepository.save(
                new Book(null, 2222L, "Book2", "Author", "Genre", lib1, null)
        );
        bookRepository.save(
                new Book(null, 3333L, "Book3", "Author", "Genre", lib2, null)
        );

        List<Book> booksInLib1 =
                libraryRepository.findBooksInLibrary(lib1.getId());

        assertEquals(2, booksInLib1.size());
    }



}
