package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class Book {

    private final long id;
    private final long authorId;
    private final String name;

    private Author author;
    private List<Genre> genres;
}
