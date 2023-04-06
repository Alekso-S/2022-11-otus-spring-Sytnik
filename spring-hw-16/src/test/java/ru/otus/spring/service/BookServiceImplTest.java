package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnBookCount() {
        when(bookRepository.count()).thenReturn(Long.valueOf(getAllBooks().size()));

        assertEquals(getAllBooks().size(), bookService.count());
    }

    @DisplayName("возвращать список всех книг")
    @Test
    void shouldReturnAllBooks() {
        when(bookRepository.findAll()).thenReturn(getAllBooks());

        assertEquals(BookConverter.toDto(getAllBooks()), bookService.getAll());
    }

    @DisplayName("возвращать список книг по имени жанра")
    @Test
    void shouldReturnBooksByGenreName() {
        when(bookRepository.findAllByGenresName(GENRE_1_NAME)).thenReturn(getAllBooksByGenreName(GENRE_1_NAME));

        assertEquals(BookConverter.toDto(getAllBooksByGenreName(GENRE_1_NAME)), bookService.getAllByGenreName(GENRE_1_NAME));
    }

    @DisplayName("возвращать список книг по имени автора")
    @Test
    void shouldReturnBooksByAuthorName() {
        when(bookRepository.findAllByAuthorName(AUTHOR_1_NAME)).thenReturn(getAllBooksByAuthorName(AUTHOR_1_NAME));

        assertEquals(BookConverter.toDto(getAllBooksByAuthorName(AUTHOR_1_NAME)), bookService.getAllByAuthorName(AUTHOR_1_NAME));
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