package thws.librarymanager.application.ports.out.repository;
import thws.librarymanager.application.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserPort {  // Create or Update
    User save(User user);

    // Read operations
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();

    // Delete operation
    void deleteById(Long id);

    // Check existence
    boolean existsById(Long id);
    boolean existsByEmail(String email);
}
