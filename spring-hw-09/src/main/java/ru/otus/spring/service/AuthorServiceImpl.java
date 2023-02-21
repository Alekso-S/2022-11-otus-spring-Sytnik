package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.repository.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<AuthorDto> getAll() {
        return AuthorConverter.toDto(repository.findAll());
    }
}
