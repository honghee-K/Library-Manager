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
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.ports.out.repository.BookPort;

@ApplicationScoped
public class JpaBookRepository implements BookPort {

    @Inject
    private JpaConverter jpaConverter;

    @Inject
    private EntityManager entityManager;

    public JpaBookRepository() {}

    @Override
    @Transactional
    public Book save(Book book) {
        BookEntity entity = jpaConverter.toJpaBook(book);
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return jpaConverter.toBook(entity);
    }

    @Override
    @Transactional
    public Optional<Book> getBookByIsbn(Long isbn) {
        return entityManager
                .createQuery("from BookEntity where isbn = :isbn", BookEntity.class)
                .setParameter("isbn", isbn)
                .getResultStream()
                .findFirst()
                .map(jpaConverter::toBook);
    }

    /*    @Override
    @Transactional
    public void deleteByIsbn(Long isbn) {
        getBookByIsbn(isbn).ifPresent(book -> {
            BookEntity entity = entityManager.find(BookEntity.class, book.getId());
            if (entity != null) {
                entityManager.remove(entity);
            }
        });
    }*/

    @Override
    @Transactional
    public List<Book> findAll(int page, int size, String author, String genre) {
        StringBuilder jpql = new StringBuilder("from BookEntity b where 1=1");
        if (author != null && !author.isBlank()) jpql.append(" and b.author = :author");
        if (genre != null && !genre.isBlank()) jpql.append(" and b.genre = :genre");

        jpql.append(" order by b.id");
        TypedQuery<BookEntity> query = entityManager.createQuery(jpql.toString(), BookEntity.class);

        if (author != null && !author.isBlank()) query.setParameter("author", author);
        if (genre != null && !genre.isBlank()) query.setParameter("genre", genre);

        return query.setFirstResult(page * size).setMaxResults(size).getResultList().stream()
                .map(jpaConverter::toBook)
                .collect(Collectors.toList());
    }

    /*    @Override
    @Transactional
    public List<Book> findAllForStatistics() {
        return entityManager.createQuery("SELECT b FROM BookEntity b", BookEntity.class)
                .getResultList()
                .stream()
                .map(jpaConverter::toBook)
                .toList();
    }*/

    /*    @Override
    @Transactional
    public List<Book> findAllForStatistics() {
        return entityManager.createQuery("from BookEntity", BookEntity.class)
                .getResultList()
                .stream()
                .map(jpaConverter::toBook)
                .collect(Collectors.toList());

    }*/
}
