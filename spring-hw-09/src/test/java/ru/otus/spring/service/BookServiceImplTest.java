package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.repository.BookRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.otus.spring.util.DataProducer.*;

@SuppressWarnings("SameParameterValue")
@DisplayName("Сервис работы с книгами должен")
@SpringBootTest
@Import(BookServiceImpl.class)
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @MockBean
    private BookRepository repository;

    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String GENRE_1_NAME = "Genre 1";

    @BeforeEach
    void setUp() {
        when(repository.count()).thenReturn(Long.valueOf(getAllBooks().size()));
        when(repository.findAll()).thenReturn(getAllBooks());
        when(repository.findAllByGenresName(GENRE_1_NAME)).thenReturn(getAllBooksByGenreName(GENRE_1_NAME));
        when(repository.findAllByAuthorName(AUTHOR_1_NAME)).thenReturn(getAllBooksByAuthorName(AUTHOR_1_NAME));
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldShowBookCount() {
        assertEquals(getAllBooks().size(), service.count());
    }

    @DisplayName("возвращать список всех книг")
    @Test
    void shouldShowAllBooks() {
        List<BookDto> books = getAllBooks().stream()
                .map(BookDto::new)
                .sorted(Comparator.comparing(BookDto::getName))
                .collect(Collectors.toList());
        assertEquals(books, service.getAll());
    }

    @DisplayName("возвращать список книг по имени жанра")
    @Test
    void shouldShowBooksByGenreName() {
        List<BookDto> books = getAllBooksByGenreName(GENRE_1_NAME).stream()
                .map(BookDto::new)
                .sorted(Comparator.comparing(BookDto::getName))
                .collect(Collectors.toList());
        assertEquals(books, service.getAllByGenreName(GENRE_1_NAME));
    }

    @DisplayName("возвращать список книг по имени автора")
    @Test
    void shouldShowBooksByAuthorName() {
        List<BookDto> books = getAllBooksByAuthorName(AUTHOR_1_NAME).stream()
                .map(BookDto::new)
                .sorted(Comparator.comparing(BookDto::getName))
                .collect(Collectors.toList());
        assertEquals(books, service.getAllByAuthorName(AUTHOR_1_NAME));
    }
}