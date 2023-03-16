package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.security.SecurityConfig;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.otus.spring.util.DataProducer.*;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
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

    @BeforeEach
    void setUp() {
        when(bookService.count()).thenReturn(getBooksCount());
        when(authorService.count()).thenReturn(getAuthorsCount());
        when(genreService.count()).thenReturn(getGenresCount());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("возвращать страницу со всеми книгами")
    void shouldReturnBooksPage() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("booksCount", "authorsCount", "genresCount"))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(model().attribute("username", "user"))
                .andExpect(view().name("books"));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("возвращать страницу с книгами выбранного автора")
    void shouldReturnBooksPageByAuthor() throws Exception {
        mockMvc.perform(get("/books?author={author}", AUTHOR_1_NAME))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("booksCount", "authorsCount", "genresCount", "authorName"))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(model().attribute("authorName", AUTHOR_1_NAME))
                .andExpect(model().attribute("username", "user"))
                .andExpect(view().name("books"));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("возвращать страницу с книгами выбранного жанра")
    void shouldReturnBooksPageByGenre() throws Exception {
        mockMvc.perform(get("/books?genre={genre}", GENRE_1_NAME))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("booksCount", "authorsCount", "genresCount", "genreName"))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(model().attribute("genreName", GENRE_1_NAME))
                .andExpect(model().attribute("username", "user"))
                .andExpect(view().name("books"));
    }
}