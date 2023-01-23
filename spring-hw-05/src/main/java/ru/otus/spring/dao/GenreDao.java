package ru.otus.spring.dao;

import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.GenreNotFoundEx;

import java.util.List;

public interface GenreDao {
    long count();

    List<Genre> getAll();

    Genre getById(long id) throws GenreNotFoundEx;

    Genre getByName(String name) throws GenreNotFoundEx;

    List<Genre> getByBookId(long id);

    Genre add(Genre genre);

    void delByName(String name);

    void addGenreForBook(long bookId, long genreId);

    void delGenresForBook(long id);
}
