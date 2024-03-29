package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.exception.CommentNotFoundEx;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long getCountByBookId(long bookId) {
        String jpqlQuery = "select count (c) from Book b join b.comments c where b.id = :book_id";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        query.setParameter("book_id", bookId);
        return query.getSingleResult();
    }

    @Override
    public long getCountByBookName(String bookName) {
        String jpqlQuery = "select count (c) from Book b join b.comments c where b.name = :book_name";
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        query.setParameter("book_name", bookName);
        return query.getSingleResult();
    }

    @Override
    public Comment getById(long id) throws CommentNotFoundEx {
        return Optional.ofNullable(entityManager.find(Comment.class, id)).orElseThrow(CommentNotFoundEx::new);
    }

    @Override
    public List<Comment> getByBookId(long bookId) {
        String jpqlQuery = "select c from Book b join b.comments c where b.id = :book_id";
        TypedQuery<Comment> query = entityManager.createQuery(jpqlQuery, Comment.class);
        query.setParameter("book_id", bookId);
        return query.getResultList();
    }

    @Override
    public List<Comment> getByBookName(String bookName) {
        String jpqlQuery = "select c from Book b join b.comments c where b.name = :book_name";
        TypedQuery<Comment> query = entityManager.createQuery(jpqlQuery, Comment.class);
        query.setParameter("book_name", bookName);
        return query.getResultList();
    }

    @Override
    public Comment add(Comment comment) {
        entityManager.persist(comment);
        return comment;
    }

    @Override
    public void delete(Comment comment) {
        entityManager.remove(comment);
    }

    @Override
    public void deleteByBookName(String bookName) {
        String jpqlQuery = "delete from Comment c where c in " +
                "(select c from Book b join b.comments c where b.name = :book_name)";
        Query query = entityManager.createQuery(jpqlQuery);
        query.setParameter("book_name", bookName);
        query.executeUpdate();
    }

    @Override
    public Comment update(Comment comment) {
        return entityManager.merge(comment);
    }
}
