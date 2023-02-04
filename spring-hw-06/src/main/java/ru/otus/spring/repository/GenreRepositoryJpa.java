package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.GenreNotFoundEx;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long count() {
        String jpqlQuery = "select count (g) from Genre g";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        return query.getSingleResult();
    }

    @Override
    public List<Genre> getAll() {
        String jpqlQuery = "select g from Genre g";
        TypedQuery<Genre> query = entityManager.createQuery(jpqlQuery, Genre.class);
        return query.getResultList();
    }

    @Override
    public Genre getById(long id) throws GenreNotFoundEx {
        return Optional.ofNullable(entityManager.find(Genre.class, id))
                .orElseThrow(GenreNotFoundEx::new);
    }

    @Override
    public Genre getByName(String name) throws GenreNotFoundEx {
        String jpqlQuery = "select g from Genre g where g.name = :name";
        TypedQuery<Genre> query = entityManager.createQuery(jpqlQuery, Genre.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new GenreNotFoundEx(e);
        }
    }

    @Override
    public List<Genre> getByBookId(long bookId) {
        String jpqlQuery = "select g from Book b join b.genres g where b.id = :book_id";
        TypedQuery<Genre> query = entityManager.createQuery(jpqlQuery, Genre.class);
        query.setParameter("book_id", bookId);
        return query.getResultList();
    }

    @Override
    public List<Genre> getByBookName(String bookName) {
        String jpqlQuery = "select g from Book b join b.genres g where b.name = :book_name";
        TypedQuery<Genre> query = entityManager.createQuery(jpqlQuery, Genre.class);
        query.setParameter("book_name", bookName);
        return query.getResultList();
    }

    @Override
    public Genre add(Genre genre) {
        entityManager.persist(genre);
        return genre;
    }

    @Override
    public boolean checkExistenceByName(String name) {
        String jpqlQuery = "select count (g) from Genre g where g.name = :name";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }

    @Override
    public boolean checkRelationsByName(String name) {
        String jpqlQuery = "select count (g) from Book b join b.genres g where g.name = :name";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }

    @Override
    public void delete(Genre genre) {
        entityManager.remove(genre);
    }

    @Override
    public boolean checkExistenceById(long id) {
        String jpqlQuery = "select count (g) from Genre g where g.id = :id";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        query.setParameter("id", id);
        return query.getSingleResult() > 0;
    }
}
