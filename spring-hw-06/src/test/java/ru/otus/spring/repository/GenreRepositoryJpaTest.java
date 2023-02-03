package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.GenreHasRelationsEx;
import ru.otus.spring.exception.GenreNotFoundEx;
import ru.otus.spring.util.DataProducer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий для работы с жанрами должен")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepositoryJpa genreRepository;

    private final static long GENRE_5_ID = 5;
    private final static String GENRE_5_NAME = "Genre 5";
    private final static long BOOK_1_ID = 1;

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnRecordsCnt() {
        assertEquals(getAll().size(), genreRepository.count());
    }

    @DisplayName("возвращать список всех жанров")
    @Test
    void shouldReturnAllGenres() {
        assertIterableEquals(getAll(), genreRepository.getAll());
    }

    @DisplayName("возвращать жанр по идентификатору")
    @Test
    void shouldReturnGenreById() throws GenreNotFoundEx {
        assertEquals(getAll().get(0), genreRepository.getById(getAll().get(0).getId()));
    }

    @DisplayName("возвращать жанр по имени")
    @Test
    void shouldReturnGenreByName() throws GenreNotFoundEx {
        assertEquals(getAll().get(0), genreRepository.getByName(getAll().get(0).getName()));
    }

    @DisplayName("возвращать жанры по идентификатору книги")
    @Test
    void shouldReturnGenresByBookId() {
        assertIterableEquals(getByBookId(BOOK_1_ID), genreRepository.getByBookId(BOOK_1_ID));
    }

    @DisplayName("добавлять жанр")
    @DirtiesContext
    @Test
    void shouldCreateGenre() throws GenreNotFoundEx {
        assertThrowsExactly(GenreNotFoundEx.class, () -> genreRepository.getByName(GENRE_5_NAME));
        genreRepository.add(new Genre(GENRE_5_NAME));
        assertEquals(GENRE_5_NAME, genreRepository.getById(GENRE_5_ID).getName());
    }

    @DisplayName("удалять жанр")
    @DirtiesContext
    @Test
    void shouldDeleteGenre() throws GenreNotFoundEx, GenreHasRelationsEx {
        genreRepository.add(new Genre(GENRE_5_NAME));
        assertDoesNotThrow(() -> genreRepository.getByName(GENRE_5_NAME));
        genreRepository.delByName(GENRE_5_NAME);
        assertThrowsExactly(GenreNotFoundEx.class, () -> genreRepository.getByName(GENRE_5_NAME));
    }

    private List<Genre> getAll() {
        return DataProducer.getAllGenres();
    }

    private List<Genre> getByBookId(long bookId) {
        return DataProducer.getGenreByBookId(bookId);
    }
}