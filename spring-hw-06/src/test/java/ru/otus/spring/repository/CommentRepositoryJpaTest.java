package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.util.DataProducer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"FieldCanBeLocal", "SameParameterValue"})
@DisplayName("Репозиторий для работы с комментариями должен")
@DataJpaTest
@Import(CommentRepositoryJpa.class)
class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepositoryJpa commentRepository;
    @Autowired
    private TestEntityManager entityManager;

    private final long BOOK_1_ID = 1;
    private final String BOOK_1_NAME = "Book 1";
    private final long COMMENT_1_ID = 1;
    private final long COMMENT_9_ID = 9;
    private final String COMMENT_9_TEXT = "Comment 9 text";
    private final String COMMENT_1_TEXT_UPD = "Comment 1 text updated";

    @DisplayName("возвращать число комментариев по идентификатору книги")
    @Test
    void shouldGetCountByBookId() {
        assertEquals(getByBookId(BOOK_1_ID).size(), commentRepository.getCountByBookId(BOOK_1_ID));
    }

    @DisplayName("возвращать число комментариев по имени книги")
    @Test
    void shouldGetCountByBookName() {
        assertEquals(getByBookName(BOOK_1_NAME).size(), commentRepository.getCountByBookName(BOOK_1_NAME));
    }

    @DisplayName("возвращать комментарий по идентификатору")
    @Test
    void shouldGetById() throws CommentNotFoundEx {
        assertEquals(getById(COMMENT_1_ID), commentRepository.getById(COMMENT_1_ID));
    }

    @DisplayName("возвращать комментарии по идентификатору книги")
    @Test
    void shouldGetByBookId() {
        assertIterableEquals(getByBookId(BOOK_1_ID), commentRepository.getByBookId(BOOK_1_ID));
    }

    @DisplayName("возвращать комментарии по имени книги")
    @Test
    void shouldGetByBookName() {
        assertIterableEquals(getByBookName(BOOK_1_NAME), commentRepository.getByBookName(BOOK_1_NAME));
    }

    @DisplayName("добавлять комментарий")
    @DirtiesContext
    @Test
    void shouldAdd() {
        assertNull(entityManager.find(Comment.class, COMMENT_9_ID));
        Comment comment = commentRepository.add(new Comment(getBookById(BOOK_1_ID), COMMENT_9_TEXT));
        entityManager.flush();
        entityManager.clear();
        assertEquals(comment, entityManager.find(Comment.class, COMMENT_9_ID));
    }

    @DisplayName("обновлять комментарий по идентификатору")
    @DirtiesContext
    @Test
    void shouldUpdateComment() {
        Comment comment = entityManager.find(Comment.class, COMMENT_1_ID);
        assertNotEquals(COMMENT_1_TEXT_UPD, comment.getText());
        entityManager.detach(comment);
        comment.setText(COMMENT_1_TEXT_UPD);
        commentRepository.update(comment);
        entityManager.flush();
        entityManager.clear();
        assertEquals(COMMENT_1_TEXT_UPD, entityManager.find(Comment.class, COMMENT_1_ID).getText());
    }

    @DisplayName("удалять комментарий по идентификатору")
    @DirtiesContext
    @Test
    void shouldDeleteComment() {
        Comment comment = entityManager.find(Comment.class, COMMENT_1_ID);
        assertNotNull(comment);
        commentRepository.delete(comment);
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(Comment.class, COMMENT_1_ID));
    }

    private List<Comment> getByBookId(long bookId) {
        return DataProducer.getCommentsByBookId(bookId);
    }

    private List<Comment> getByBookName(String bookName) {
        return DataProducer.getCommentsByBookName(bookName);
    }

    private Comment getById(long id) {
        return DataProducer.getCommentById(id);
    }

    private Book getBookById(long bookId) {
        return DataProducer.getBookById(bookId);
    }
}