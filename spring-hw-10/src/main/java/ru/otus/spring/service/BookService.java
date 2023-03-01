package ru.otus.spring.service;

import ru.otus.spring.dto.BookDto;
import ru.otus.spring.exception.BookAlreadyExistsEx;
import ru.otus.spring.exception.BookNotFoundEx;

import java.util.List;

public interface BookService {
    long count();

    List<BookDto> getAll();

    List<BookDto> getAllByGenreName(String genreName);

    List<BookDto> getAllByAuthorName(String authorName);

    BookDto getById(String bookId) throws BookNotFoundEx;

    BookDto add(String name, String authorName, String genreNames) throws BookAlreadyExistsEx;

    void deleteById(String id) throws BookNotFoundEx;
}
