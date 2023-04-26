package ru.otus.spring.service;

import ru.otus.spring.dto.GenreDto;

import java.util.List;

public interface GenreService {
    long count();

    List<GenreDto> getAll();
}
