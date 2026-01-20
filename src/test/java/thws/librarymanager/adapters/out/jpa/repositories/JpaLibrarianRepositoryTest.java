package thws.librarymanager.adapters.out.jpa.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thws.librarymanager.application.domain.models.Librarian;

@QuarkusTest
public class JpaLibrarianRepositoryTest {

    @Inject
    JpaLibrarianRepository jpaLibrarianRepository;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    void setup() {
        entityManager.createQuery("DELETE FROM LibrarianEntity").executeUpdate();
    }

    @Test
    @Transactional
    void save_ShouldCreateAndAssignId() {
        Librarian newLibrarian = new Librarian(null, "Alice Smith");

        Librarian saved = jpaLibrarianRepository.save(newLibrarian);

        assertNotNull(saved.getId());
        assertEquals("Alice Smith", saved.getName());
    }

    @Test
    @Transactional
    void findByName_ShouldReturnLibrarianIfExists() {
        String targetName = "Bob Martin";
        jpaLibrarianRepository.save(new Librarian(null, targetName));

        Optional<Librarian> found = jpaLibrarianRepository.findByName(targetName);

        assertTrue(found.isPresent());
        assertEquals(targetName, found.get().getName());
    }

    @Test
    @Transactional
    void findByName_ShouldReturnEmptyIfNotFound() {
        Optional<Librarian> found = jpaLibrarianRepository.findByName("NonExistent");

        assertTrue(found.isEmpty());
    }

    @Test
    @Transactional
    void save_ShouldUpdateExistingLibrarian() {
        Librarian initial = jpaLibrarianRepository.save(new Librarian(null, "Old Name"));

        Librarian updated = new Librarian(initial.getId(), "New Name");
        jpaLibrarianRepository.save(updated);

        Optional<Librarian> dbCheck = jpaLibrarianRepository.findByName("New Name");
        assertTrue(dbCheck.isPresent());
        assertEquals(initial.getId(), dbCheck.get().getId());
    }
}
