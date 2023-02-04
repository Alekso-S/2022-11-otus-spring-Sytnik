package ru.otus.spring.repository;

import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.GenreHasRelationsEx;
import ru.otus.spring.exception.GenreNotFoundEx;

import java.util.List;

public interface GenreRepository {
    long count();

    List<Genre> getAll();

    Genre getById(long id) throws GenreNotFoundEx;

    Genre getByName(String name) throws GenreNotFoundEx;

    List<Genre> getByBookId(long bookId);

    List<Genre> getByBookName(String bookName);

    Genre add(Genre genre);

    boolean checkExistenceByName(String name);

    void delByName(String name) throws GenreHasRelationsEx, GenreNotFoundEx;

    boolean checkExistenceById(long id);
}
