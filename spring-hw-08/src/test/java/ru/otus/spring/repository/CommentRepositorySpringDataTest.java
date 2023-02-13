package ru.otus.spring.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.util.DataProducer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings("SameParameterValue")
@DataMongoTest
@EnableMongock
@DisplayName("Репозиторий для работы с комментариями должен")
class CommentRepositorySpringDataTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private final static String BOOK_1_ID = "1";
    private final static String BOOK_1_NAME = "Book 1";
    private final static String COMMENT_9_TEXT = "Comment 9";

    @Test
    @DisplayName("считать число комментариев для книги по её идентификатору")
    void shouldCountByBookId() {
        assertEquals(getAllByBookId(BOOK_1_ID).size(), commentRepository.countByBookId(BOOK_1_ID));
    }

    @Test
    @DisplayName("считать число комментариев для книги по её имени")
    void shouldCountByBookName() {
        assertEquals(getAllByBookName(BOOK_1_NAME).size(), commentRepository.countByBookName(BOOK_1_NAME));
    }

    @Test
    @DisplayName("находить комментарии по идентификатору книги")
    void shouldFindByBookId() {
        assertEquals(getAllByBookId(BOOK_1_ID), commentRepository.findAllByBookId(BOOK_1_ID));
    }

    @Test
    @DisplayName("находить комментарии по имени книги")
    void shouldFindByBookName() {
        assertEquals(getAllByBookName(BOOK_1_NAME), commentRepository.findAllByBookName(BOOK_1_NAME));
    }

    @Test
    @DirtiesContext
    @DisplayName("удалять комментарии по имени книги")
    void shouldDeleteByBookName() {
        assertNotEquals(0, commentRepository.findAllByBookName(BOOK_1_NAME).size());
        commentRepository.deleteByBookName(BOOK_1_NAME);
        assertEquals(0, commentRepository.findAllByBookName(BOOK_1_NAME).size());
    }

    @Test
    @DirtiesContext
    @DisplayName("должен добавлять новый комментарий")
    void shouldAdd() {
        Comment comment = commentRepository.save(new Comment(getBookByName(BOOK_1_NAME).orElseThrow(), COMMENT_9_TEXT));
        assertEquals(comment, mongoTemplate.findById(comment.getId(), Comment.class));
    }

    private List<Comment> getAllByBookId(String bookId) {
        return DataProducer.getAllCommentsByBookId(bookId);
    }

    private List<Comment> getAllByBookName(String bookName) {
        return DataProducer.getAllCommentsByBookName(bookName);
    }

    private Optional<Book> getBookByName(String bookName) {
        return DataProducer.getBookByName(bookName);
    }
}