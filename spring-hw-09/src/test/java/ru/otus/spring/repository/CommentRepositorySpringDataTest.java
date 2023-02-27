package ru.otus.spring.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.otus.spring.util.DataProducer.getAllCommentsByBookName;
import static ru.otus.spring.util.DataProducer.getBookByName;

@SuppressWarnings("SameParameterValue")
@DataMongoTest
@EnableMongock
@DisplayName("Репозиторий для работы с комментариями должен")
class CommentRepositorySpringDataTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private final static String BOOK_1_NAME = "Book 1";
    private final static String COMMENT_9_TEXT = "Comment 9";

    @Test
    @DisplayName("находить комментарии по имени книги")
    void shouldFindByBookName() {
        assertEquals(getAllCommentsByBookName(BOOK_1_NAME), commentRepository.findAllByBookName(BOOK_1_NAME));
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
}