package ru.otus.spring.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.security.SecurityConfig;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.GenreService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import(SecurityConfig.class)
@DisplayName("Контроллеры должны")
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private CommentService commentService;

    private final static String BOOK_1_ID = "1";

    @ParameterizedTest
    @ValueSource(strings = {"/authors", "/books", "/comments", "/genres",
            "/api/authors", "/api/books", "/api/comments", "/api/genres"})
    @DisplayName("запрещать неаутентифицированный доступ по GET и перенаправлять на страницу логина")
    void shouldDenyUnauthorizedGetAccess(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http://*/login"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/books", "/api/comments"})
    @DisplayName("запрещать неаутентифицированный доступ по POST и перенаправлять на страницу логина")
    void shouldDenyUnauthorizedPostAccess(String url) throws Exception {
        mockMvc.perform(post(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http://*/login"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/comments"})
    @DisplayName("запрещать неаутентифицированный доступ по PUT и перенаправлять на страницу логина")
    void shouldDenyUnauthorizedPutAccess(String url) throws Exception {
        mockMvc.perform(put(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http://*/login"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/books", "/api/comments"})
    @DisplayName("запрещать неаутентифицированный доступ по DELETE и перенаправлять на страницу логина")
    void shouldDenyUnauthorizedDeleteAccess(String url) throws Exception {
        mockMvc.perform(delete(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http://*/login"));
    }

    @Test
    @DisplayName("запрещать удаление книги пользователю без роли администратора")
    @WithMockUser(roles = "USER")
    void shouldDenyDeleteBookForUsers() throws Exception {
        mockMvc.perform(delete("/api/books/{book}", BOOK_1_ID))
                .andExpect(status().isForbidden());
    }
}
