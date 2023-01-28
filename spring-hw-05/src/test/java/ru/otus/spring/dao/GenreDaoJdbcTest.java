package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.GenreHasRelationsEx;
import ru.otus.spring.exception.GenreNotFoundEx;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DAO для работы с жанрами должен")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    @Autowired
    private GenreDaoJdbc genreDaoJdbc;

    private final static long GENRE_1_ID = 1;
    private final static long GENRE_2_ID = 2;
    private final static long GENRE_3_ID = 3;
    private final static long GENRE_4_ID = 4;
    private final static long GENRE_5_ID = 5;
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String GENRE_2_NAME = "Genre 2";
    private final static String GENRE_3_NAME = "Genre 3";
    private final static String GENRE_4_NAME = "Genre 4";
    private final static String GENRE_5_NAME = "Genre 5";
    private final static long BOOK_1_ID = 1;
    private final static String BOOK_1_NAME = "Book 1";

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnRecordsCnt() {
        assertEquals(getAllGenres().size(), genreDaoJdbc.count());
    }

    @DisplayName("возвращать список всех жанров")
    @Test
    void shouldReturnAllGenres() {
        assertIterableEquals(getAllGenres(), genreDaoJdbc.getAll());
    }

    @DisplayName("возвращать жанр по идентификатору")
    @Test
    void shouldReturnGenreById() throws GenreNotFoundEx {
        assertEquals(getAllGenres().get(0), genreDaoJdbc.getById(getAllGenres().get(0).getId()));
    }

    @DisplayName("возвращать жанр по имени")
    @Test
    void shouldReturnGenreByName() throws GenreNotFoundEx {
        assertEquals(getAllGenres().get(0), genreDaoJdbc.getByName(getAllGenres().get(0).getName()));
    }

    @DisplayName("возвращать жанры по идентификатору книги")
    @Test
    void shouldReturnGenresByBookId() {
        assertIterableEquals(getGenresByBookId(BOOK_1_ID), genreDaoJdbc.getByBookId(BOOK_1_ID));
    }

    @DisplayName("добавлять жанр")
    @DirtiesContext
    @Test
    void shouldCreateGenre() throws GenreNotFoundEx {
        assertThrowsExactly(GenreNotFoundEx.class, () -> genreDaoJdbc.getByName(GENRE_5_NAME));
        genreDaoJdbc.add(new Genre(0, GENRE_5_NAME));
        assertEquals(GENRE_5_NAME, genreDaoJdbc.getById(GENRE_5_ID).getName());
    }

    @DisplayName("удалять жанр")
    @DirtiesContext
    @Test
    void shouldDeleteGenre() throws GenreNotFoundEx, GenreHasRelationsEx {
        genreDaoJdbc.add(new Genre(0, GENRE_5_NAME));
        assertDoesNotThrow(() -> genreDaoJdbc.getByName(GENRE_5_NAME));
        genreDaoJdbc.delByName(GENRE_5_NAME);
        assertThrowsExactly(GenreNotFoundEx.class, () -> genreDaoJdbc.getByName(GENRE_5_NAME));
    }

    @DisplayName("добавлять жанры для книги")
    @Test
    void shouldAddGenresForBook() {
        List<Genre> genres = getGenresByBookId(BOOK_1_ID);
        genreDaoJdbc.addGenresForBook(BOOK_1_ID, List.of(new Genre(GENRE_3_ID, GENRE_3_NAME)));
        genres.add(new Genre(GENRE_3_ID, GENRE_3_NAME));
        assertIterableEquals(genres, genreDaoJdbc.getByBookId(BOOK_1_ID));
    }

    @Test
    void delGenresForBook() {
        genreDaoJdbc.delGenresByBookName(BOOK_1_NAME);
        assertEquals(0, genreDaoJdbc.getByBookId(BOOK_1_ID).size());
    }

    private List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(GENRE_1_ID, GENRE_1_NAME));
        genres.add(new Genre(GENRE_2_ID, GENRE_2_NAME));
        genres.add(new Genre(GENRE_3_ID, GENRE_3_NAME));
        genres.add(new Genre(GENRE_4_ID, GENRE_4_NAME));
        return genres;
    }

    private List<Genre> getGenresByBookId(long bookId) {
        if (bookId != BOOK_1_ID) {
            throw new RuntimeException("Unsupported book id");
        }
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(GENRE_1_ID, GENRE_1_NAME));
        genres.add(new Genre(GENRE_2_ID, GENRE_2_NAME));
        return genres;
    }
}