package ru.otus.spring.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.spring.config.WebConfig;
import ru.otus.spring.config.endpoint.GenreEndpointConfig;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.ReactiveGenreRepository;

import java.util.List;

import static org.mockito.Mockito.when;
import static ru.otus.spring.util.DataProducer.getAllGenres;

@WebFluxTest(GenreEndpointConfig.class)
@Import(WebConfig.class)

@DisplayName("REST контроллер жанров должен")
public class GenreRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReactiveGenreRepository genreRepository;

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
