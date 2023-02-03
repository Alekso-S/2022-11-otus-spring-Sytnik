package ru.otus.spring.service;

public interface GenreService {
    long showGenresCount();

    String showAllGenres();

    String showGenreById(long id);

    String showGenreByName(String name);

    String showGenresByBookId(long bookId);

    String showGenresByBookName(String bookName);

    String addGenre(String name);

    String delGenreByName(String name);
}
