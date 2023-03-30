package ru.otus.spring.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.model.mongo.MongoBook;

public interface MongoBookRepository extends MongoRepository<MongoBook, String> {
}
