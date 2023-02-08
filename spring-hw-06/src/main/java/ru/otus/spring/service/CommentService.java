package ru.otus.spring.service;

public interface CommentService {
    String showCountByBookId(long bookId);

    String showCountByBookName(String bookName);

    String showById(long id);

    String showByBookId(long bookId);

    String showByBookName(String bookName);

    String addByBookName(String bookName, String text);

    String updateById(long id, String text);

    String deleteById(long id);
}
