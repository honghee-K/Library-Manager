package thws.librarymanager.adapters.out.jpa.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import thws.librarymanager.adapters.out.jpa.converter.JpaConverter;
import thws.librarymanager.adapters.out.jpa.entity.UserEntity;
import thws.librarymanager.application.domain.model.User;
import thws.librarymanager.application.ports.out.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaUserRepository implements UserRepository {

    @Inject
    private JpaConverter jpaConverter;

    @Inject
    private EntityManager entityManager;

    public JpaUserRepository() {}

    @Override
    @Transactional
    public User save(User user) {

        UserEntity userEntity = jpaConverter.toJpaUser(user);
        userEntity.setId(null); // TODO

        entityManager.persist(userEntity);

        return jpaConverter.toUser(userEntity);
    }

    @Override
    @Transactional
    public Optional<User> findById(Long id) {
        throw new RuntimeException("not implemented");
    }

    @Override
    @Transactional
    public Optional<User> findByName(String name) {

        Object user = entityManager.createQuery("from UserEntity where name = :name")
                .setParameter("name", name)
                .getSingleResultOrNull();

        if (user instanceof UserEntity userEntity) {
            return Optional.of(jpaConverter.toUser(userEntity));
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        throw new RuntimeException("not implemented");
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return (List<User>) entityManager.createQuery("from UserEntity").getResultList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        throw new RuntimeException("not implemented");
    }

    @Override
    @Transactional
    public boolean hasActiveLoans(Long userId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    @Transactional
    public boolean existsById(Long id) {
        throw new RuntimeException("not implemented");
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        throw new RuntimeException("not implemented");
    }
}
