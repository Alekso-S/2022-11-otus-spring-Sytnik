package ru.otus.spring.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    @HystrixCommand(defaultFallback = "countFallback")
    public long count() {
        return genreRepository.count();
    }

    private long countFallback() {
        return -1;
    }

    @Override
    @HystrixCommand(defaultFallback = "getListFallback")
    public List<GenreDto> getAll() {
        return GenreConverter.toDto(genreRepository.findAll());
    }

    private List<GenreDto> getListFallback() {
        return List.of(new Genre("Error").toDto());
    }
}
