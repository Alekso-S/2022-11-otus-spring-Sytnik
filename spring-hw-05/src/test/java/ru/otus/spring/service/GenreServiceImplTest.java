package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.GenreNotFoundEx;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Сервис работы с авторами должен")
@Import(GenreServiceImpl.class)
@JdbcTest
@ExtendWith(SpringExtension.class)
class GenreServiceImplTest {

    @Autowired
    private GenreServiceImpl genreService;

    @MockBean
    GenreDao genreDao;
    @MockBean
    BookDao bookDao;

    private final static long GENRE_1_ID = 1;
    private final static long GENRE_2_ID = 2;
    private final static long GENRE_3_ID = 3;
    private final static long GENRE_4_ID = 4;
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String GENRE_2_NAME = "Genre 2";
    private final static String GENRE_3_NAME = "Genre 3";
    private final static String GENRE_4_NAME = "Genre 4";
    private final static String GENRE_5_NAME = "Genre 5";
    private final static long BOOK_1_ID = 1;
    private final static long BOOK_1_AUTHOR_ID = 1;
    private final static String BOOK_1_NAME = "Book 1";
    private final static String SUCCESSFUL_ADD_MESSAGE = "Genre added";
    private final static String SUCCESSFUL_DEL_MESSAGE = "Genre deleted";

    @BeforeEach
    void setUp() throws GenreNotFoundEx, BookNotFoundEx {
        when(genreDao.count()).thenReturn(Long.valueOf(getAllGenres().size()));
        when(genreDao.getAll()).thenReturn(getAllGenres());
        when(genreDao.getById(GENRE_1_ID)).thenReturn(new Genre(GENRE_1_ID, GENRE_1_NAME));
        when(genreDao.getByName(GENRE_1_NAME)).thenReturn(new Genre(GENRE_1_ID, GENRE_1_NAME));
        when(genreDao.getByName(GENRE_5_NAME)).thenThrow(GenreNotFoundEx.class);
        when(genreDao.getByBookId(BOOK_1_ID)).thenReturn(getGenresByBookId(BOOK_1_ID));
        when(bookDao.getByName(BOOK_1_NAME)).thenReturn(new Book(BOOK_1_ID, BOOK_1_AUTHOR_ID, BOOK_1_NAME));
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldShowGenresCount() {
        assertEquals(getAllGenres().size(), genreService.showGenresCount());
    }

    @DisplayName("возвращать список всех жанров")
    @Test
    void showAllGenres() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : getAllGenres()) {
            stringBuilder.append(GenreConverter.toString(genre)).append('\n');
        }
        assertEquals(stringBuilder.toString(), genreService.showAllGenres());
    }

    @DisplayName("возвращать жанр по идентификатору")
    @Test
    void shouldShowGenreById() {
        assertEquals(GenreConverter.toString(new Genre(GENRE_1_ID, GENRE_1_NAME)),
                genreService.showGenreById(GENRE_1_ID));
    }

    @DisplayName("возвращать жанр по имени")
    @Test
    void shouldShowGenreByName() {
        assertEquals(GenreConverter.toString(new Genre(GENRE_1_ID, GENRE_1_NAME)),
                genreService.showGenreByName(GENRE_1_NAME));
    }

    @DisplayName("возвращать список жанров по идентификатору книги")
    @Test
    void shouldShowGenresByBookId() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : getGenresByBookId(BOOK_1_ID)) {
            stringBuilder.append(GenreConverter.toString(genre)).append('\n');
        }
        assertEquals(stringBuilder.toString(), genreService.showGenresByBookId(BOOK_1_ID));
    }

    @DisplayName("возвращать список жанров по имени книги")
    @Test
    void shouldShowGenresByBookName() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : getGenresByBookId(BOOK_1_ID)) {
            stringBuilder.append(GenreConverter.toString(genre)).append('\n');
        }
        assertEquals(stringBuilder.toString(), genreService.showGenresByBookName(BOOK_1_NAME));
    }

    @DisplayName("выводить сообщение об успешном добавлении жанра")
    @Test
    void shouldShowAddGenreMessage() {
        assertEquals(SUCCESSFUL_ADD_MESSAGE, genreService.addGenre(GENRE_5_NAME));
    }

    @DisplayName("выводить сообщение об успешном удалении жанра")
    @Test
    void shouldShowDelGenreMessage() {
        assertEquals(SUCCESSFUL_DEL_MESSAGE, genreService.delGenreByName(GENRE_1_NAME));
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