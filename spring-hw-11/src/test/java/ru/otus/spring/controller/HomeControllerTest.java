package ru.otus.spring.controller;

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

@WebFluxTest(HomeController.class)
@Import(WebConfig.class)
@DisplayName("Базовый контроллер должен")
class HomeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveBookRepository bookRepository;
    @MockBean
    private ReactiveAuthorRepository authorRepository;
    @MockBean
    private ReactiveGenreRepository genreRepository;

    @Test
    @DisplayName("возвращать домашнюю страницу")
    void shouldReturnHomePage() {
        when(bookRepository.count()).thenReturn(Mono.just(getBooksCount()));
        when(authorRepository.count()).thenReturn(Mono.just(getAuthorsCount()));
        when(genreRepository.count()).thenReturn(Mono.just(getGenresCount()));

        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).value(s -> {
                    assertThat(s).contains("Select category");
                    assertThat(s).contains(String.format("Books (<span>%d</span>)", getBooksCount()));
                    assertThat(s).contains(String.format("Authors (<span>%d</span>)", getAuthorsCount()));
                    assertThat(s).contains(String.format("Genres (<span>%d</span>)", getGenresCount()));
                });
    }
}