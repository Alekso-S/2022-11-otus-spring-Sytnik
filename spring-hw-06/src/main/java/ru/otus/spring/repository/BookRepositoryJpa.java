package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.BookNotFoundEx;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long count() {
        String jpqlQuery = "select count (b) from Book b";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        return query.getSingleResult();
    }

    @Override
    public Book getById(long id) throws BookNotFoundEx {
        return Optional.ofNullable(entityManager.find(Book.class, id))
                .orElseThrow(BookNotFoundEx::new);
    }

    @Override
    public Book getByName(String name) throws BookNotFoundEx {
        String jpqlQuery = "select b from Book b where b.name = :name";
        TypedQuery<Book> query = entityManager.createQuery(jpqlQuery, Book.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new BookNotFoundEx(e);
        }
    }

    @Override
    public List<Book> getAll() {
        String jpqlQuery = "select b from Book b join fetch b.author";
        TypedQuery<Book> query = entityManager.createQuery(jpqlQuery, Book.class);
        return query.getResultList();
    }

    @Override
    public List<Book> getByGenreId(long genreId) {
        String jpqlQuery = "select b from Book b join fetch b.author join b.genres g where g.id = :genre_id";
        TypedQuery<Book> query = entityManager.createQuery(jpqlQuery, Book.class);
        query.setParameter("genre_id", genreId);
        return query.getResultList();
    }

    @Override
    public List<Book> getByGenreName(String genreName) {
        String jpqlQuery = "select b from Book b join fetch b.author join b.genres g where g.name = :genre_name";
        TypedQuery<Book> query = entityManager.createQuery(jpqlQuery, Book.class);
        query.setParameter("genre_name", genreName);
        return query.getResultList();
    }

    @Override
    public Book add(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public void delByName(String name) throws BookNotFoundEx {
        String jpqlQuery = "delete from Book b where b.name = :name";
        Query query = entityManager.createQuery(jpqlQuery);
        query.setParameter("name", name);
        if (query.executeUpdate() == 0) {
            throw new BookNotFoundEx();
        }
    }

    @Override
    public boolean checkExistenceById(long id) {
        String jpqlQuery = "select count (b) from Book b where b.id = :id";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        query.setParameter("id", id);
        return query.getSingleResult() > 0;
    }

    @Override
    public boolean checkExistenceByName(String name) {
        String jpqlQuery = "select count (b) from Book b where b.name = :name";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }
}
