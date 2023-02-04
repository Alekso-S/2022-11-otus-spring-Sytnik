package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
    @Autowired
    private TestEntityManager entityManager;

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
    void shouldCreateAuthor() {
        assertNull(entityManager.find(Author.class, AUTHOR_3_ID));
        Author author = authorRepository.add(new Author(AUTHOR_3_NAME));
        entityManager.flush();
        entityManager.clear();
        assertEquals(author, entityManager.find(Author.class, AUTHOR_3_ID));
    }

    @DisplayName("удалять автора")
    @DirtiesContext
    @Test
    void shouldDeleteAuthor() throws AuthorHasRelationsEx, AuthorNotFoundEx {
        Author author = new Author(AUTHOR_3_NAME);
        entityManager.persist(author);
        entityManager.flush();
        entityManager.clear();
        assertNotNull(entityManager.find(Author.class, AUTHOR_3_ID));
        authorRepository.delByName(AUTHOR_3_NAME);
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(Author.class, AUTHOR_3_ID));
    }

    private List<Author> getAll() {
        return DataProducer.getAllAuthors();
    }
}