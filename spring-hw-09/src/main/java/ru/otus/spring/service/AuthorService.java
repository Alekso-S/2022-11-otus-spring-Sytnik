package ru.otus.spring.service;

import ru.otus.spring.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    long count();

    List<AuthorDto> getAll();
}
