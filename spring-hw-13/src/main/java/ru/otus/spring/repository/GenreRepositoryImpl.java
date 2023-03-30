package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryImpl implements GenreRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public long count() {
        Aggregation aggregation = newAggregation (
                unwind("genres"),
                replaceRoot("genres"),
                group("name").first("name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Genre.class).getMappedResults().size();
    }

    @Override
    public List<Genre> findAll() {
        Aggregation aggregation = newAggregation (
                unwind("genres"),
                replaceRoot("genres"),
                group("name").first("name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Genre.class).getMappedResults();
    }
}
