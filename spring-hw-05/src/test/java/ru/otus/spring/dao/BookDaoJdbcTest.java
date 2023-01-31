package ru.otus.spring.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.BookNotFoundEx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("DAO для работы с книгами должен")
@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {

    @Autowired
    private BookDaoJdbc bookDaoJdbc;
    @MockBean
    private GenreDao genreDao;

    private final static long BOOK_1_ID = 1;
    private final static long BOOK_2_ID = 2;
    private final static long BOOK_3_ID = 3;
    private final static long BOOK_4_ID = 4;
    private final static long BOOK_5_ID = 5;
    private final static String BOOK_1_NAME = "Book 1";
    private final static String BOOK_2_NAME = "Book 2";
    private final static String BOOK_3_NAME = "Book 3";
    private final static String BOOK_4_NAME = "Book 4";
    private final static String BOOK_5_NAME = "Book 5";
    private final static Author BOOK_1_AUTHOR = new Author(1, "Author 1");
    private final static Author BOOK_2_AUTHOR = new Author(1, "Author 1");
    private final static Author BOOK_3_AUTHOR = new Author(2, "Author 2");
    private final static Author BOOK_4_AUTHOR = new Author(2, "Author 2");
    private final static Author BOOK_5_AUTHOR = new Author(1, "Author 1");
    private final static long AUTHOR_1_ID = 1;
    private final static long GENRE_1_ID = 1;
    private final static long GENRE_2_ID = 2;
    private final static long GENRE_3_ID = 3;
    private final static long GENRE_4_ID = 4;
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String GENRE_2_NAME = "Genre 2";
    private final static String GENRE_3_NAME = "Genre 3";
    private final static String GENRE_4_NAME = "Genre 4";

    @BeforeEach
    void setUp() {
        when(genreDao.getAllUsed()).thenReturn(getAllGenres());
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnRecordsCnt() {
        assertEquals(getAllBooks().size(), bookDaoJdbc.count());
    }

    @DisplayName("возвращать список всех книг")
    @Test
    void shouldReturnAllBooks() {
        assertIterableEquals(getAllBooks(), bookDaoJdbc.getAll());
    }

    @DisplayName("возвращать книгу по идентификатору")
    @Test
    void shouldReturnBookById() throws BookNotFoundEx {
        assertEquals(getAllBooks().get(0), bookDaoJdbc.getById(getAllBooks().get(0).getId()));
    }

    @DisplayName("возвращать книгу по имени")
    @Test
    void shouldReturnBookByName() throws BookNotFoundEx {
        assertEquals(getAllBooks().get(0), bookDaoJdbc.getByName(getAllBooks().get(0).getName()));
    }

    @DisplayName("возвращать список книг по идентификатору жанра")
    @Test
    void shouldReturnBooksByGenreId() {
        assertIterableEquals(getBooksByGenreId(GENRE_1_ID), bookDaoJdbc.getByGenreId(GENRE_1_ID));
    }

    @DisplayName("возвращать список книг по идентификатору автора")
    @Test
    void shouldReturnBooksByAuthorId() {
        assertIterableEquals(getBooksByAuthorId(AUTHOR_1_ID), bookDaoJdbc.getByAuthorId(AUTHOR_1_ID));
    }

    @DisplayName("добавлять книгу")
    @DirtiesContext
    @Test
    void shouldCreateBook() throws BookNotFoundEx {
        assertThrowsExactly(BookNotFoundEx.class, () -> bookDaoJdbc.getByName(BOOK_5_NAME));
        bookDaoJdbc.add(new Book(0, BOOK_5_NAME, BOOK_5_AUTHOR, new ArrayList<>()));
        assertEquals(BOOK_5_NAME, bookDaoJdbc.getById(BOOK_5_ID).getName());
    }

    @DisplayName("удалять книгу")
    @DirtiesContext
    @Test
    void shouldDeleteAuthor() throws BookNotFoundEx {
        bookDaoJdbc.add(new Book(0, BOOK_5_NAME, BOOK_5_AUTHOR, new ArrayList<>()));
        assertDoesNotThrow(() -> bookDaoJdbc.getByName(BOOK_5_NAME));
        bookDaoJdbc.delByName(BOOK_5_NAME);
        assertThrowsExactly(BookNotFoundEx.class, () -> bookDaoJdbc.getByName(BOOK_5_NAME));
    }

    private List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(BOOK_1_ID, BOOK_1_NAME, BOOK_1_AUTHOR, List.of(
                new Genre(GENRE_1_ID, GENRE_1_NAME),
                new Genre(GENRE_2_ID, GENRE_2_NAME)))
        );
        books.add(new Book(BOOK_2_ID, BOOK_2_NAME, BOOK_2_AUTHOR, List.of(
                new Genre(GENRE_2_ID, GENRE_2_NAME),
                new Genre(GENRE_3_ID, GENRE_3_NAME)))
        );
        books.add(new Book(BOOK_3_ID, BOOK_3_NAME, BOOK_3_AUTHOR, List.of(
                new Genre(GENRE_3_ID, GENRE_3_NAME),
                new Genre(GENRE_4_ID, GENRE_4_NAME)))
        );
        books.add(new Book(BOOK_4_ID, BOOK_4_NAME, BOOK_4_AUTHOR, List.of(
                new Genre(GENRE_1_ID, GENRE_1_NAME),
                new Genre(GENRE_4_ID, GENRE_4_NAME)))
        );
        return books;
    }

    private List<Book> getBooksByGenreId(long genreId) {
        return getAllBooks()
                .stream()
                .filter((x) -> x.getGenres().stream().anyMatch((y) -> y.getId() == genreId))
                .collect(Collectors.toList());
    }

    private List<Book> getBooksByAuthorId(long authorId) {
        return getAllBooks()
                .stream()
                .filter((x) -> x.getAuthor().getId() == authorId)
                .collect(Collectors.toList());
    }

    private List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(GENRE_1_ID, GENRE_1_NAME));
        genres.add(new Genre(GENRE_2_ID, GENRE_2_NAME));
        genres.add(new Genre(GENRE_3_ID, GENRE_3_NAME));
        genres.add(new Genre(GENRE_4_ID, GENRE_4_NAME));
        return genres;
    }
}