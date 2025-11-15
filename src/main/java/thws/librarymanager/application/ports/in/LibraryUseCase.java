package thws.librarymanager.application.ports.in;

import thws.librarymanager.application.domain.model.Library;

public interface LibraryUseCase {

    Library getById(long id);
}
