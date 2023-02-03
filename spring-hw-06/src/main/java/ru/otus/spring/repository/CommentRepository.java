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

    Comment add(Comment comment);

    void updById(long id, String text) throws CommentNotFoundEx;

    void delById(long id) throws CommentNotFoundEx;
}
