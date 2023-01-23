package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.BookNotFoundEx;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DAO для работы с книгами должен")
@Import(BookDaoJdbc.class)
@JdbcTest
@ExtendWith(SpringExtension.class)
class BookDaoJdbcTest {

    @Autowired
    private BookDaoJdbc bookDaoJdbc;

    private final static long BOOK_1_ID = 1;
    private final static long BOOK_2_ID = 2;
    private final static long BOOK_3_ID = 3;
    private final static long BOOK_4_ID = 4;
    private final static long BOOK_5_ID = 5;
    private final static long BOOK_1_AUTHOR_ID = 1;
    private final static long BOOK_2_AUTHOR_ID = 1;
    private final static long BOOK_3_AUTHOR_ID = 2;
    private final static long BOOK_4_AUTHOR_ID = 2;
    private final static long BOOK_5_AUTHOR_ID = 1;
    private final static String BOOK_1_NAME = "Book 1";
    private final static String BOOK_2_NAME = "Book 2";
    private final static String BOOK_3_NAME = "Book 3";
    private final static String BOOK_4_NAME = "Book 4";
    private final static String BOOK_5_NAME = "Book 5";
    private final static long GENRE_1_ID = 1;
    private final static long AUTHOR_1_ID = 1;

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
        bookDaoJdbc.add(new Book(0, BOOK_5_AUTHOR_ID, BOOK_5_NAME));
        assertEquals(BOOK_5_NAME, bookDaoJdbc.getById(BOOK_5_ID).getName());
    }

    @DisplayName("удалять книгу")
    @DirtiesContext
    @Test
    void shouldDeleteAuthor() {
        bookDaoJdbc.add(new Book(0, BOOK_5_AUTHOR_ID, BOOK_5_NAME));
        assertDoesNotThrow(() -> bookDaoJdbc.getByName(BOOK_5_NAME));
        bookDaoJdbc.delByName(BOOK_5_NAME);
        assertThrowsExactly(BookNotFoundEx.class, () -> bookDaoJdbc.getByName(BOOK_5_NAME));
    }

    private List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(BOOK_1_ID, BOOK_1_AUTHOR_ID, BOOK_1_NAME));
        books.add(new Book(BOOK_2_ID, BOOK_2_AUTHOR_ID, BOOK_2_NAME));
        books.add(new Book(BOOK_3_ID, BOOK_3_AUTHOR_ID, BOOK_3_NAME));
        books.add(new Book(BOOK_4_ID, BOOK_4_AUTHOR_ID, BOOK_4_NAME));
        return books;
    }

    private List<Book> getBooksByGenreId(long genreId) {
        if (genreId != GENRE_1_ID) {
            throw new RuntimeException("Unsupported genre id");
        }
        List<Book> books = new ArrayList<>();
        books.add(new Book(BOOK_1_ID, BOOK_1_AUTHOR_ID, BOOK_1_NAME));
        books.add(new Book(BOOK_4_ID, BOOK_4_AUTHOR_ID, BOOK_4_NAME));
        return books;
    }

    private List<Book> getBooksByAuthorId(long authorId) {
        if (authorId != AUTHOR_1_ID) {
            throw new RuntimeException("Unsupported author id");
        }
        List<Book> books = new ArrayList<>();
        books.add(new Book(BOOK_1_ID, BOOK_1_AUTHOR_ID, BOOK_1_NAME));
        books.add(new Book(BOOK_2_ID, BOOK_2_AUTHOR_ID, BOOK_2_NAME));
        return books;
    }
}