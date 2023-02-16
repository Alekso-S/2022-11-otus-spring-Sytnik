package ru.otus.spring.converter;

import ru.otus.spring.domain.Author;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorConverter {

    public static String toString(Author author) {
        return "name='" + author.getName() + '\'';
    }

    public static String toString(List<Author> authors) {
        return authors.stream()
                .sorted(Comparator.comparing(Author::getName))
                .map(AuthorConverter::toString)
                .collect(Collectors.joining("\n"));
    }
}
