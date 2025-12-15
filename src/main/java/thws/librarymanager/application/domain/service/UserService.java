package thws.librarymanager.application.domain.service;

import thws.librarymanager.application.ports.in.UserUseCase;
import thws.librarymanager.application.ports.out.repository.UserPort;
import thws.librarymanager.application.domain.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

public class UserService implements UserUseCase {
    private final UserPort userPort;

    // Constructor injection
    public UserService(UserPort userPort) {
        this.userPort = userPort;
    }

    @Override
    public User createUser(String name, String email) {
        // Business Rule: Check if email already exists
        if (userPort.existsByEmail(email)) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        // Business Rule: Validate input
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address.");
        }

        // Create new user
        User newUser = new User(null, name, email);
        return userPort.save(newUser);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userPort.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userPort.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userPort.findAll();
    }

    @Override
    public void updateUser(Long id, String name, String email) {
        // Find existing user
        User user = userPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // Use domain behavior to update
        user.updateInfo(name, email);

        // Save updated user
        userPort.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        // Business Rule: Check if user exists
        if (!userPort.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        userPort.deleteById(id);
    }

}
