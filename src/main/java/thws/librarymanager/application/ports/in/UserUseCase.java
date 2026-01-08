package thws.librarymanager.application.ports.in;

import java.util.List;
import java.util.Optional;

import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.User;

public interface UserUseCase {
    User createUser(String name, String email);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers(int page, int size);

    void updateUser(Long id, String name, String email);

    void deleteUser(Long id);

    void addLoanToUser(Long userId, Loan loan);

    void removeLoanFromUser(Long userId, Loan loan);

}
