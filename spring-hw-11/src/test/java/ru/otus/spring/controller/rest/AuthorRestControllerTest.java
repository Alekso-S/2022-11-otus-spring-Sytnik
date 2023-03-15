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
import ru.otus.spring.config.endpoint.AuthorEndpointConfig;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repository.ReactiveAuthorRepository;

import java.util.List;

import static org.mockito.Mockito.when;
import static ru.otus.spring.util.DataProducer.getAllAuthors;

@WebFluxTest(AuthorEndpointConfig.class)
@Import(WebConfig.class)

@DisplayName("REST контроллер авторов должен")
public class AuthorRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReactiveAuthorRepository authorRepository;

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
}
