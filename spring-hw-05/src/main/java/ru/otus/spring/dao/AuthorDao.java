package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.AuthorNotFoundEx;

import java.util.List;

public interface AuthorDao {
    long count();

    List<Author> getAll();

    Author getById(long id) throws AuthorNotFoundEx;

    Author getByName(String name) throws AuthorNotFoundEx;

    Author add(Author author);

    void delByName(String name);
}
