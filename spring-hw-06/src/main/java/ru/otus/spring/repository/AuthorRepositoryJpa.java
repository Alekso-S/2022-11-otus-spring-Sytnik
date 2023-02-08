package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.AuthorNotFoundEx;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long count() {
        String jpqlQuery = "select count (a) from Author a";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        return query.getSingleResult();
    }

    @Override
    public Author getById(long id) throws AuthorNotFoundEx {
        return Optional.ofNullable(entityManager.find(Author.class, id))
                .orElseThrow(AuthorNotFoundEx::new);
    }

    @Override
    public Author getByName(String name) throws AuthorNotFoundEx {
        String jpqlQuery = "select a from Author a where a.name = :name";
        TypedQuery<Author> query = entityManager.createQuery(jpqlQuery, Author.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new AuthorNotFoundEx(e);
        }
    }

    @Override
    public List<Author> getAll() {
        String jpqlQuery = "select a from Author a";
        TypedQuery<Author> query = entityManager.createQuery(jpqlQuery, Author.class);
        return query.getResultList();
    }

    @Override
    public Author add(Author author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public void delete(Author author) {
        entityManager.remove(author);
    }

    @Override
    public boolean checkRelationsByName(String name) {
        String jpqlQuery = "select count (a) from Book b join b.author a where a.name = :name";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }

    @Override
    public boolean checkExistenceByName(String name) {
        String jpqlQuery = "select count (a) from Author a where a.name = :name";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }
}
