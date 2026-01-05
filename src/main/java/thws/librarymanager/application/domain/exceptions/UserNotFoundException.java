package thws.librarymanager.application.domain.exceptions;

// Thrown when a user with the given ID cannot be found
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super("User not found with id: " + userId);
    }
}
