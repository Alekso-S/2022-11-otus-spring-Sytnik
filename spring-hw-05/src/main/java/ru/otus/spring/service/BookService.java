package ru.otus.spring.service;

import org.springframework.transaction.annotation.Transactional;

public interface BookService {
    long showBooksCount();

    String showAllBooks();

    String showBookById(long id);

    String showBookByName(String name);

    String showBooksByGenreId(Long genreId);

    String showBooksByGenreName(String genreName);

    @Transactional
    String addBook(String name, String authorName, String[] genreNames);

    @Transactional
    String delBookByName(String name);
}
