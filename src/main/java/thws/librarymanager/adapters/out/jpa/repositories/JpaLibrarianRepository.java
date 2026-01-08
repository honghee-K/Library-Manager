package thws.librarymanager.adapters.out.jpa.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import thws.librarymanager.adapters.out.jpa.converter.JpaConverter;
import thws.librarymanager.adapters.out.jpa.entities.LibrarianEntity;
import thws.librarymanager.application.domain.models.Librarian;
import thws.librarymanager.application.ports.out.repository.LibrarianPort;

import java.util.Optional;

@ApplicationScoped
public class JpaLibrarianRepository implements LibrarianPort {

    @Inject
    EntityManager entityManager;

    @Inject
    JpaConverter jpaConverter;

    @Override
    @Transactional
    public Optional<Librarian> findByName(String name) {
        try {
            LibrarianEntity entity = entityManager
                    .createQuery("SELECT l FROM LibrarianEntity l WHERE l.name = :name", LibrarianEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.ofNullable(jpaConverter.toLibrarian(entity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Librarian save(Librarian librarian) {
        LibrarianEntity entity = jpaConverter.toJpaLibrarian(librarian);
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return jpaConverter.toLibrarian(entity);
    }
}