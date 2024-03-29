package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.util.DataProducer;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SuppressWarnings("SameParameterValue")
@DisplayName("Сервис работы с жанрами должен")
@SpringBootTest
@Import(GenreServiceImpl.class)
class GenreServiceImplTest {

    @Autowired
    private GenreServiceImpl genreService;

    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private BookRepository bookRepository;

    private final static long GENRE_1_ID = 1;
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String GENRE_5_NAME = "Genre 5";
    private final static long BOOK_1_ID = 1;
    private final static String BOOK_1_NAME = "Book 1";
    private final static String SUCCESSFUL_ADD_MESSAGE = "Genre added";
    private final static String SUCCESSFUL_DEL_MESSAGE = "Genre deleted";

    @BeforeEach
    void setUp() {
        when(genreRepository.count()).thenReturn(Long.valueOf(getAll().size()));
        when(genreRepository.findAll()).thenReturn(getAll());
        when(genreRepository.findById(GENRE_1_ID)).thenReturn(Optional.of(new Genre(GENRE_1_ID, GENRE_1_NAME)));
        when(genreRepository.findByName(GENRE_1_NAME)).thenReturn(Optional.of(new Genre(GENRE_1_ID, GENRE_1_NAME)));
        when(bookRepository.findById(BOOK_1_ID)).thenReturn(getBookById(BOOK_1_ID));
        when(bookRepository.existsById(BOOK_1_ID)).thenReturn(true);
        when(bookRepository.existsByName(BOOK_1_NAME)).thenReturn(true);
        when(bookRepository.findByName(BOOK_1_NAME)).thenReturn(getBookById(BOOK_1_ID));
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldShowGenresCount() {
        assertEquals(getAll().size(), genreService.showCount());
    }

    @DisplayName("возвращать список всех жанров")
    @Test
    void showAllGenres() {
        String genresString = getAll().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .map(GenreConverter::toString)
                .collect(Collectors.joining("\n"));
        assertEquals(genresString, genreService.showAll());
    }

    @DisplayName("возвращать жанр по идентификатору")
    @Test
    void shouldShowGenreById() {
        assertEquals(GenreConverter.toString(new Genre(GENRE_1_ID, GENRE_1_NAME)),
                genreService.showById(GENRE_1_ID));
    }

    @DisplayName("возвращать жанр по имени")
    @Test
    void shouldShowGenreByName() {
        assertEquals(GenreConverter.toString(new Genre(GENRE_1_ID, GENRE_1_NAME)),
                genreService.showByName(GENRE_1_NAME));
    }

    @DisplayName("возвращать список жанров по идентификатору книги")
    @Test
    void shouldShowGenresByBookId() {
        String genresString = getByBookId(BOOK_1_ID).stream()
                .sorted(Comparator.comparing(Genre::getId))
                .map(GenreConverter::toString)
                .collect(Collectors.joining("\n"));
        assertEquals(genresString, genreService.showAllByBookId(BOOK_1_ID));
    }

    @DisplayName("возвращать список жанров по имени книги")
    @Test
    void shouldShowGenresByBookName() {
        String genresString = getByBookId(BOOK_1_ID).stream()
                .sorted(Comparator.comparing(Genre::getId))
                .map(GenreConverter::toString)
                .collect(Collectors.joining("\n"));
        assertEquals(genresString, genreService.showAllByBookName(BOOK_1_NAME));
    }

    @DisplayName("выводить сообщение об успешном добавлении жанра")
    @Test
    void shouldShowAddGenreMessage() {
        assertEquals(SUCCESSFUL_ADD_MESSAGE, genreService.add(GENRE_5_NAME));
    }

    @DisplayName("выводить сообщение об успешном удалении жанра")
    @Test
    void shouldShowDelGenreMessage() {
        assertEquals(SUCCESSFUL_DEL_MESSAGE, genreService.deleteByName(GENRE_1_NAME));
    }

    private List<Genre> getAll() {
        return DataProducer.getAllGenres();
    }

    private List<Genre> getByBookId(long bookId) {
        return DataProducer.getAllGenresByBookId(bookId);
    }

    private Optional<Book> getBookById(long bookId) {
        return DataProducer.getBookById(bookId);
    }
}