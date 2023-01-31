package ru.otus.spring.converter;

import ru.otus.spring.domain.Genre;

public class GenreConverter {

    public static String toString(Genre genre) {
        return "id=" + genre.getId() +
                ", name='" + genre.getName() + '\'';
    }
}
