package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    long countByBookId(String bookId);

    long countByBookName(String bookName);

    List<Comment> findAllByBookId(String bookId);

    List<Comment> findAllByBookName(String bookName);

    void deleteByBookName(String bookName);
}
