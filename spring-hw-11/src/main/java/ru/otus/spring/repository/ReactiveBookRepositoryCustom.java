package ru.otus.spring.repository;

import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;

public interface ReactiveBookRepositoryCustom {
    Mono<Void> deleteWithComments(Book book);
}
