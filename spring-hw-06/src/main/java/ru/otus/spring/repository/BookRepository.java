package ru.otus.spring.repository;

import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.BookNotFoundEx;

import java.util.List;

public interface BookRepository {
    long count();

    List<Book> getAll();

    Book getById(long id) throws BookNotFoundEx;

    Book getByName(String name) throws BookNotFoundEx;

    List<Book> getByGenreId(long genreId);

    List<Book> getByGenreName(String genreName);

    Book add(Book book);

    void delByName(String name) throws BookNotFoundEx;

    boolean checkExistenceById(long id);

    boolean checkExistenceByName(String name);
}
