package ru.otus.spring.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Author;

public interface ReactiveAuthorRepository {
    Mono<Long> count();

    Flux<Author> findAll();
}
