package ru.otus.spring.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Genre;

public interface ReactiveGenreRepository {
    Mono<Long> count();

    Flux<Genre> findAll();
}
