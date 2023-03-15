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
import ru.otus.spring.config.endpoint.BookEndpointConfig;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.rest.BookRestDto;
import ru.otus.spring.repository.ReactiveBookRepository;

import java.util.List;

import static org.mockito.Mockito.*;
import static ru.otus.spring.util.DataProducer.*;

@WebFluxTest(BookEndpointConfig.class)
@Import(WebConfig.class)

@DisplayName("REST контроллер книг должен")
public class BookRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReactiveBookRepository bookRepository;

    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String BOOK_5_NAME = "Book 5";
    private final static String BOOK_5_ID = "5";
    private final static String BOOK_1_ID = "1";

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
}
