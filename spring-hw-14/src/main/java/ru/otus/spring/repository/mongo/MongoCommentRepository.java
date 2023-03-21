package ru.otus.spring.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.model.mongo.MongoComment;

public interface MongoCommentRepository extends MongoRepository<MongoComment, String> {
}
