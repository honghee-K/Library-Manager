package thws.librarymanager.application.ports.out.repository;

import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.User;

public interface UserPort {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll(int page, int size);

    void deleteById(Long id);

    boolean hasActiveLoans(Long userId);

    boolean existsByEmail(String email);
}
