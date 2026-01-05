package thws.librarymanager.application.ports.out.repository;

import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.User;

public interface UserPort {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteById(Long id);

    boolean hasActiveLoans(Long userId);

    boolean existsById(Long id);

    boolean existsByEmail(String email);
}
