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
import ru.otus.spring.config.EndpointsConfig;
import ru.otus.spring.config.WebConfig;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.rest.BookRestDto;
import ru.otus.spring.dto.rest.CommentRestDto;
import ru.otus.spring.repository.ReactiveAuthorRepository;
import ru.otus.spring.repository.ReactiveBookRepository;
import ru.otus.spring.repository.ReactiveCommentRepository;
import ru.otus.spring.repository.ReactiveGenreRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static ru.otus.spring.util.DataProducer.*;

@WebFluxTest(EndpointsConfig.class)
@Import(WebConfig.class)

@DisplayName("REST контроллер должен")
public class RestControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReactiveBookRepository bookRepository;
    @MockBean
    private ReactiveAuthorRepository authorRepository;
    @MockBean
    private ReactiveGenreRepository genreRepository;
    @MockBean
    private ReactiveCommentRepository commentRepository;

    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String BOOK_5_NAME = "Book 5";
    private final static String BOOK_5_ID = "5";
    private final static String BOOK_1_ID = "1";
    private static final String COMMENT_9_ID = "9";
    private static final String COMMENT_9_TEXT = "Comment 9";
    private static final String COMMENT_1_ID = "1";
    private static final String COMMENT_1_TEXT_UPDATED = "Comment 1 text updated";

    @Test
    @DisplayName("возвращать всех авторов")
    void shouldGetAllAuthors() throws Exception {
        List<Author> authors = getAllAuthors();
        when(authorRepository.findAll()).thenReturn(Flux.fromIterable(authors));

        webTestClient.get()
                .uri("/api/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(objectMapper.writeValueAsString(authors));
    }

    @Test
    @DisplayName("возвращать книги по заданному автору")
    void shouldGetBooksByAuthor() throws Exception {
        List<Book> books = getAllBooksByAuthorName(AUTHOR_1_NAME);
        when(bookRepository.findAllByAuthorName(AUTHOR_1_NAME)).thenReturn(Flux.fromIterable(books));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/books")
                        .queryParam("author", AUTHOR_1_NAME)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(objectMapper.writeValueAsString(books));
    }

    @Test
    @DisplayName("возвращать книги по заданному жанру")
    void shouldGetBooksByGenre() throws Exception {
        List<Book> books = getAllBooksByGenreName(GENRE_1_NAME);
        when(bookRepository.findAllByGenresName(GENRE_1_NAME)).thenReturn(Flux.fromIterable(books));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/books")
                        .queryParam("genre", GENRE_1_NAME)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(objectMapper.writeValueAsString(books));
    }

    @Test
    @DisplayName("возвращать все книги")
    void shouldGetAllBooks() throws Exception {
        List<Book> books = getAllBooks();
        when(bookRepository.findAll()).thenReturn(Flux.fromIterable(books));

        webTestClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(objectMapper.writeValueAsString(books));
    }

    @Test
    @DisplayName("добавлять и возвращать книгу")
    void shouldAddBookAndReturn() throws Exception {
        BookRestDto bookRestDto = new BookRestDto(BOOK_5_NAME, AUTHOR_1_NAME, GENRE_1_NAME);
        Book book = new Book(BOOK_5_ID, BOOK_5_NAME, new Author(AUTHOR_1_NAME), List.of(new Genre(GENRE_1_NAME)));
        when(bookRepository.existsByName(BOOK_5_NAME)).thenReturn(Mono.just(false));
        when(bookRepository.insert((Book) any())).thenReturn(Mono.just(book));

        webTestClient.post()
                .uri("/api/books")
                .body(BodyInserters.fromValue(bookRestDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(objectMapper.writeValueAsString(book.toDto()));
    }

    @Test
    @DisplayName("удалять книгу")
    void shouldDeleteBook() {
        when(bookRepository.findById(BOOK_1_ID)).thenReturn(Mono.justOrEmpty(getBookById(BOOK_1_ID)));
        when(bookRepository.deleteWithComments(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/books")
                        .pathSegment(BOOK_1_ID)
                        .build())
                .exchange()
                .expectStatus().isOk();

        verify(bookRepository, times(1)).findById(BOOK_1_ID);
        verify(bookRepository, times(1)).deleteWithComments(any());
    }

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

    @Test
    @DisplayName("возвращать все жанры")
    void shouldGetAllGenres() throws Exception {
        List<Genre> genres = getAllGenres();
        when(genreRepository.findAll()).thenReturn(Flux.fromIterable(genres));

        webTestClient.get()
                .uri("/api/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(objectMapper.writeValueAsString(genres));
    }
}
