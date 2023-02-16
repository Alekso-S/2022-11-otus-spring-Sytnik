package ru.otus.spring.service;

public interface BookService {
    long showCount();

    String showAll();

    String showById(String id);

    String showByName(String name);

    String showAllByGenreName(String genreName);

    String add(String name, String authorName, String... genreNames);

    String deleteByName(String name);
}
