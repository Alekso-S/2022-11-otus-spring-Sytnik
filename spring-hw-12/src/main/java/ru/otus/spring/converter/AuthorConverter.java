package ru.otus.spring.converter;

import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.AuthorDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorConverter {

    public static List<AuthorDto> toDto(List<Author> authors) {
        return authors.stream()
                .map(AuthorDto::new)
                .sorted(Comparator.comparing(AuthorDto::getName))
                .collect(Collectors.toList());
    }
}
