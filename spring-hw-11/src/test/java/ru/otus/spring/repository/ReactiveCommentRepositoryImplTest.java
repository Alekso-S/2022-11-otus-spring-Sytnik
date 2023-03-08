package ru.otus.spring.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;
import ru.otus.spring.domain.Comment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.otus.spring.util.DataProducer.getBookByName;

@DataMongoTest
@EnableMongock
@DisplayName("Репозиторий для работы с комментариями должен")
class ReactiveCommentRepositoryImplTest {

    @Autowired
    private ReactiveCommentRepository commentRepository;
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private final static String BOOK_1_NAME = "Book 1";
    private final static String COMMENT_9_TEXT = "Comment 9";

    @Test
    @DirtiesContext
    @DisplayName("должен добавлять новый комментарий")
    void shouldAdd() {
        List<Comment> comments = new ArrayList<>();
        StepVerifier.create(commentRepository.insert(
                        new Comment(getBookByName(BOOK_1_NAME).orElseThrow(), COMMENT_9_TEXT)))
                .recordWith(() -> comments)
                .assertNext(comment -> assertNotNull(comment.getId()))
                .verifyComplete();
        StepVerifier.create(mongoTemplate.findById(comments.get(0).getId(), Comment.class))
                .assertNext(comment -> assertEquals(comments.get(0), comment))
                .verifyComplete();
    }
}