package ru.otus.spring.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.repository.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @HystrixCommand(defaultFallback = "countFallback")
    public long count() {
        return authorRepository.count();
    }

    private long countFallback() {
        return -1;
    }

    @Override
    @HystrixCommand(defaultFallback = "getListFallback")
    public List<AuthorDto> getAll() {
        return AuthorConverter.toDto(authorRepository.findAll());
    }

    private List<AuthorDto> getListFallback() {
        return List.of(new Author("Error").toDto());
    }
}
