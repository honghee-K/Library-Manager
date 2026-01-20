package thws.librarymanager.application.ports.out.repository;

import java.util.Optional;

import thws.librarymanager.application.domain.models.Librarian;

public interface LibrarianPort {
    Optional<Librarian> findByName(String name);

    Librarian save(Librarian librarian);
}
