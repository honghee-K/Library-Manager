package thws.librarymanager.application.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.out.repository.LibraryPort;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    LibraryPort libraryPort;

    @Mock
    BookUseCase bookUseCase;

    @InjectMocks
    LibraryService libraryService;


    @Test
    void getLibraryById_success() {
        Library library = mock(Library.class);
        when(libraryPort.getLibraryById(1L)).thenReturn(Optional.of(library));

        Optional<Library> result = libraryService.getLibraryById(1L);

        assertTrue(result.isPresent());
        assertEquals(library, result.get());
        verify(libraryPort).getLibraryById(1L);
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }

    @Test
    void getLibraryById_notFound() {
        when(libraryPort.getLibraryById(99L)).thenReturn(Optional.empty());

        Optional<Library> result = libraryService.getLibraryById(99L);

        assertTrue(result.isEmpty());
        verify(libraryPort).getLibraryById(99L);
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }


    @Test
    void getAllLibraries_success() {
        when(libraryPort.findAllLibraries(null, null)).thenReturn(List.of(mock(Library.class)));

        List<Library> result = libraryService.getAllLibraries(null, null);

        assertEquals(1, result.size());
        verify(libraryPort).findAllLibraries(null, null);
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }

    @Test
    void getAllLibraries_withFilters_success() {
        when(libraryPort.findAllLibraries("Wuerzburg", "Central"))
                .thenReturn(List.of(mock(Library.class), mock(Library.class)));

        List<Library> result = libraryService.getAllLibraries("Wuerzburg", "Central");

        assertEquals(2, result.size());
        verify(libraryPort).findAllLibraries("Wuerzburg", "Central");
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }

    // -------- addLibrary --------

    @Test
    void addLibrary_success() {
        Library library = mock(Library.class);
        when(library.getName()).thenReturn("Central Library");
        when(libraryPort.findByName("Central Library")).thenReturn(Optional.empty());
        when(libraryPort.save(library)).thenReturn(library);

        Library result = libraryService.addLibrary(library);

        assertEquals(library, result);
        verify(libraryPort).findByName("Central Library");
        verify(libraryPort).save(library);
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }

    @Test
    void addLibrary_duplicateName_throws() {
        Library library = mock(Library.class);
        when(library.getName()).thenReturn("Central Library");
        when(libraryPort.findByName("Central Library")).thenReturn(Optional.of(mock(Library.class)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> libraryService.addLibrary(library));

        assertTrue(ex.getMessage().contains("already exists"));
        verify(libraryPort).findByName("Central Library");
        verify(libraryPort, never()).save(any());
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }


    @Test
    void updateLibrary_success() {
        Library existing = mock(Library.class);
        Library updated = mock(Library.class);

        when(libraryPort.getLibraryById(1L)).thenReturn(Optional.of(existing));
        when(existing.updateLibrary("New Name", "New Location")).thenReturn(updated);

        libraryService.updateLibrary(1L, "New Name", "New Location");

        verify(libraryPort).getLibraryById(1L);
        verify(existing).updateLibrary("New Name", "New Location");
        verify(libraryPort).save(updated);
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }

    @Test
    void updateLibrary_notFound_throws() {
        when(libraryPort.getLibraryById(404L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> libraryService.updateLibrary(404L, "X", "Y"));

        assertTrue(ex.getMessage().contains("Library not found"));
        verify(libraryPort).getLibraryById(404L);
        verify(libraryPort, never()).save(any());
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }


    @Test
    void deleteLibrary_success() {
        when(libraryPort.countTotalBooks(1L)).thenReturn(0L);

        libraryService.deleteLibrary(1L);

        verify(libraryPort).countTotalBooks(1L);
        verify(libraryPort).deleteLibraryById(1L);
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }

    @Test
    void deleteLibrary_hasBooks_throws() {
        when(libraryPort.countTotalBooks(1L)).thenReturn(3L);

        assertThrows(IllegalStateException.class, () -> libraryService.deleteLibrary(1L));

        verify(libraryPort).countTotalBooks(1L);
        verify(libraryPort, never()).deleteLibraryById(anyLong());
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }


    @Test
    void addBookToLibrary_success() {
        Library library = mock(Library.class);
        when(libraryPort.getLibraryById(1L)).thenReturn(Optional.of(library));

        libraryService.addBookToLibrary(1L, 123L);

        verify(libraryPort).getLibraryById(1L);
        verify(bookUseCase).addBookToLibrary(123L, library);
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }

    @Test
    void addBookToLibrary_libraryNotFound_throws() {
        when(libraryPort.getLibraryById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> libraryService.addBookToLibrary(1L, 123L));

        verify(libraryPort).getLibraryById(1L);
        verifyNoInteractions(bookUseCase);
        verifyNoMoreInteractions(libraryPort);
    }

    // -------- removeBookFromLibrary --------
    // NOTE: Your implementation ignores libraryId and removes by calling addBookToLibrary(isbn, null).
    @Test
    void removeBookFromLibrary_callsBookUseCaseWithNullLibrary() {
        libraryService.removeBookFromLibrary(1L, 123L);

        verify(bookUseCase).addBookToLibrary(123L, null);
        verifyNoInteractions(libraryPort);
        verifyNoMoreInteractions(bookUseCase);
    }


    @Test
    void getTotalBookCount_success() {
        when(libraryPort.countTotalBooks(1L)).thenReturn(42L);

        Long count = libraryService.getTotalBookCount(1L);

        assertEquals(42L, count);
        verify(libraryPort).countTotalBooks(1L);
        verifyNoMoreInteractions(libraryPort, bookUseCase);
    }
}

