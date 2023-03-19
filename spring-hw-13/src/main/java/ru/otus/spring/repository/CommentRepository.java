package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findAllByBookId(String bookId);

    void deleteByBookId(String bookName);

    @PreAuthorize("#comment.id == null || hasPermission(#comment,'WRITE') || hasRole('ADMIN')")
    Comment save(Comment comment);

    @PreAuthorize("hasPermission(#comment,'DELETE') || hasRole('ADMIN')")
    void delete(Comment comment);
}
