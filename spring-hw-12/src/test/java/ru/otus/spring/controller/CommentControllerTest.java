package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.util.DataProducer;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.otus.spring.util.DataProducer.*;

@WebMvcTest(CommentController.class)
@DisplayName("Контроллер комментариев должен")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    private static final String BOOK_1_NAME = "Book 1";
    private static final String BOOK_1_ID = "1";

    @BeforeEach
    void setUp() {
        when(bookService.count()).thenReturn(getBooksCount());
        when(authorService.count()).thenReturn(getAuthorsCount());
        when(genreService.count()).thenReturn(getGenresCount());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("возвращать страницу с комментариями выбранной книги")
    void shouldReturnCommentsPage() throws Exception {
        when(bookService.getById(BOOK_1_ID)).thenReturn(DataProducer.getBookById(BOOK_1_ID).orElseThrow().toDto());

        mockMvc.perform(get("/comments?bookId={bookId}", BOOK_1_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("booksCount", "authorsCount", "genresCount",
                        "bookId", "bookName"))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(model().attribute("bookId", BOOK_1_ID))
                .andExpect(model().attribute("bookName", BOOK_1_NAME))
                .andExpect(model().attribute("username", "user"))
                .andExpect(view().name("comments"));
    }
}