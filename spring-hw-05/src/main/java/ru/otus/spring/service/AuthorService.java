package ru.otus.spring.service;

import org.springframework.transaction.annotation.Transactional;

public interface AuthorService {
    long showAuthorsCount();

    String showAllAuthors();

    String showAuthorById(long id);

    String showAuthorByName(String name);

    @Transactional
    String addAuthor(String name);

    @Transactional
    String delAuthorByName(String name);
}
