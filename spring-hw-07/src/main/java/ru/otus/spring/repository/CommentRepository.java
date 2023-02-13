package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countByBookId(long bookId);

    long countByBookName(String bookName);

    List<Comment> findAllByBookId(long bookId);

    List<Comment> findAllByBookName(String bookName);

    void deleteByBookName(String bookName);
}
