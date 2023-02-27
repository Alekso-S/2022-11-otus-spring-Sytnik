package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @MockBean
    private CommentService commentService;

    private static final String BOOK_1_NAME = "Book 1";
    private static final String COMMENT_1_ID = "1";
    private static final String COMMENT_9_TEXT = "Comment 9";
    private static final String COMMENT_1_TEXT_UPDATED = "Comment 1 text updated";

    @BeforeEach
    void setUp() {
        when(bookService.count()).thenReturn(getBooksCount());
        when(authorService.count()).thenReturn(getAuthorsCount());
        when(genreService.count()).thenReturn(getGenresCount());
    }

    @Test
    @DisplayName("возвращать страницу с комментариями выбранной книги")
    void shouldReturnCommentsPage() throws Exception {
        List<CommentDto> comments = getAllCommentsByBookName(BOOK_1_NAME).stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
        when(commentService.getAllByBookName(BOOK_1_NAME)).thenReturn(comments);

        mockMvc.perform(get("/comments?book={book}", BOOK_1_NAME))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comments", "booksCount", "authorsCount", "genresCount"))
                .andExpect(model().attribute("comments", comments))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(view().name("comments"));
    }

    @Test
    @DisplayName("возвращать страницу добавления комментария")
    void shouldReturnAddPage() throws Exception {
        mockMvc.perform(get("/comments/add?book={book}", BOOK_1_NAME))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("booksCount", "authorsCount", "genresCount"))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(view().name("comment-add"));
    }

    @Test
    @DisplayName("возвращать страницу изменения комментария")
    void shouldReturnEditPage() throws Exception {
        when(commentService.getById(COMMENT_1_ID)).thenReturn(getCommentById(COMMENT_1_ID)
                .orElseThrow(CommentNotFoundEx::new).toDtoWithBookName());

        mockMvc.perform(get("/comments/edit?id={id}", COMMENT_1_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("booksCount", "authorsCount", "genresCount", "comment"))
                .andExpect(model().attribute("booksCount", getBooksCount()))
                .andExpect(model().attribute("authorsCount", getAuthorsCount()))
                .andExpect(model().attribute("genresCount", getGenresCount()))
                .andExpect(model().attribute("comment", getCommentById(COMMENT_1_ID).
                        orElseThrow(CommentNotFoundEx::new).toDtoWithBookName()))
                .andExpect(view().name("comment-edit"));
    }

    @Test
    @DisplayName("выполнять отмену и редирект")
    void shouldCancelAndRedirect() throws Exception {
        mockMvc.perform(get("/comments/cancel?book={book}", BOOK_1_NAME))
                .andExpect(redirectedUrlTemplate("/comments?book={book}", BOOK_1_NAME));
    }

    @Test
    @DisplayName("добавлять комментарий и выполнять редирект")
    void shouldAddAndRedirect() throws Exception {
        mockMvc.perform(post("/comments/add?book={book}&text={text}", BOOK_1_NAME, COMMENT_9_TEXT))
                .andExpect(redirectedUrlTemplate("/comments?book={book}", BOOK_1_NAME));
        verify(commentService, times(1)).addByBookName(BOOK_1_NAME, COMMENT_9_TEXT);
    }

    @Test
    @DisplayName("изменять комментарий и выполнять редирект")
    void shouldEditAndRedirect() throws Exception {
        when(commentService.updateById(COMMENT_1_ID, COMMENT_1_TEXT_UPDATED)).thenReturn(getCommentById(COMMENT_1_ID)
                .orElseThrow(CommentNotFoundEx::new)
                .toDtoWithBookName());

        mockMvc.perform(post("/comments/edit?id={id}&text={text}", COMMENT_1_ID, COMMENT_1_TEXT_UPDATED))
                .andExpect(redirectedUrlTemplate("/comments?book={book}", BOOK_1_NAME));
        verify(commentService, times(1)).updateById(COMMENT_1_ID, COMMENT_1_TEXT_UPDATED);
    }

    @Test
    @DisplayName("удалять комментарий и выполнять редирект")
    void shouldDeleteAndRedirect() throws Exception {
        when(commentService.deleteById(COMMENT_1_ID)).thenReturn(getCommentById(COMMENT_1_ID)
                .orElseThrow(CommentNotFoundEx::new)
                .toDtoWithBookName());

        mockMvc.perform(post("/comments/delete?id={id}", COMMENT_1_ID))
                .andExpect(redirectedUrlTemplate("/comments?book={book}", BOOK_1_NAME));
        verify(commentService, times(1)).deleteById(COMMENT_1_ID);
    }
}