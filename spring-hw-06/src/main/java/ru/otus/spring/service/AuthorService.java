package ru.otus.spring.service;

public interface AuthorService {
    long showAuthorsCount();

    String showAllAuthors();

    String showAuthorById(long id);

    String showAuthorByName(String name);

    String addAuthor(String name);

    String deleteAuthorByName(String name);
}
