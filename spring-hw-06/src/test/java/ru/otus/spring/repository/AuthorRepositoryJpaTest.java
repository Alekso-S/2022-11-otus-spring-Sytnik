package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.AuthorHasRelationsEx;
import ru.otus.spring.exception.AuthorNotFoundEx;
import ru.otus.spring.util.DataProducer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий для работы с авторами должен")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepositoryJpa authorRepository;

    private final static long AUTHOR_3_ID = 3;
    private final static String AUTHOR_3_NAME = "Author 3";

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnRecordsCnt() {
        assertEquals(getAll().size(), authorRepository.count());
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        assertIterableEquals(getAll(), authorRepository.getAll());
    }

    @DisplayName("возвращать автора по идентификатору")
    @Test
    void shouldReturnAuthorById() throws AuthorNotFoundEx {
        assertEquals(getAll().get(0), authorRepository.getById(getAll().get(0).getId()));
    }

    @DisplayName("возвращать автора по имени")
    @Test
    void shouldReturnAuthorByName() throws AuthorNotFoundEx {
        assertEquals(getAll().get(0), authorRepository.getByName(getAll().get(0).getName()));
    }

    @DisplayName("добавлять автора")
    @DirtiesContext
    @Test
    void shouldCreateAuthor() throws AuthorNotFoundEx {
        assertThrowsExactly(AuthorNotFoundEx.class, () -> authorRepository.getByName(AUTHOR_3_NAME));
        authorRepository.add(new Author(AUTHOR_3_NAME));
        assertEquals(AUTHOR_3_NAME, authorRepository.getById(AUTHOR_3_ID).getName());
    }

    @DisplayName("удалять автора")
    @DirtiesContext
    @Test
    void shouldDeleteAuthor() throws AuthorHasRelationsEx, AuthorNotFoundEx {
        authorRepository.add(new Author(AUTHOR_3_NAME));
        assertDoesNotThrow(() -> authorRepository.getByName(AUTHOR_3_NAME));
        authorRepository.delByName(AUTHOR_3_NAME);
        assertThrowsExactly(AuthorNotFoundEx.class, () -> authorRepository.getByName(AUTHOR_3_NAME));
    }

    private List<Author> getAll() {
        return DataProducer.getAllAuthors();
    }
}