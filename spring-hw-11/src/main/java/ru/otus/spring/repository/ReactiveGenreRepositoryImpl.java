package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class ReactiveGenreRepositoryImpl implements ReactiveGenreRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Long> count() {
        Aggregation aggregation = newAggregation(
                unwind("genres"),
                replaceRoot("genres"),
                group("name").first("name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Genre.class).count();
    }

    @Override
    public Flux<Genre> findAll() {
        Aggregation aggregation = newAggregation(
                unwind("genres"),
                replaceRoot("genres"),
                group("name").first("name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Genre.class);
    }
}
