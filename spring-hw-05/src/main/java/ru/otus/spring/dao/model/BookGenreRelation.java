package ru.otus.spring.dao.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class BookGenreRelation {

    private final long bookId;
    private final long genreId;
}
