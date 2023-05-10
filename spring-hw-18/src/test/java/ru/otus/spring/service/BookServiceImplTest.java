package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.BookAlreadyExistsEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.repository.BookRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.otus.spring.util.DataProducer.*;

@SuppressWarnings("SameParameterValue")
@DisplayName("Сервис работы с книгами должен")
@SpringBootTest
@Import(BookServiceImpl.class)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String BOOK_1_ID = "1";
    private final static String BOOK_5_NAME = "Book 5";
    private final static String BOOK_5_ID = "5";
    private static final long DELAY = 1500;
    private static final long DEFAULT_COUNT = -1;
    private static final Book DEFAULT_BOOK = new Book("Error", "Error", "Error");

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnBookCountValid() {
        when(bookRepository.count()).thenReturn(Long.valueOf(getAllBooks().size()));

        assertEquals(getAllBooks().size(), bookService.count());
    }

    @DisplayName("возвращать заменённое число записей при некорректной работе источника")
    @Test
    void shouldReturnBookCountInvalid() {
        Mockito.doAnswer(new AnswersWithDelay(DELAY, invocation -> (long) getAllBooks().size())).when(bookRepository).count();

        assertEquals(DEFAULT_COUNT, bookService.count());
    }

    @DisplayName("возвращать корректный список всех книг")
    @Test
    void shouldReturnAllBooksValid() {
        when(bookRepository.findAll()).thenReturn(getAllBooks());

        assertEquals(BookConverter.toDto(getAllBooks()), bookService.getAll());
    }

    @DisplayName("возвращать заменённый список всех книг при некорректной работе источника")
    @Test
    void shouldReturnAllBooksInvalid() {
        Mockito.doAnswer(new AnswersWithDelay(DELAY, invocation -> List.of(DEFAULT_BOOK)))
                .when(bookRepository).findAll();

        assertEquals(List.of(DEFAULT_BOOK.toDto()), bookService.getAll());
    }

    @DisplayName("возвращать корректный список книг по имени жанра")
    @Test
    void shouldReturnBooksByGenreNameValid() {
        when(bookRepository.findAllByGenresName(GENRE_1_NAME)).thenReturn(getAllBooksByGenreName(GENRE_1_NAME));

        assertEquals(BookConverter.toDto(getAllBooksByGenreName(GENRE_1_NAME)), bookService.getAllByGenreName(GENRE_1_NAME));
    }

    @DisplayName("возвращать заменённый список книг по имени жанра при некорректной работе источника")
    @Test
    void shouldReturnBooksByGenreNameInvalid() {
        Mockito.doAnswer(new AnswersWithDelay(DELAY, invocation -> List.of(DEFAULT_BOOK)))
                .when(bookRepository).findAllByGenresName(GENRE_1_NAME);

        assertEquals(List.of(DEFAULT_BOOK.toDto()), bookService.getAllByGenreName(GENRE_1_NAME));
    }

    @DisplayName("возвращать корректный список книг по имени автора")
    @Test
    void shouldReturnBooksByAuthorNameValid() {
        when(bookRepository.findAllByAuthorName(AUTHOR_1_NAME)).thenReturn(getAllBooksByAuthorName(AUTHOR_1_NAME));

        assertEquals(BookConverter.toDto(getAllBooksByAuthorName(AUTHOR_1_NAME)), bookService.getAllByAuthorName(AUTHOR_1_NAME));
    }

    @DisplayName("возвращать заменённый список книг по имени автора при некорректной работе источника")
    @Test
    void shouldReturnBooksByAuthorNameInvalid() {
        Mockito.doAnswer(new AnswersWithDelay(DELAY, invocation -> List.of(DEFAULT_BOOK)))
                .when(bookRepository).findAllByAuthorName(AUTHOR_1_NAME);

        assertEquals(List.of(DEFAULT_BOOK.toDto()), bookService.getAllByAuthorName(AUTHOR_1_NAME));
    }

    @DisplayName("возвращать книгу по идентификатору")
    @Test
    void shouldReturnBookById() throws BookNotFoundEx {
        when(bookRepository.findById(BOOK_1_ID)).thenReturn(getBookById(BOOK_1_ID));

        assertEquals(getBookById(BOOK_1_ID).orElseThrow().toDto(), bookService.getById(BOOK_1_ID));
    }

    @DisplayName("добавлять и возвращать книгу")
    @Test
    void shouldAddAndReturnBook() throws BookAlreadyExistsEx {
        Book book = new Book(BOOK_5_ID, BOOK_5_NAME, new Author(AUTHOR_1_NAME), List.of(new Genre(GENRE_1_NAME)));
        when(bookRepository.existsByName(BOOK_5_NAME)).thenReturn(false);
        when(bookRepository.save(any())).thenReturn(book);

        assertEquals(book.toDto(), bookService.add(BOOK_5_NAME, AUTHOR_1_NAME, GENRE_1_NAME));
    }

    @DisplayName("удалять книгу по идентификатору")
    @Test
    void shouldDeleteBookByID() throws BookNotFoundEx {
        when(bookRepository.findById(BOOK_1_ID)).thenReturn(getBookById(BOOK_1_ID));

        bookService.deleteById(BOOK_1_ID);

        verify(bookRepository, times(1)).deleteWithComments(getBookById(BOOK_1_ID).orElseThrow());
    }
}