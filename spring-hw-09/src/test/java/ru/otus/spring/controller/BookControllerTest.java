package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.otus.spring.util.DataProducer.*;

@WebMvcTest(BookController.class)
@DisplayName("Контроллер книг должен")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String BOOK_5_NAME = "Book 5";

    @BeforeEach
    void setUp() {
        when(bookService.count()).thenReturn(getBooksCount());
        when(authorService.count()).thenReturn(getAuthorsCount());
        when(genreService.count()).thenReturn(getGenresCount());
    }

    @Test
    @DisplayName("возвращать страницу со всеми книгами")
    void shouldReturnBooksPage() throws Exception {
        List<BookDto> books = getAllBooks().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());

        when(bookService.getAll()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books", "booksCount", "authorsCount", "genresCount"))
                .andExpect(model().attribute("books", books))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(view().name("books"));
    }

    @Test
    @DisplayName("возвращать страницу с книгами выбранного автора")
    void shouldReturnBooksPageByAuthor() throws Exception {
        List<BookDto> books = getAllBooksByAuthorName(AUTHOR_1_NAME).stream()
                .map(BookDto::new)
                .collect(Collectors.toList());

        when(bookService.getAllByAuthorName(AUTHOR_1_NAME)).thenReturn(books);

        mockMvc.perform(get("/books?author={author}", AUTHOR_1_NAME))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books", "booksCount", "authorsCount", "genresCount"))
                .andExpect(model().attribute("books", books))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(view().name("books"));
    }

    @Test
    @DisplayName("возвращать страницу с книгами выбранного жанра")
    void shouldReturnBooksPageByGenre() throws Exception {
        List<BookDto> books = getAllBooksByGenreName(GENRE_1_NAME).stream()
                .map(BookDto::new)
                .collect(Collectors.toList());

        when(bookService.getAllByGenreName(GENRE_1_NAME)).thenReturn(books);

        mockMvc.perform(get("/books?genre={genre}", GENRE_1_NAME))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books", "booksCount", "authorsCount", "genresCount"))
                .andExpect(model().attribute("books", books))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(view().name("books"));
    }

    @Test
    @DisplayName("возвращать страницу добавления книги")
    void shouldReturnAddPage() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("booksCount", "authorsCount", "genresCount"))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(view().name("book-add"));
    }

    @Test
    @DisplayName("добавлять книгу и выполнять редирект")
    void shouldAddAndRedirect() throws Exception {
        mockMvc.perform(post("/books/add?name={name}&author={author}&genres={genres}",
                        BOOK_5_NAME, AUTHOR_1_NAME, GENRE_1_NAME))
                .andExpect(redirectedUrl("/books"));
        verify(bookService, times(1)).add(BOOK_5_NAME, AUTHOR_1_NAME, GENRE_1_NAME);
    }

    @Test
    @DisplayName("удалять книгу и выполнять редирект")
    void shouldDeleteAndRedirect() throws Exception {
        mockMvc.perform(post("/books/delete?name={name}", BOOK_5_NAME))
                .andExpect(redirectedUrl("/books"));
        verify(bookService, times(1)).deleteByName(BOOK_5_NAME);
    }
}