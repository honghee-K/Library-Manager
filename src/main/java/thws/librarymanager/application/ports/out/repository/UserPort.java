package thws.librarymanager.application.ports.out.repository;
import thws.librarymanager.application.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserPort {
    User save(User user);

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();

    void deleteById(Long id);
    boolean hasActiveLoans(Long userId);

    boolean existsById(Long id);
    boolean existsByEmail(String email);

}
