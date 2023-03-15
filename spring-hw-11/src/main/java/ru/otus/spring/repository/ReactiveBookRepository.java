package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;

public interface ReactiveBookRepository extends ReactiveMongoRepository<Book, String>, ReactiveBookRepositoryCustom {
    Mono<Boolean> existsByName(String name);

    Flux<Book> findAllByGenresName(String genreName);

    Flux<Book> findAllByAuthorName(String authorName);
}
