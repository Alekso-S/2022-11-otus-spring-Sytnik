package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.util.DataProducer;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings("SameParameterValue")
@DataJpaTest
@DisplayName("Репозиторий для работы с комментариями должен")
class CommentRepositorySpringDataTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TestEntityManager entityManager;

    private final static long BOOK_1_ID = 1;
    private final static String BOOK_1_NAME = "Book 1";
    private final static String COMMENT_9_TEXT = "Comment 9";

    @Test
    @DisplayName("считать число комментариев для книги по её идентификатору")
    void shouldCountByBookId() {
        assertEquals(getAllByBookId(BOOK_1_ID).size(), commentRepository.countByBookId(BOOK_1_ID));
    }

    @Test
    @DisplayName("считать число комментариев для книги по её имени")
    void shouldCountByBookName() throws BookNotFoundEx {
        assertEquals(getAllByBookName(BOOK_1_NAME).size(), commentRepository.countByBookName(BOOK_1_NAME));
    }

    @Test
    @DisplayName("находить комментарии по идентификатору книги")
    void shouldFindByBookId() {
        assertEquals(getAllByBookId(BOOK_1_ID), commentRepository.findAllByBookId(BOOK_1_ID));
    }

    @Test
    @DisplayName("находить комментарии по имени книги")
    void shouldFindByBookName() throws BookNotFoundEx {
        assertEquals(getAllByBookName(BOOK_1_NAME), commentRepository.findAllByBookName(BOOK_1_NAME));
    }

    @Test
    @DisplayName("удалять комментарии по имени книги")
    void shouldDeleteByBookName() {
        TypedQuery<Long> query = entityManager.getEntityManager()
                .createQuery("select count (c) from Comment c where c.book.name = :book_name", Long.class)
                .setParameter("book_name", BOOK_1_NAME);
        assertNotEquals(0, query.getSingleResult());
        commentRepository.deleteByBookName(BOOK_1_NAME);
        assertEquals(0, query.getSingleResult());
    }

    @Test
    @DirtiesContext
    @DisplayName("должен добавлять новый комментарий")
    void shouldAdd() {
        TypedQuery<Comment> query = entityManager.getEntityManager()
                .createQuery("select c from Comment c where c.id in (" +
                        "select max (c.id) from Comment c where c.book.name = :book_name)", Comment.class)
                .setParameter("book_name", BOOK_1_NAME);
        assertNotEquals(COMMENT_9_TEXT, query.getSingleResult().getText());
        commentRepository.save(new Comment(getBookByName(BOOK_1_NAME).orElseThrow(), COMMENT_9_TEXT));
        assertEquals(COMMENT_9_TEXT, query.getSingleResult().getText());
    }

    private List<Comment> getAllByBookId(long bookId) {
        return DataProducer.getAllCommentsByBookId(bookId);
    }

    private List<Comment> getAllByBookName(String bookName) throws BookNotFoundEx {
        return DataProducer.getAllCommentsByBookName(bookName);
    }

    private Optional<Book> getBookByName(String bookName) {
        return DataProducer.getBookByName(bookName);
    }
}