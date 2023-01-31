package ru.otus.spring.service;

import org.springframework.transaction.annotation.Transactional;

public interface GenreService {
    long showGenresCount();

    String showAllGenres();

    String showGenreById(long id);

    String showGenreByName(String name);

    String showGenresByBookId(long bookId);

    String showGenresByBookName(String bookName);

    @Transactional
    String addGenre(String name);

    @Transactional
    String delGenreByName(String name);
}
