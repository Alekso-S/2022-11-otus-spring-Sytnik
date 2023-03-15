package ru.otus.spring.config.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.repository.ReactiveAuthorRepository;

import java.util.Comparator;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class AuthorEndpointConfig {

    private final ReactiveAuthorRepository authorRepository;

    @Bean
    public RouterFunction<ServerResponse> authorRouterFunction() {
        return route()
                .GET("/api/authors", this::getAllAuthors)
                .build();
    }

    public Mono<ServerResponse> getAllAuthors(ServerRequest request) {
        Flux<AuthorDto> authors = authorRepository.findAll()
                .map(Author::toDto)
                .sort(Comparator.comparing(AuthorDto::getName));
        return ok().body(authors, AuthorDto.class);
    }
}

