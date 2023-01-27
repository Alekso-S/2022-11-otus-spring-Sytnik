package ru.otus.spring.dao;

import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.BookNotFoundEx;

import java.util.List;

public interface BookDao {
    long count();

    List<Book> getAll();

    Book getById(long id) throws BookNotFoundEx;

    Book getByName(String name) throws BookNotFoundEx;

    List<Book> getByGenreId(long id);

    List<Book> getByAuthorId(long authorId);

    Book add(Book book);

    void delByName(String name) throws BookNotFoundEx;
}
