package ru.otus.spring.service;

public interface BookService {
    long showCount();

    String showAll();

    String showById(long id);

    String showByName(String name);

    String showAllByGenreId(long genreId);

    String showAllByGenreName(String genreName);

    String add(String name, String authorName, String[] genreNames);

    String deleteByName(String name);
}
