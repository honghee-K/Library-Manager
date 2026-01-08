package thws.librarymanager.application.ports.out.repository;

import thws.librarymanager.application.domain.models.Librarian;
import java.util.Optional;

public interface LibrarianPort {
    Optional<Librarian> findByName(String name);

    Librarian save(Librarian librarian);
}