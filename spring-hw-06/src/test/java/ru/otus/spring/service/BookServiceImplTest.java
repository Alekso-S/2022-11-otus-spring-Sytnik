package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.util.DataProducer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SuppressWarnings("SameParameterValue")
@DisplayName("Сервис работы с книгами должен")
@SpringBootTest
@Import(BookServiceImpl.class)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private BookRepository bookRepository;

    private final static long BOOK_1_ID = 1;
    private final static String BOOK_1_NAME = "Book 1";
    private final static String BOOK_5_NAME = "Book 5";
    private final static String AUTHOR_1_NAME = "Author 1";
    private final static long GENRE_1_ID = 1;
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String SUCCESSFUL_ADD_MESSAGE = "Book added";
    private final static String SUCCESSFUL_DEL_MESSAGE = "Book deleted";

    @BeforeEach
    void setUp() throws BookNotFoundEx {
        when(bookRepository.count()).thenReturn(Long.valueOf(getAll().size()));
        when(bookRepository.getAll()).thenReturn(getAll());
        when(bookRepository.getById(BOOK_1_ID)).thenReturn(getAll().get(0));
        when(bookRepository.getByName(BOOK_1_NAME)).thenReturn(getAll().get(0));
        when(bookRepository.getByGenreId(GENRE_1_ID)).thenReturn(getByGenreId(GENRE_1_ID));
        when(bookRepository.getByGenreName(GENRE_1_NAME)).thenReturn(getByGenreId(GENRE_1_ID));
        when(genreRepository.checkExistenceById(GENRE_1_ID)).thenReturn(true);
        when(genreRepository.checkExistenceByName(GENRE_1_NAME)).thenReturn(true);
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldShowBookCount() {
        assertEquals(getAll().size(), bookService.showBooksCount());
    }

    @DisplayName("возвращать список всех книг")
    @Test
    void shouldShowAllBooks() {
        String booksString = getAll().stream()
                .sorted(Comparator.comparing(Book::getId))
                .map(BookConverter::toString)
                .collect(Collectors.joining("\n"));
        assertEquals(booksString, bookService.showAllBooks());
    }

    @DisplayName("возвращать книгу по идентификатору")
    @Test
    void shouldShowBookById() {
        assertEquals(BookConverter.toString(getAll().get(0)),
                bookService.showBookById(BOOK_1_ID));
    }

    @DisplayName("возвращать книгу по имени")
    @Test
    void shouldShowBookByName() {
        assertEquals(BookConverter.toString(getAll().get(0)),
                bookService.showBookByName(BOOK_1_NAME));
    }

    @DisplayName("возвращать список книг по идентификатору жанра")
    @Test
    void shouldShowBooksByGenreId() {
        String booksString = getByGenreId(GENRE_1_ID).stream()
                .sorted(Comparator.comparing(Book::getId))
                .map(BookConverter::toString)
                .collect(Collectors.joining("\n"));
        assertEquals(booksString, bookService.showBooksByGenreId(GENRE_1_ID));
    }

    @DisplayName("возвращать список книг по имени жанра")
    @Test
    void shouldShowBooksByGenreName() {
        String booksString = getByGenreId(GENRE_1_ID).stream()
                .sorted(Comparator.comparing(Book::getId))
                .map(BookConverter::toString)
                .collect(Collectors.joining("\n"));
        assertEquals(booksString, bookService.showBooksByGenreName(GENRE_1_NAME));
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

    private List<Book> getAll() {
        return DataProducer.getAllBooks();
    }

    private List<Book> getByGenreId(long genreId) {
        return DataProducer.getBooksByGenreId(genreId);
    }
}