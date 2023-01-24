package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.AuthorNotFoundEx;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DAO для работы с авторами должен")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    @Autowired
    private AuthorDaoJdbc authorDaoJdbc;

    private final static long AUTHOR_1_ID = 1;
    private final static long AUTHOR_2_ID = 2;
    private final static long AUTHOR_3_ID = 3;
    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String AUTHOR_2_NAME = "Author 2";
    private final static String AUTHOR_3_NAME = "Author 3";

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnRecordsCnt() {
        assertEquals(getAllAuthors().size(), authorDaoJdbc.count());
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        assertIterableEquals(getAllAuthors(), authorDaoJdbc.getAll());
    }

    @DisplayName("возвращать автора по идентификатору")
    @Test
    void shouldReturnAuthorById() throws AuthorNotFoundEx {
        assertEquals(getAllAuthors().get(0), authorDaoJdbc.getById(getAllAuthors().get(0).getId()));
    }

    @DisplayName("возвращать автора по имени")
    @Test
    void shouldReturnAuthorByName() throws AuthorNotFoundEx {
        assertEquals(getAllAuthors().get(0), authorDaoJdbc.getByName(getAllAuthors().get(0).getName()));
    }

    @DisplayName("добавлять автора")
    @DirtiesContext
    @Test
    void shouldCreateAuthor() throws AuthorNotFoundEx {
        assertThrowsExactly(AuthorNotFoundEx.class, () -> authorDaoJdbc.getByName(AUTHOR_3_NAME));
        authorDaoJdbc.add(new Author(0, AUTHOR_3_NAME));
        assertEquals(AUTHOR_3_NAME, authorDaoJdbc.getById(AUTHOR_3_ID).getName());
    }

    @DisplayName("удалять автора")
    @DirtiesContext
    @Test
    void shouldDeleteAuthor() {
        authorDaoJdbc.add(new Author(0, AUTHOR_3_NAME));
        assertDoesNotThrow(() -> authorDaoJdbc.getByName(AUTHOR_3_NAME));
        authorDaoJdbc.delByName(AUTHOR_3_NAME);
        assertThrowsExactly(AuthorNotFoundEx.class, () -> authorDaoJdbc.getByName(AUTHOR_3_NAME));
    }

    private List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(AUTHOR_1_ID, AUTHOR_1_NAME));
        authors.add(new Author(AUTHOR_2_ID, AUTHOR_2_NAME));
        return authors;
    }
}