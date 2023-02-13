package ru.otus.spring.service;

public interface GenreService {
    long showCount();

    String showAll();

    String showById(long id);

    String showByName(String name);

    String showAllByBookId(long bookId);

    String showAllByBookName(String bookName);

    String add(String name);

    String deleteByName(String name);
}
