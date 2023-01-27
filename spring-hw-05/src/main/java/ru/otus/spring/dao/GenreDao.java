package ru.otus.spring.dao;

import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.GenreNotFoundEx;

import java.util.List;

public interface GenreDao {
    long count();

    List<Genre> getAll();

    List<Genre> getAllUsed();

    Genre getById(long id) throws GenreNotFoundEx;

    Genre getByName(String name) throws GenreNotFoundEx;

    List<Genre> getByBookId(long id);

    Genre add(Genre genre);

    void delByName(String name);

    void addGenresForBook(long bookId, List<Genre> genres);

    void delGenresForBook(long id);
}
