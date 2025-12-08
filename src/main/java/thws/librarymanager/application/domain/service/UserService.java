package thws.librarymanager.application.domain.service;

import thws.librarymanager.application.ports.in.UserUseCase;

import thws.librarymanager.application.domain.model.User;
import java.util.HashMap;
import java.util.Map;

public class UserService implements UserUseCase {

    private final Map<Long, User> userStore = new HashMap<>();

    public User getById(Long userId) {
        return userStore.get(userId);
    }

    // (İstersen test için kullanıcı eklemek adına)
    public void save(User user) {
        userStore.put(user.getId(), user);
    }
}
