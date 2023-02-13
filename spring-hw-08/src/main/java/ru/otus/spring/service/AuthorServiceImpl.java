package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.repository.AuthorRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public long showCount() {
        return authorRepository.count();
    }

    @Override
    public String showAll() {
        return AuthorConverter.toString(authorRepository.findAll());
    }
}
