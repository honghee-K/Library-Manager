package thws.librarymanager.adapters.out.jpa.converter;

import jakarta.enterprise.context.ApplicationScoped;
import thws.librarymanager.adapters.out.jpa.entity.UserEntity;
import thws.librarymanager.application.domain.model.User;

@ApplicationScoped
public class JpaConverter {

    public JpaConverter() {}

    public User toUser(UserEntity userEntity) {

        return new User(userEntity.getId(), userEntity.getName(), userEntity.getEmail());
    }

    public UserEntity toJpaUser(User user) {

        UserEntity result = new UserEntity();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());

        return result;
    }

}
