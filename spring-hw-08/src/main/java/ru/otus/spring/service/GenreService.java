package ru.otus.spring.service;

public interface GenreService {
    long showCount();

    String showAll();

    String showAllByBookId(String bookId);

    String showAllByBookName(String bookName);
}
