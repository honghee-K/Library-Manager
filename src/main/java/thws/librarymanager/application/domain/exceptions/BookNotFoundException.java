package thws.librarymanager.application.domain.exceptions;

// Thrown when a book with the given ID cannot be found
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long bookId) {
        super("Book not found with id: " + bookId);
    }
}
