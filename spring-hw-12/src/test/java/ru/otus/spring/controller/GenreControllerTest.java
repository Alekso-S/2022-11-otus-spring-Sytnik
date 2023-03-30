package ru.otus.spring.controller;

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

@WebMvcTest(GenreController.class)
@Import(SecurityConfig.class)
@DisplayName("Контроллер жанров должен")
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @Test
    @WithMockUser(username = "user")
    @DisplayName("возвращать страницу со всеми жанрами")
    void shouldReturnGenresPage() throws Exception {
        when(bookService.count()).thenReturn(getBooksCount());
        when(authorService.count()).thenReturn(getAuthorsCount());
        when(genreService.count()).thenReturn(getGenresCount());

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("booksCount", "authorsCount", "genresCount"))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(model().attribute("username", "user"))
                .andExpect(view().name("genres"));
    }
}