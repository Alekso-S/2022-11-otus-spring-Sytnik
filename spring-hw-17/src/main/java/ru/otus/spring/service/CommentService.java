package ru.otus.spring.service;

import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;

import java.util.List;

public interface CommentService {
    List<CommentDto> getAllByBookId(String bookId) throws BookNotFoundEx;

    CommentDto addByBookId(String bookId, String text) throws BookNotFoundEx;

    CommentDto updateById(String id, String text) throws CommentNotFoundEx;

    void deleteById(String id) throws CommentNotFoundEx;
}
