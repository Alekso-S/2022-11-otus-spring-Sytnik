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
    void shouldAdd() throws CommentNotFoundEx {
        assertThrowsExactly(CommentNotFoundEx.class, () -> commentRepository.getById(COMMENT_9_ID));
        Comment comment = new Comment(getBookById(BOOK_1_ID), COMMENT_9_TEXT);
        commentRepository.add(comment);
        assertEquals(comment, commentRepository.getById(COMMENT_9_ID));
    }

    @DisplayName("обновлять комментарий по идентификатору")
    @DirtiesContext
    @Test
    void shouldUpdById() throws CommentNotFoundEx {
        Comment comment = new Comment(COMMENT_1_ID, COMMENT_1_TEXT_UPD);
        commentRepository.updById(COMMENT_1_ID, COMMENT_1_TEXT_UPD);
        assertEquals(comment, commentRepository.getById(COMMENT_1_ID));
    }

    @DisplayName("удалять комментарий по идентификатору")
    @DirtiesContext
    @Test
    void shouldDelById() throws CommentNotFoundEx {
        assertDoesNotThrow(() -> commentRepository.getById(COMMENT_1_ID));
        commentRepository.delById(COMMENT_1_ID);
        entityManager.clear();
        assertThrowsExactly(CommentNotFoundEx.class, () -> commentRepository.getById(COMMENT_1_ID));
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