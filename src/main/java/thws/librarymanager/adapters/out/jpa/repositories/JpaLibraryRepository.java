package thws.librarymanager.adapters.out.jpa.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import thws.librarymanager.adapters.out.jpa.converter.JpaConverter;
import thws.librarymanager.adapters.out.jpa.entities.BookEntity;
import thws.librarymanager.adapters.out.jpa.entities.LibraryEntity;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Library;
import thws.librarymanager.application.ports.out.repository.LibraryPort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ApplicationScoped
public class JpaLibraryRepository implements LibraryPort {

    @Inject
    EntityManager em;

    @Inject
    JpaConverter converter; // User/Book dönüştürme işlemleri için



   @Override
    @Transactional
    public Library save(Library library) {
        LibraryEntity entity = converter.toJpaLibrary(library);
        if (library.getId() == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }
        return converter.toLibrary(entity);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Library> getLibraryById(Long id) {
        LibraryEntity entity = em.find(LibraryEntity.class, id);
        return entity != null
                ? Optional.of(converter.toLibrary(entity))
                : Optional.empty();
    }

   /* @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Library> getLibraryByName(String name) {
        TypedQuery<LibraryEntity> query = em.createQuery(
                "SELECT l FROM LibraryEntity l WHERE l.name = :name", LibraryEntity.class);
        query.setParameter("name", name);
        List<LibraryEntity> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(converter.toDomainLibrary(result.get(0)));
    }*/

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)

    public List<Library> findAllLibraries(String location, String name) {

        String jpql = "SELECT l FROM LibraryEntity l WHERE 1=1";

        if (location != null) {
            jpql += " AND l.location = :location";
        }

        if (name != null) {
            jpql += " AND l.name LIKE :name";
        }

        TypedQuery<LibraryEntity> query =
                em.createQuery(jpql, LibraryEntity.class);

        if (location != null) {
            query.setParameter("location", location);
        }

        if (name != null) {
            query.setParameter("name", "%" + name + "%");
        }

        return query.getResultList()

                .stream()
                .map(converter::toLibrary)
                .collect(Collectors.toList());
    }

   /* @Override
    @Transactional
    public void deleteLibraryById(Long id) {
        LibraryEntity entity = em.find(LibraryEntity.class, id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Long countTotalBooks(Long libraryId) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(b) FROM BookEntity b WHERE b.library.id = :libraryId", Long.class);
        query.setParameter("libraryId", libraryId);
        return query.getSingleResult();

    }



    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Book> findBooksInLibrary(Long libraryId) {
        TypedQuery<BookEntity> query = em.createQuery(
                "SELECT b FROM BookEntity b WHERE b.library.id = :libraryId", BookEntity.class);
        query.setParameter("libraryId", libraryId);
        return query.getResultList()
                .stream()
                .map(converter::toBook)
                .collect(Collectors.toList());
    }*/
}

