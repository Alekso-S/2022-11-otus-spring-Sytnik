package ru.otus.spring.converter;

import ru.otus.spring.domain.Author;

public class AuthorConverter {

    public static String toString(Author author) {
        return "id=" + author.getId() +
                ", name='" + author.getName() + '\'';
    }
}
