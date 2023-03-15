package ru.otus.spring.config.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.repository.ReactiveGenreRepository;

import java.util.Comparator;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class GenreEndpointConfig {

    private final ReactiveGenreRepository genreRepository;

    @Bean
    public RouterFunction<ServerResponse> genreRouterFunction() {
        return route()
                .GET("/api/genres", this::getAllGenres)
                .build();
    }

    private Mono<ServerResponse> getAllGenres(ServerRequest request) {
        Flux<GenreDto> genres = genreRepository.findAll()
                .map(Genre::toDto)
                .sort(Comparator.comparing(GenreDto::getName));
        return ok().body(genres, GenreDto.class);
    }
}

