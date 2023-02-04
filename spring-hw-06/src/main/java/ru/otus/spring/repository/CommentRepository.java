package ru.otus.spring.repository;

import ru.otus.spring.domain.Comment;
import ru.otus.spring.exception.CommentNotFoundEx;

import java.util.List;

public interface CommentRepository {
    long getCountByBookId(long bookId);

    long getCountByBookName(String bookName);

    Comment getById(long id) throws CommentNotFoundEx;

    List<Comment> getByBookId(long bookId);

    List<Comment> getByBookName(String bookName);

    @SuppressWarnings("UnusedReturnValue")
    Comment add(Comment comment);

    void delete(Comment comment);

    void deleteByBookName(String bookName);

    @SuppressWarnings("UnusedReturnValue")
    Comment update(Comment comment);
}
