package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository repository;

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<GenreDto> getAll() {
        return GenreConverter.toDto(repository.findAll());
    }
}
