package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.otus.spring.config.WebConfig;
import ru.otus.spring.repository.ReactiveAuthorRepository;
import ru.otus.spring.repository.ReactiveBookRepository;
import ru.otus.spring.repository.ReactiveGenreRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ru.otus.spring.util.DataProducer.*;

@WebFluxTest(BookController.class)
@Import(WebConfig.class)
@DisplayName("Контроллер книг должен")
class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveBookRepository bookRepository;
    @MockBean
    private ReactiveAuthorRepository authorRepository;
    @MockBean
    private ReactiveGenreRepository genreRepository;

    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String GENRE_1_NAME = "Genre 1";

    @BeforeEach
    void setUp() {
        when(bookRepository.count()).thenReturn(Mono.just(getBooksCount()));
        when(authorRepository.count()).thenReturn(Mono.just(getAuthorsCount()));
        when(genreRepository.count()).thenReturn(Mono.just(getGenresCount()));
    }

    @Test
    @DisplayName("возвращать страницу со всеми книгами")
    void shouldReturnBooksPage() {
        webTestClient.get()
                .uri("/books")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).value(s -> {
                    assertThat(s).contains("All books");
                    assertThat(s).contains(String.format("Books (<span>%d</span>)", getBooksCount()));
                    assertThat(s).contains(String.format("Authors (<span>%d</span>)", getAuthorsCount()));
                    assertThat(s).contains(String.format("Genres (<span>%d</span>)", getGenresCount()));
                });
    }

    @Test
    @DisplayName("возвращать страницу с книгами выбранного автора")
    void shouldReturnBooksPageByAuthor() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books")
                        .queryParam("author", AUTHOR_1_NAME)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).value(s -> {
                    assertThat(s).contains(String.format("Books by <span>%s</span>", AUTHOR_1_NAME));
                    assertThat(s).contains(String.format("Books (<span>%d</span>)", getBooksCount()));
                    assertThat(s).contains(String.format("Authors (<span>%d</span>)", getAuthorsCount()));
                    assertThat(s).contains(String.format("Genres (<span>%d</span>)", getGenresCount()));
                });
    }

    @Test
    @DisplayName("возвращать страницу с книгами выбранного жанра")
    void shouldReturnBooksPageByGenre() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books")
                        .queryParam("genre", GENRE_1_NAME)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).value(s -> {
                    assertThat(s).contains(String.format("Books of <span>%s</span>", GENRE_1_NAME));
                    assertThat(s).contains(String.format("Books (<span>%d</span>)", getBooksCount()));
                    assertThat(s).contains(String.format("Authors (<span>%d</span>)", getAuthorsCount()));
                    assertThat(s).contains(String.format("Genres (<span>%d</span>)", getGenresCount()));
                });
    }
}