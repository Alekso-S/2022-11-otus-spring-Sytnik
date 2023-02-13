package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryImpl implements AuthorRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public long count() {
        Aggregation aggregation = newAggregation (
                replaceRoot("author"),
                group("name").first("name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Author.class).getMappedResults().size();
    }

    @Override
    public List<Author> findAll() {
        Aggregation aggregation = newAggregation (
                replaceRoot("author"),
                group("name").first("name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Author.class).getMappedResults();
    }
}
