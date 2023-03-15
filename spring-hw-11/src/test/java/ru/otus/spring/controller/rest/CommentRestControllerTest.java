package ru.otus.spring.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.config.WebConfig;
import ru.otus.spring.config.endpoint.CommentEndpointConfig;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.rest.CommentRestDto;
import ru.otus.spring.repository.ReactiveBookRepository;
import ru.otus.spring.repository.ReactiveCommentRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static ru.otus.spring.util.DataProducer.*;

@WebFluxTest(CommentEndpointConfig.class)
@Import(WebConfig.class)

@DisplayName("REST контроллер комментариев должен")
public class CommentRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReactiveBookRepository bookRepository;
    @MockBean
    private ReactiveCommentRepository commentRepository;

    private final static String BOOK_1_ID = "1";
    private static final String COMMENT_9_ID = "9";
    private static final String COMMENT_9_TEXT = "Comment 9";
    private static final String COMMENT_1_ID = "1";
    private static final String COMMENT_1_TEXT_UPDATED = "Comment 1 text updated";

    @Test
    @DisplayName("возвращать комментарии по идентификатору книги")
    void shouldGetCommentsByBookId() throws Exception {
        List<Comment> comments = getAllCommentsByBookId(BOOK_1_ID);
        when(bookRepository.existsById(BOOK_1_ID)).thenReturn(Mono.just(true));
        when(commentRepository.findAllByBookId(BOOK_1_ID)).thenReturn(Flux.fromIterable(comments));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/comments")
                        .queryParam("bookId", BOOK_1_ID)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(objectMapper.writeValueAsString(comments.stream()
                        .map(Comment::toDto)
                        .collect(Collectors.toList())));
    }

    @Test
    @DisplayName("добавлять и возвращать комментарий")
    void shouldAddCommentAndReturn() throws Exception {
        CommentRestDto commentRestDto = new CommentRestDto(COMMENT_9_ID, BOOK_1_ID, COMMENT_9_TEXT);
        Comment comment = new Comment(COMMENT_9_ID, getBookById(BOOK_1_ID).orElseThrow(), COMMENT_9_TEXT);
        when(bookRepository.findById(BOOK_1_ID)).thenReturn(Mono.justOrEmpty(getBookById(BOOK_1_ID)));
        when(commentRepository.insert((Comment) any())).thenReturn(Mono.just(comment));

        webTestClient.post()
                .uri("/api/comments")
                .body(BodyInserters.fromValue(commentRestDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(objectMapper.writeValueAsString(comment.toDto()));
    }

    @Test
    @DisplayName("изменять комментарий")
    void shouldUpdateComment() {
        CommentRestDto commentRestDto = new CommentRestDto(COMMENT_1_ID, BOOK_1_ID, COMMENT_1_TEXT_UPDATED);
        Comment comment = new Comment(COMMENT_1_ID, getBookById(BOOK_1_ID).orElseThrow(), COMMENT_1_TEXT_UPDATED);
        when(commentRepository.findById(COMMENT_1_ID)).thenReturn(Mono.justOrEmpty(getCommentById(COMMENT_1_ID)));
        when(commentRepository.save(any())).thenReturn(Mono.just(comment));

        webTestClient.put()
                .uri("/api/comments")
                .body(BodyInserters.fromValue(commentRestDto))
                .exchange()
                .expectStatus().isOk();

        verify(commentRepository, times(1)).findById(COMMENT_1_ID);
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("удалять комментарий")
    void shouldDeleteComment() {
        when(commentRepository.findById(BOOK_1_ID)).thenReturn(Mono.justOrEmpty(getCommentById(COMMENT_1_ID)));
        when(commentRepository.delete(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/comments")
                        .pathSegment(COMMENT_1_ID)
                        .build())
                .exchange()
                .expectStatus().isOk();

        verify(commentRepository, times(1)).findById(COMMENT_1_ID);
        verify(commentRepository, times(1)).delete(any());
    }
}
