package ru.otus.spring.converter;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.stream.Collectors;

public class BookConverter {

    public static String toString(Book book) {
        return "id=" + book.getId() +
                ", name='" + book.getName() + '\'' +
                ", author='" + book.getAuthor().getName() + '\'' +
                ", genres='" + book.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", ")) + '\'';
    }
}
