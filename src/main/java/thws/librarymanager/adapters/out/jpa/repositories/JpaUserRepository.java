package thws.librarymanager.adapters.out.jpa.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import thws.librarymanager.adapters.out.jpa.converter.JpaConverter;
import thws.librarymanager.adapters.out.jpa.entities.UserEntity;
import thws.librarymanager.adapters.out.jpa.enums.LoanStatusJpa;
import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.ports.out.repository.UserPort;

@ApplicationScoped
public class JpaUserRepository implements UserPort {

    @Inject
    private JpaConverter jpaConverter;

    @Inject
    private EntityManager entityManager;

    public JpaUserRepository() {}

    @Override
    @Transactional
    public User save(User user) {
        UserEntity userEntity = jpaConverter.toJpaUser(user);

        if (userEntity.getId() == null) {
            entityManager.persist(userEntity);
            return jpaConverter.toUser(userEntity);
        } else {
            UserEntity merged = entityManager.merge(userEntity);
            return jpaConverter.toUser(merged);
        }
    }

    @Override
    @Transactional
    public Optional<User> findById(Long id) {
        UserEntity entity = entityManager.find(UserEntity.class, id);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(jpaConverter.toUser(entity));
    }

    @Override
    @Transactional
    public List<User> findAll(int page, int size) {
        return entityManager.createQuery("from UserEntity", UserEntity.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList().stream()
                .map(jpaConverter::toUser)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        UserEntity entity = entityManager.find(UserEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
    @Override
    @Transactional
    public boolean hasActiveLoans(Long userId) {
        return entityManager.createQuery(
                        "SELECT COUNT(l) FROM LoanEntity l WHERE l.user.id = :userId AND l.status = :status", Long.class)
                .setParameter("userId", userId)
                .setParameter("status", LoanStatusJpa.ACTIVE)
                .getSingleResult() > 0;
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        Long count = entityManager.createQuery("select COUNT(u) from UserEntity u where u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }
}
