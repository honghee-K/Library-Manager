package thws.librarymanager.application.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.ports.out.repository.BookPort;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookPort bookPort;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private final Long isbn = 1234567890L;

    @BeforeEach
    void setUp() {
        // Initialize a shared Book object for tests
        testBook = new Book(1L, isbn, "Test Title", "Test Author", "Test Genre", null, null);
    }

    @Test
    void addBook_ShouldThrowException_WhenIsbnAlreadyExists() {
        // Given: A book with the same ISBN already exists in the repository
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));
        Library mockLibrary = mock(Library.class);

        // When & Then: Expect an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.addBook(isbn, "Title", "Author", "Genre", mockLibrary);
        });

        // Verify that the save method was never called
        verify(bookPort, never()).save(any(Book.class));
    }

    @Test
    void addBookToLibrary_ShouldUpdateBookAndSave() {
        // Given: The book exists in the repository
        Library targetLibrary = new Library(10L, "Central Library", "Würzburg", new ArrayList<>());
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        // When: Assigning the book to a library
        bookService.addBookToLibrary(isbn, targetLibrary);

        // Then: Verify the book's library state and persistence call
        assertEquals(targetLibrary, testBook.getLibrary());
        verify(bookPort, times(1)).save(testBook);
    }

    @Test
    void updateBook_ShouldThrowException_WhenBookIsOnLoan() {
        // Given: The book exists and is already on loan
        Loan mockLoan = mock(Loan.class);
        testBook.startLoan(mockLoan); // Make the book 'on loan'

        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        // When & Then: Expect an IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            bookService.updateBook(isbn, "New Title", "New Author", "New Genre");
        });

        // Verify that save was never called because of the loan status
        verify(bookPort, never()).save(any(Book.class));
    }

    @Test
    void updateBook_ShouldUpdateDetails_WhenNotOnLoan() {
        // Given: The book exists and is NOT on loan
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        // When
        bookService.updateBook(isbn, "Updated Title", "Updated Author", "Updated Genre");

        // Then
        assertEquals("Updated Title", testBook.getTitle());
        verify(bookPort, times(1)).save(testBook);
    }

    @Test
    void deleteBook_ShouldThrowException_WhenBookIsOnLoan() {
        // Given: The book is on loan
        Loan mockLoan = mock(Loan.class);
        testBook.startLoan(mockLoan);
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            bookService.deleteBook(isbn);
        });

        // Verify that delete was never called
        verify(bookPort, never()).deleteByIsbn(anyLong());
    }
    @Test
    void addBook_ShouldSaveBook_WhenIsbnIsUnique() {
        // Given
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.empty());
        when(bookPort.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Library library = new Library(10L, "Central Library", "Würzburg", new ArrayList<>());

        // When
        Book savedBook = bookService.addBook(isbn, "Title", "Author", "Genre", library);

        // Then
        assertNotNull(savedBook);
        assertEquals(isbn, savedBook.getIsbn());
        verify(bookPort, times(1)).save(any(Book.class));
    }

    @Test
    void deleteBook_ShouldCallDelete_WhenNotOnLoan() {
        // Given
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        // When
        bookService.deleteBook(isbn);

        // Then
        verify(bookPort, times(1)).deleteByIsbn(isbn);
    }

    @Test
    void startLoanForBook_ShouldUpdateStatusAndSave() {
        // Given
        Loan mockLoan = mock(Loan.class);
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        // When
        bookService.startLoanForBook(isbn, mockLoan);

        // Then
        assertTrue(testBook.isOnLoan());
        assertEquals(mockLoan, testBook.getCurrentLoan());
        verify(bookPort, times(1)).save(testBook);
    }
}