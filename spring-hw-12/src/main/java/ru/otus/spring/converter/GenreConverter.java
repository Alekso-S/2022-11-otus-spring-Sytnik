package ru.otus.spring.converter;

import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.GenreDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GenreConverter {

    public static List<GenreDto> toDto(List<Genre> genres) {
        return genres.stream()
                .map(GenreDto::new)
                .sorted(Comparator.comparing(GenreDto::getName))
                .collect(Collectors.toList());
    }
}
