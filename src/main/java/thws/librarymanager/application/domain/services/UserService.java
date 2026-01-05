package thws.librarymanager.application.domain.services;

import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.ports.in.UserUseCase;
import thws.librarymanager.application.ports.out.repository.UserPort;

public class UserService implements UserUseCase {
    private final UserPort userPort;

    public UserService(UserPort userPort) {
        this.userPort = userPort;
    }

    @Override
    public User createUser(String name, String email) {
        if (userPort.existsByEmail(email)) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        User user = new User(null, name, email);

        return userPort.save(user);
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
        User user =
                userPort.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        user.updateInfo(name, email);

        userPort.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userPort.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        if (userPort.hasActiveLoans(id)) {
            throw new IllegalStateException("Cannot delete user with active loans.");
        }

        userPort.deleteById(id);
    }
}
