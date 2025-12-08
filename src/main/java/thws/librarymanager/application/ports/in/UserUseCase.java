package thws.librarymanager.application.ports.in;

import thws.librarymanager.application.domain.model.User;

public interface UserUseCase {
    User getById(Long userId);
}
