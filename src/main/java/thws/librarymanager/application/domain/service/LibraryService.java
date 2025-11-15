package thws.librarymanager.application.domain.service;

import thws.librarymanager.application.domain.model.Library;
import thws.librarymanager.application.ports.in.LibraryUseCase;
import thws.librarymanager.application.ports.out.repository.LibraryRepository;

public class LibraryService implements LibraryUseCase {

    private final LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    @Override
    public Library getById(long id) {
        return libraryRepository.getById(id);
    }
}
