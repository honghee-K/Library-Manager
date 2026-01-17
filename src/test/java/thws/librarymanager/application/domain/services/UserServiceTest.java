package thws.librarymanager.application.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.ports.out.repository.UserPort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserPort userPort;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private final Long userId = 1L;
    private final String email = "test@example.com";

    @BeforeEach
    void setUp() {
        // Based on User(Long id, String name, String email)
        testUser = new User(userId, "Test User", email);
    }

    @Test
    void createUser_ShouldSaveUser_WhenEmailIsUnique() {
        when(userPort.existsByEmail(email)).thenReturn(false);
        when(userPort.save(any(User.class))).thenReturn(testUser);

        User created = userService.createUser("Test User", email);

        assertNotNull(created);
        verify(userPort).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailExists() {
        when(userPort.existsByEmail(email)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser("User", email));
        verify(userPort, never()).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenIdExists() {
        when(userPort.findById(userId)).thenReturn(Optional.of(testUser));

        Optional<User> found = userService.getUserById(userId);

        assertTrue(found.isPresent());
        assertEquals(userId, found.get().getId());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        when(userPort.findAll(0, 10)).thenReturn(List.of(testUser));

        List<User> users = userService.getAllUsers(0, 10);

        assertEquals(1, users.size());
        verify(userPort).findAll(0, 10);
    }

    @Test
    void addLoanToUser_ShouldIncreaseLoanListSize() {
        Loan mockLoan = mock(Loan.class);
        when(userPort.findById(userId)).thenReturn(Optional.of(testUser));

        userService.addLoanToUser(userId, mockLoan);

        assertTrue(testUser.getLoans().contains(mockLoan));
        // Verify dirty checking: no save call expected in service
        verify(userPort, never()).save(testUser);
    }

    @Test
    void removeLoanFromUser_ShouldDecreaseLoanListSize() {
        Loan mockLoan = mock(Loan.class);
        testUser.addLoan(mockLoan);
        when(userPort.findById(userId)).thenReturn(Optional.of(testUser));

        userService.removeLoanFromUser(userId, mockLoan);

        assertFalse(testUser.getLoans().contains(mockLoan));
    }

    @Test
    void updateUser_ShouldUpdateDetailsAndSave() {
        when(userPort.findById(userId)).thenReturn(Optional.of(testUser));

        userService.updateUser(userId, "New Name", "new@example.com");

        assertEquals("New Name", testUser.getName());
        assertEquals("new@example.com", testUser.getEmail());
        verify(userPort).save(testUser);
    }

    @Test
    void deleteUser_ShouldCallDelete_WhenNoActiveLoans() {
        when(userPort.findById(userId)).thenReturn(Optional.of(testUser));
        // testUser has no active loans by default

        userService.deleteUser(userId);

        verify(userPort).deleteById(userId);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserHasActiveLoans() {
        Loan activeLoan = mock(Loan.class);
        when(activeLoan.isActive()).thenReturn(true);
        testUser.addLoan(activeLoan);

        when(userPort.findById(userId)).thenReturn(Optional.of(testUser));

        assertThrows(IllegalStateException.class, () -> userService.deleteUser(userId));
        verify(userPort, never()).deleteById(userId);
    }
}