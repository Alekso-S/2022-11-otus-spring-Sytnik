package ru.otus.spring.repository;

import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.AuthorNotFoundEx;

import java.util.List;

public interface AuthorRepository {
    long count();

    List<Author> getAll();

    Author getById(long id) throws AuthorNotFoundEx;

    Author getByName(String name) throws AuthorNotFoundEx;

    Author add(Author author);

    void delete(Author author);

    boolean checkExistenceByName(String name);

    boolean checkRelationsByName(String name);
}
