package thws.librarymanager.application.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

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
        testBook = new Book(1L, isbn, "Test Title", "Test Author", "Test Genre", null, null);
    }

    @Test
    void addBook_ShouldThrowException_WhenIsbnAlreadyExists() {
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));
        Library mockLibrary = mock(Library.class);

        assertThrows(IllegalArgumentException.class, () -> {
            bookService.addBook(isbn, "Title", "Author", "Genre", mockLibrary);
        });

        verify(bookPort, never()).save(any(Book.class));
    }

    @Test
    void addBookToLibrary_ShouldUpdateBookAndSave() {
        Library targetLibrary = new Library(10L, "Central Library", "Würzburg", new ArrayList<>());
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        bookService.addBookToLibrary(isbn, targetLibrary);

        assertEquals(targetLibrary, testBook.getLibrary());
        verify(bookPort, times(1)).save(testBook);
    }

    @Test
    void updateBook_ShouldThrowException_WhenBookIsOnLoan() {
        Loan mockLoan = mock(Loan.class);
        testBook.startLoan(mockLoan);

        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        assertThrows(IllegalStateException.class, () -> {
            bookService.updateBook(isbn, "New Title", "New Author", "New Genre");
        });

        verify(bookPort, never()).save(any(Book.class));
    }

    @Test
    void updateBook_ShouldUpdateDetails_WhenNotOnLoan() {
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        bookService.updateBook(isbn, "Updated Title", "Updated Author", "Updated Genre");

        assertEquals("Updated Title", testBook.getTitle());
        verify(bookPort, times(1)).save(testBook);
    }

    @Test
    void deleteBook_ShouldThrowException_WhenBookIsOnLoan() {
        Loan mockLoan = mock(Loan.class);
        testBook.startLoan(mockLoan);
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        assertThrows(IllegalStateException.class, () -> {
            bookService.deleteBook(isbn);
        });

        verify(bookPort, never()).deleteByIsbn(anyLong());
    }

    @Test
    void addBook_ShouldSaveBook_WhenIsbnIsUnique() {
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.empty());
        when(bookPort.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Library library = new Library(10L, "Central Library", "Würzburg", new ArrayList<>());

        Book savedBook = bookService.addBook(isbn, "Title", "Author", "Genre", library);

        assertNotNull(savedBook);
        assertEquals(isbn, savedBook.getIsbn());
        verify(bookPort, times(1)).save(any(Book.class));
    }

    @Test
    void deleteBook_ShouldCallDelete_WhenNotOnLoan() {
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        bookService.deleteBook(isbn);

        verify(bookPort, times(1)).deleteByIsbn(isbn);
    }

    @Test
    void startLoanForBook_ShouldUpdateStatusAndSave() {
        Loan mockLoan = mock(Loan.class);
        when(bookPort.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

        bookService.startLoanForBook(isbn, mockLoan);

        assertTrue(testBook.isOnLoan());
        assertEquals(mockLoan, testBook.getCurrentLoan());
        verify(bookPort, times(1)).save(testBook);
    }
}
