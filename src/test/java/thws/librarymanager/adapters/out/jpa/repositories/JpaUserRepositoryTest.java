package thws.librarymanager.adapters.out.jpa.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thws.librarymanager.adapters.out.jpa.entities.BookEntity;
import thws.librarymanager.adapters.out.jpa.entities.LoanEntity;
import thws.librarymanager.adapters.out.jpa.entities.UserEntity;
import thws.librarymanager.adapters.out.jpa.enums.LoanStatusJpa;
import thws.librarymanager.application.domain.models.User;

@QuarkusTest
public class JpaUserRepositoryTest {

    @Inject
    JpaUserRepository jpaUserRepository;

    @Inject
    EntityManager entityManager;

    private static final String TEST_USER_NAME = "John Doe";
    private static final String TEST_USER_EMAIL = "john@example.com";

    @BeforeEach
    @Transactional
    void setup() {
        entityManager.createQuery("DELETE FROM LoanEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM BookEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM UserEntity").executeUpdate();
    }

    @Test
    @Transactional
    void save_ShouldPersistUserSuccessfully() {
        User newUser = new User(null, TEST_USER_NAME, TEST_USER_EMAIL);

        User saved = jpaUserRepository.save(newUser);

        assertNotNull(saved.getId());
        assertEquals(TEST_USER_EMAIL, saved.getEmail());
    }

    @Test
    @Transactional
    void existsByEmail_ShouldReturnTrueWhenEmailExists() {
        jpaUserRepository.save(new User(null, TEST_USER_NAME, TEST_USER_EMAIL));

        boolean exists = jpaUserRepository.existsByEmail(TEST_USER_EMAIL);

        assertTrue(exists);
    }

    @Test
    @Transactional
    void findById_ShouldReturnUserWhenIdExists() {
        User saved = jpaUserRepository.save(new User(null, TEST_USER_NAME, TEST_USER_EMAIL));

        Optional<User> found = jpaUserRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(TEST_USER_NAME, found.get().getName());
    }

    @Test
    @Transactional
    void hasActiveLoans_ShouldReturnTrueWhenUserHasActiveLoan() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Active User");
        userEntity.setEmail("active@test.com");
        entityManager.persist(userEntity);

        BookEntity bookEntity = new BookEntity();
        bookEntity.setIsbn(11223344L);
        bookEntity.setTitle("Reference Book");
        entityManager.persist(bookEntity);

        LoanEntity activeLoan = new LoanEntity(
                userEntity, bookEntity, LocalDate.now(), LocalDate.now().plusDays(7), LoanStatusJpa.ACTIVE);
        entityManager.persist(activeLoan);

        boolean result = jpaUserRepository.hasActiveLoans(userEntity.getId());

        assertTrue(result);
    }

    @Test
    @Transactional
    void findAll_ShouldReturnPaginatedList() {
        jpaUserRepository.save(new User(null, "User 1", "u1@test.com"));
        jpaUserRepository.save(new User(null, "User 2", "u2@test.com"));

        List<User> result = jpaUserRepository.findAll(0, 10);

        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    void deleteById_ShouldRemoveUser() {
        User saved = jpaUserRepository.save(new User(null, "Delete Me", "bye@test.com"));
        Long id = saved.getId();

        jpaUserRepository.deleteById(id);

        assertFalse(jpaUserRepository.findById(id).isPresent());
    }
}
