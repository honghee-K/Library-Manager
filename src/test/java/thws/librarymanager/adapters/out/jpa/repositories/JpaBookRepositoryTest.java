package thws.librarymanager.adapters.out.jpa.repositories;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thws.librarymanager.adapters.out.jpa.entities.LibraryEntity;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class JpaBookRepositoryTest {

    @Inject
    JpaBookRepository jpaBookRepository;

    @Inject
    EntityManager entityManager;

    private Long sharedIsbn = 9781234567890L;
    private LibraryEntity sharedLibraryEntity;

    @BeforeEach
    @Transactional
    void setup() {
        entityManager.createQuery("DELETE FROM BookEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM LibraryEntity").executeUpdate();

        sharedLibraryEntity = new LibraryEntity("Central Library", "Würzburg");
        entityManager.persist(sharedLibraryEntity);
    }

    @Test
    @Transactional
    void save_ShouldPersistBookAndReturnDomainModel() {
        Library libraryDomain = new Library(sharedLibraryEntity.getId(), "Central Library", "Würzburg", null);
        Book newBook = new Book(null, sharedIsbn, "Test Title", "Test Author", "Fiction", libraryDomain, null);

        Book savedBook = jpaBookRepository.save(newBook);

        assertNotNull(savedBook.getId());
        assertEquals(sharedIsbn, savedBook.getIsbn());
        assertEquals("Test Title", savedBook.getTitle());
    }

    @Test
    @Transactional
    void getBookByIsbn_ShouldReturnCorrectBook() {
        Library libraryDomain = new Library(sharedLibraryEntity.getId(), "Central Library", "Würzburg", null);
        Book newBook = new Book(null, sharedIsbn, "Test Title", "Test Author", "Fiction", libraryDomain, null);
        jpaBookRepository.save(newBook);

        Optional<Book> foundBook = jpaBookRepository.getBookByIsbn(sharedIsbn);

        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    @Transactional
    void findAll_ShouldApplyFiltersAndPagination() {
        Library libraryDomain = new Library(sharedLibraryEntity.getId(), "Central Library", "Würzburg", null);
        jpaBookRepository.save(new Book(null, 111L, "Book 1", "Author A", "Genre X", libraryDomain, null));
        jpaBookRepository.save(new Book(null, 222L, "Book 2", "Author B", "Genre X", libraryDomain, null));
        jpaBookRepository.save(new Book(null, 333L, "Book 3", "Author A", "Genre Y", libraryDomain, null));

        // When: Search by Genre X
        List<Book> genreXBooks = jpaBookRepository.findAll(0, 10, null, "Genre X");
        assertEquals(2, genreXBooks.size());

        // When: Search by Author A
        List<Book> authorABooks = jpaBookRepository.findAll(0, 10, "Author A", null);
        assertEquals(2, authorABooks.size());
    }

    @Test
    @Transactional
    void deleteByIsbn_ShouldRemoveBookFromDatabase() {
        Library libraryDomain = new Library(sharedLibraryEntity.getId(), "Central Library", "Würzburg", null);
        jpaBookRepository.save(new Book(null, sharedIsbn, "To Be Deleted", "Author", "Genre", libraryDomain, null));

        jpaBookRepository.deleteByIsbn(sharedIsbn);

        Optional<Book> foundBook = jpaBookRepository.getBookByIsbn(sharedIsbn);
        assertFalse(foundBook.isPresent());
    }
}