package ru.otus.spring.dto;

import lombok.Data;
import ru.otus.spring.domain.Genre;

@Data
public class GenreDto {

    private final String name;

    public GenreDto(Genre genre) {
        this.name = genre.getName();
    }
}
