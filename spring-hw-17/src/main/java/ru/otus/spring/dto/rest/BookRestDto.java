package ru.otus.spring.dto.rest;

import lombok.Data;

@Data
public class BookRestDto {

    private final String name;
    private final String authorName;
    private final String genreNames;
}
