package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.util.DataProducer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SameParameterValue")
@DataJpaTest
@DisplayName("Репозиторий для работы с жанрами должен")
class GenreRepositorySpringDataTest {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private TestEntityManager entityManager;

    private final static String GENRE_1_NAME = "Genre 1";
    private final static String GENRE_5_NAME = "Genre 5";

    @Test
    @DisplayName("находить жанр по имени")
    void shouldFindByName() {
        assertEquals(getByName(GENRE_1_NAME),genreRepository.findByName(GENRE_1_NAME));
        assertEquals(Optional.empty(),genreRepository.findByName(GENRE_5_NAME));
    }

    @Test
    @DisplayName("проверять наличие жанра по имени")
    void shouldCheckIfExistsByName() {
        assertTrue(genreRepository.existsByName(GENRE_1_NAME));
        assertFalse(genreRepository.existsByName(GENRE_5_NAME));
    }

    @Test
    @DirtiesContext
    @DisplayName("должен добавлять новый жанр")
    void shouldAdd() {
        Genre genre = genreRepository.save(new Genre(GENRE_5_NAME));
        assertEquals(genre, entityManager.find(Genre.class, genre.getId()));
    }

    private Optional<Genre> getByName(String name) {
        return DataProducer.getGenreByName(name);
    }
}