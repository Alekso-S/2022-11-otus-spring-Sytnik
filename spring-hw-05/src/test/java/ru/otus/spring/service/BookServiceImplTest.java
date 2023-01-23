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
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.AuthorNotFoundEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.GenreNotFoundEx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Сервис работы с книгами должен")
@Import(BookServiceImpl.class)
@JdbcTest
@ExtendWith(SpringExtension.class)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @MockBean
    private BookDao bookDao;
    @MockBean
    private AuthorDao authorDao;
    @MockBean
    private GenreDao genreDao;

    private final static long BOOK_1_ID = 1;
    private final static long BOOK_2_ID = 2;
    private final static long BOOK_3_ID = 3;
    private final static long BOOK_4_ID = 4;
    private final static long BOOK_5_ID = 5;
    private final static long BOOK_1_AUTHOR_ID = 1;
    private final static long BOOK_2_AUTHOR_ID = 1;
    private final static long BOOK_3_AUTHOR_ID = 2;
    private final static long BOOK_4_AUTHOR_ID = 2;
    private final static String BOOK_1_NAME = "Book 1";
    private final static String BOOK_2_NAME = "Book 2";
    private final static String BOOK_3_NAME = "Book 3";
    private final static String BOOK_4_NAME = "Book 4";
    private final static String BOOK_5_NAME = "Book 5";
    private final static long AUTHOR_1_ID = 1;
    private final static long AUTHOR_2_ID = 2;
    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String AUTHOR_2_NAME = "Author 2";
    private final static long GENRE_1_ID = 1;
    private final static long GENRE_2_ID = 2;
    private final static long GENRE_3_ID = 3;
    private final static long GENRE_4_ID = 4;
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String GENRE_2_NAME = "Genre 2";
    private final static String GENRE_3_NAME = "Genre 3";
    private final static String GENRE_4_NAME = "Genre 4";
    private final static String SUCCESSFUL_ADD_MESSAGE = "Book added";
    private final static String SUCCESSFUL_DEL_MESSAGE = "Book deleted";

    @BeforeEach
    void setUp() throws AuthorNotFoundEx, BookNotFoundEx, GenreNotFoundEx {
        when(bookDao.count()).thenReturn(Long.valueOf(getAllBooks().size()));
        when(bookDao.getAll()).thenReturn(getAllBooks());
        when(authorDao.getById(AUTHOR_1_ID)).thenReturn(new Author(AUTHOR_1_ID, AUTHOR_1_NAME));
        when(authorDao.getById(AUTHOR_2_ID)).thenReturn(new Author(AUTHOR_2_ID, AUTHOR_2_NAME));
        when(genreDao.getByBookId(BOOK_1_ID)).thenReturn(getAllBooks().get(0).getGenres());
        when(genreDao.getByBookId(BOOK_2_ID)).thenReturn(getAllBooks().get(1).getGenres());
        when(genreDao.getByBookId(BOOK_3_ID)).thenReturn(getAllBooks().get(2).getGenres());
        when(genreDao.getByBookId(BOOK_4_ID)).thenReturn(getAllBooks().get(3).getGenres());
        when(bookDao.getById(BOOK_1_ID)).thenReturn(getAllBooks().get(0));
        when(bookDao.getByName(BOOK_1_NAME)).thenReturn(getAllBooks().get(0));
        when(genreDao.getByName(GENRE_1_NAME)).thenReturn(new Genre(GENRE_1_ID, GENRE_1_NAME));
        when(bookDao.getByName(BOOK_5_NAME)).thenThrow(BookNotFoundEx.class);
        when(authorDao.getByName(AUTHOR_1_NAME)).thenReturn(new Author(AUTHOR_1_ID, AUTHOR_1_NAME));
        when(bookDao.add(new Book(0, AUTHOR_1_ID, BOOK_5_NAME)))
                .thenReturn(new Book(BOOK_5_ID, AUTHOR_1_ID, BOOK_5_NAME));
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldShowBookCount() {
        assertEquals(getAllBooks().size(), bookService.showBooksCount());
    }

    @DisplayName("возвращать список всех книг")
    @Test
    void shouldShowAllBooks() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book book : getAllBooks()) {
            stringBuilder.append(BookConverter.toString(book)).append('\n');
        }
        assertEquals(stringBuilder.toString(), bookService.showAllBooks());
    }

    @DisplayName("возвращать книгу по идентификатору")
    @Test
    void shouldShowBookById() {
        assertEquals(BookConverter.toString(getAllBooks().get(0)),
                bookService.showBookById(BOOK_1_ID));
    }

    @DisplayName("возвращать книгу по имени")
    @Test
    void shouldShowBookByName() {
        assertEquals(BookConverter.toString(getAllBooks().get(0)),
                bookService.showBookByName(BOOK_1_NAME));
    }

    @DisplayName("возвращать список книг по идентификатору жанра")
    @Test
    void shouldShowBooksByGenreId() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book book : getBooksByGenreId(GENRE_1_ID)) {
            stringBuilder.append(BookConverter.toString(book)).append('\n');
        }
        assertEquals(stringBuilder.toString(), bookService.showBooksByGenreId(GENRE_1_ID));
    }

    @DisplayName("возвращать список книг по имени жанра")
    @Test
    void shouldShowBooksByGenreName() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book book : getBooksByGenreId(GENRE_1_ID)) {
            stringBuilder.append(BookConverter.toString(book)).append('\n');
        }
        assertEquals(stringBuilder.toString(), bookService.showBooksByGenreName(GENRE_1_NAME));
    }

    @DisplayName("выводить сообщение об успешном добавлении книги")
    @Test
    void shouldShowAddBookMessage() {
        assertEquals(SUCCESSFUL_ADD_MESSAGE, bookService.addBook(BOOK_5_NAME, AUTHOR_1_NAME, GENRE_1_NAME));
    }

    @DisplayName("выводить сообщение об успешном удалении книги")
    @Test
    void shouldShowDelBookMessage() {
        assertEquals(SUCCESSFUL_DEL_MESSAGE, bookService.delBookByName(BOOK_1_NAME));
    }

    private List<Book> getAllBooks() {
        Book book;
        List<Book> authors = new ArrayList<>();
        book = new Book(BOOK_1_ID, BOOK_1_AUTHOR_ID, BOOK_1_NAME);
        book.setAuthor(new Author(AUTHOR_1_ID, AUTHOR_1_NAME));
        book.setGenres(List.of(
                new Genre(GENRE_1_ID, GENRE_1_NAME),
                new Genre(GENRE_2_ID, GENRE_2_NAME)));
        authors.add(book);
        book = new Book(BOOK_2_ID, BOOK_2_AUTHOR_ID, BOOK_2_NAME);
        book.setAuthor(new Author(AUTHOR_1_ID, AUTHOR_1_NAME));
        book.setGenres(List.of(
                new Genre(GENRE_2_ID, GENRE_2_NAME),
                new Genre(GENRE_3_ID, GENRE_3_NAME)));
        authors.add(book);
        book = new Book(BOOK_3_ID, BOOK_3_AUTHOR_ID, BOOK_3_NAME);
        book.setAuthor(new Author(AUTHOR_2_ID, AUTHOR_2_NAME));
        book.setGenres(List.of(
                new Genre(GENRE_3_ID, GENRE_3_NAME),
                new Genre(GENRE_4_ID, GENRE_4_NAME)));
        authors.add(book);
        book = new Book(BOOK_4_ID, BOOK_4_AUTHOR_ID, BOOK_4_NAME);
        book.setAuthor(new Author(AUTHOR_2_ID, AUTHOR_2_NAME));
        book.setGenres(List.of(
                new Genre(GENRE_4_ID, GENRE_4_NAME),
                new Genre(GENRE_1_ID, GENRE_1_NAME)));
        authors.add(book);
        return authors;
    }

    private List<Book> getBooksByGenreId(long genreId) {
        return getAllBooks()
                .stream()
                .filter((x) -> x.getGenres().contains(new Genre(genreId, "")))
                .collect(Collectors.toList());
    }
}