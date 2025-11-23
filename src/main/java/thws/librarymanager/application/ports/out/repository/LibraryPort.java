package thws.librarymanager.application.ports.out.repository;

import thws.librarymanager.application.domain.model.Library;

public interface LibraryPort {

    Library getById(long id);
}
