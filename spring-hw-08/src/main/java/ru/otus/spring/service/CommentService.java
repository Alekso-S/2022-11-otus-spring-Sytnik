package ru.otus.spring.service;

public interface CommentService {
    String showCountByBookId(String bookId);

    String showCountByBookName(String bookName);

    String showById(String id);

    String showAllByBookId(String bookId);

    String showAllByBookName(String bookName);

    String addByBookName(String bookName, String text);

    String updateById(String id, String text);

    String deleteById(String id);
}
