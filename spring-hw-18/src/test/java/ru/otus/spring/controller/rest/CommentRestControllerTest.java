package ru.otus.spring.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.converter.CommentConverter;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.rest.CommentRestDto;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.util.DataProducer;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.util.DataProducer.getBookById;

@WebMvcTest(CommentRestController.class)
@DisplayName("REST контроллер комментариев должен")
class CommentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private final static String BOOK_1_ID = "1";
    private static final String COMMENT_9_TEXT = "Comment 9";
    private static final String COMMENT_9_ID = "9";
    private static final String COMMENT_1_ID = "1";
    private static final String COMMENT_1_TEXT_UPDATED = "Comment 1 text updated";

    @Test
    @DisplayName("возвращать комментарии по идентификатору книги")
    void shouldGetAllByBookId() throws Exception {
        List<CommentDto> comments = CommentConverter.toDto(DataProducer.getAllCommentsByBookId(BOOK_1_ID));
        when(commentService.getAllByBookId(BOOK_1_ID)).thenReturn(comments);

        mockMvc.perform(get("/api/comments?bookId={bookId}", BOOK_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(comments)));
    }

    @Test
    @DisplayName("добавлять и возвращать комментарий")
    void shouldAddAndReturn() throws Exception {
        CommentRestDto commentRestDto = new CommentRestDto(null, BOOK_1_ID, COMMENT_9_TEXT);
        CommentDto commentDto = new CommentDto(
                new Comment(COMMENT_9_ID, getBookById(BOOK_1_ID).orElseThrow(), COMMENT_9_TEXT));
        when(commentService.addByBookId(BOOK_1_ID, COMMENT_9_TEXT)).thenReturn(commentDto);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentDto)));
    }

    @Test
    @DisplayName("изменять комментарий")
    void shouldUpdate() throws Exception {
        CommentRestDto commentRestDto = new CommentRestDto(COMMENT_1_ID, null, COMMENT_1_TEXT_UPDATED);

        mockMvc.perform(put("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRestDto)))
                .andExpect(status().isOk());

        verify(commentService, times(1)).updateById(COMMENT_1_ID, COMMENT_1_TEXT_UPDATED);
    }

    @Test
    @DisplayName("удалять комментарий")
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/api/comments/{commentId}", COMMENT_1_ID))
                .andExpect(status().isOk());

        verify(commentService, times(1)).deleteById(COMMENT_1_ID);
    }
}