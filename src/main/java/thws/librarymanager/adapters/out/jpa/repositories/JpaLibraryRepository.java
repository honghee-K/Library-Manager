package thws.librarymanager.adapters.out.jpa.repositories;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JpaLibraryRepository {}
        /*implements LibraryPort {

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
                return converter.toDomainLibrary(entity);
            }

            @Override
            @Transactional(Transactional.TxType.SUPPORTS)
            public Optional<Library> getLibraryById(Long id) {
                LibraryEntity entity = em.find(LibraryEntity.class, id);
                return entity != null
                        ? Optional.of(converter.toDomainLibrary(entity))
                        : Optional.empty();
            }

            @Override
            @Transactional(Transactional.TxType.SUPPORTS)
            public Optional<Library> getLibraryByName(String name) {
                TypedQuery<LibraryEntity> query = em.createQuery(
                        "SELECT l FROM LibraryEntity l WHERE l.name = :name", LibraryEntity.class);
                query.setParameter("name", name);
                List<LibraryEntity> result = query.getResultList();
                return result.isEmpty() ? Optional.empty() : Optional.of(converter.toDomainLibrary(result.get(0)));
            }

            @Override
            @Transactional(Transactional.TxType.SUPPORTS)
            public List<Library> findAllLibraries() {
                return em.createQuery("SELECT l FROM LibraryEntity l", LibraryEntity.class)
                        .getResultList()
                        .stream()
                        .map(converter::toDomainLibrary)
                        .collect(Collectors.toList());
            }

            @Override
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
            }
        } */
