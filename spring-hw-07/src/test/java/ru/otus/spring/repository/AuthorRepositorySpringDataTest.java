package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Author;
import ru.otus.spring.util.DataProducer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SameParameterValue")
@DataJpaTest
@DisplayName("Репозиторий для работы с авторами должен")
class AuthorRepositorySpringDataTest {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private TestEntityManager entityManager;

    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String AUTHOR_3_NAME = "Author 3";

    @Test
    @DisplayName("находить автора по имени")
    void shouldFindByName() {
        assertEquals(getByName(AUTHOR_1_NAME), authorRepository.findByName(AUTHOR_1_NAME));
        assertEquals(Optional.empty(), authorRepository.findByName(AUTHOR_3_NAME));
    }

    @Test
    @DisplayName("проверять наличие автора по имени")
    void shouldCheckIfExistsByName() {
        assertTrue(authorRepository.existsByName(AUTHOR_1_NAME));
        assertFalse(authorRepository.existsByName(AUTHOR_3_NAME));
    }

    @Test
    @DirtiesContext
    @DisplayName("должен добавлять нового автора")
    void shouldAdd() {
        Author author = authorRepository.save(new Author(AUTHOR_3_NAME));
        assertEquals(author, entityManager.find(Author.class, author.getId()));
    }

    private Optional<Author> getByName(String name) {
        return DataProducer.getAuthorByName(name);
    }
}