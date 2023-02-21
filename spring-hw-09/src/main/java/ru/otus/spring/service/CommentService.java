package ru.otus.spring.service;

import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;

import java.util.List;

public interface CommentService {
    CommentDto getById(String id) throws CommentNotFoundEx;

    List<CommentDto> getAllByBookName(String bookName) throws BookNotFoundEx;

    CommentDto addByBookName(String bookName, String text) throws BookNotFoundEx;

    CommentDto updateById(String id, String text) throws CommentNotFoundEx;

    CommentDto deleteById(String id) throws CommentNotFoundEx;
}
