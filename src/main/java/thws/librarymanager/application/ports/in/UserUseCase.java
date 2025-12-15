package thws.librarymanager.application.ports.in;
import thws.librarymanager.application.domain.model.User;
import java.util.List;
import java.util.Optional;
public interface UserUseCase {
    // Create a new user
    User createUser(String name, String email);

    // Get user by ID
    Optional<User> getUserById(Long id);

    // Get user by email
    Optional<User> getUserByEmail(String email);

    // Get all users
    List<User> getAllUsers();

    // Update user information
    void updateUser(Long id, String name, String email);

    // Delete user
    void deleteUser(Long id);
}
