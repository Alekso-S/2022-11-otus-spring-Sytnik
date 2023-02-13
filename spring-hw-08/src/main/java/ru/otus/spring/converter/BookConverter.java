package ru.otus.spring.converter;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookConverter {

    public static String toString(Book book) {
        return "id=" + book.getId() +
                ", name='" + book.getName() + '\'' +
                ", author='" + book.getAuthor().getName() + '\'' +
                ", genres='" + book.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", ")) + '\'';
    }

    public static String toString(List<Book> books) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getId))
                .map(BookConverter::toString)
                .collect(Collectors.joining("\n"));
    }
}
