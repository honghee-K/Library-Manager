package thws.librarymanager.application.ports.in;

import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.User;

public interface UserUseCase {
    User createUser(String name, String email);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    void updateUser(Long id, String name, String email);

    void deleteUser(Long id);
}
