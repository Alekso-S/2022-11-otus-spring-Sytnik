package ru.otus.spring.repository;

import ru.otus.spring.domain.Genre;

import java.util.List;

public interface GenreRepository {
    long count();

    List<Genre> findAll();
}
