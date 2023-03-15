package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class ReactiveAuthorRepositoryImpl implements ReactiveAuthorRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Long> count() {
        Aggregation aggregation = newAggregation (
                replaceRoot("author"),
                group("name").first("name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Author.class).count();
    }

    @Override
    public Flux<Author> findAll() {
        Aggregation aggregation = newAggregation (
                replaceRoot("author"),
                group("name").first("name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Author.class);
    }
}
