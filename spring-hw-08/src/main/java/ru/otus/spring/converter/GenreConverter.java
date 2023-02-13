package ru.otus.spring.converter;

import ru.otus.spring.domain.Genre;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GenreConverter {

    public static String toString(Genre genre) {
        return "name='" + genre.getName() + '\'';
    }

    public static String toString(List<Genre> genres) {
        return genres.stream()
                .sorted(Comparator.comparing(Genre::getName))
                .map(GenreConverter::toString)
                .collect(Collectors.joining("\n"));
    }
}
