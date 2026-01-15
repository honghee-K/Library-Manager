package thws.librarymanager.application.domain.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.ports.in.UserUseCase;
import thws.librarymanager.application.ports.out.repository.UserPort;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService implements UserUseCase {

    private final UserPort userPort;

    @Inject
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
    public List<User> getAllUsers(int page, int size) {
        return userPort.findAll(page, size);
    }

    @Override
    public void addLoanToUser(Long userId, Loan loan) {
        User user = userPort.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        user.addLoan(loan);

        //userPort.save(user);
    }

    @Override
    public void removeLoanFromUser(Long userId, Loan loan) {
        User user = userPort.findById(userId).orElseThrow();

        user.deleteLoan(loan);

        //userPort.save(user);
    }

    @Override
    public void updateUser(Long id, String name, String email) {
        User user = userPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        user.updateInfo(name, email);
        userPort.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        if (user.hasActiveLoans()) {
            throw new IllegalStateException("Cannot delete user with active loans.");
        }

        userPort.deleteById(id);
    }
}