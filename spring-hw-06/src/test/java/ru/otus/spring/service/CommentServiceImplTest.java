package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.converter.CommentConverter;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.util.DataProducer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SuppressWarnings({"FieldCanBeLocal", "SameParameterValue"})
@DisplayName("Сервис работы с комментариями должен")
@SpringBootTest
@Import(CommentServiceImpl.class)
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private BookRepository bookRepository;

    private final long BOOK_1_ID = 1;
    private final String BOOK_1_NAME = "Book 1";
    private final long COMMENT_1_ID = 1;
    private static final String SUCCESSFUL_ADD_MESSAGE = "Comment added";
    private final String COMMENT_9_TEXT = "Comment 9 text";
    private static final String SUCCESSFUL_UPD_MESSAGE = "Comment updated";
    private final String COMMENT_1_TEXT_UPD = "Comment 1 text updated";
    private static final String SUCCESSFUL_DEL_MESSAGE = "Comment deleted";

    @BeforeEach
    void setUp() throws CommentNotFoundEx {
        when(bookRepository.checkExistenceById(BOOK_1_ID)).thenReturn(true);
        when(commentRepository.getCountByBookId(BOOK_1_ID)).thenReturn(Long.valueOf(getByBookId(BOOK_1_ID).size()));
        when(bookRepository.checkExistenceByName(BOOK_1_NAME)).thenReturn(true);
        when(commentRepository.getCountByBookName(BOOK_1_NAME)).thenReturn(Long.valueOf(getByBookName(BOOK_1_NAME).size()));
        when(commentRepository.getById(COMMENT_1_ID)).thenReturn(getById(COMMENT_1_ID));
        when(commentRepository.getByBookId(BOOK_1_ID)).thenReturn(getByBookId(BOOK_1_ID));
        when(commentRepository.getByBookName(BOOK_1_NAME)).thenReturn(getByBookName(BOOK_1_NAME));
    }

    @DisplayName("возвращать число комментариев по идентификатору книги")
    @Test
    void shouldShowCountByBookId() {
        assertEquals(String.valueOf(getByBookId(BOOK_1_ID).size()), commentService.showCountByBookId(BOOK_1_ID));
    }

    @DisplayName("возвращать число комментариев по имени книги")
    @Test
    void shouldShowCountByBookName() {
        assertEquals(String.valueOf(getByBookName(BOOK_1_NAME).size()), commentService.showCountByBookName(BOOK_1_NAME));
    }

    @DisplayName("возвращать комментарий по идентификатору")
    @Test
    void shouldShowById() {
        assertEquals(CommentConverter.toString(getById(COMMENT_1_ID)), commentService.showById(COMMENT_1_ID));
    }

    @DisplayName("возвращать комментарии по идентификатору книги")
    @Test
    void shouldShowByBookId() {
        assertEquals(CommentConverter.toString(getByBookId(BOOK_1_ID)), commentService.showByBookId(BOOK_1_ID));
    }

    @DisplayName("возвращать комментарии по имени книги")
    @Test
    void shouldShowByBookName() {
        assertEquals(CommentConverter.toString(getByBookName(BOOK_1_NAME)), commentService.showByBookName(BOOK_1_NAME));
    }

    @DisplayName("выводить сообщение об успешном добавлении комментария")
    @Test
    void shouldAddByBookName() {
        assertEquals(SUCCESSFUL_ADD_MESSAGE, commentService.addByBookName(BOOK_1_NAME, COMMENT_9_TEXT));
    }

    @DisplayName("выводить сообщение об успешном обновлении комментария")
    @Test
    void shouldUpdById() {
        assertEquals(SUCCESSFUL_UPD_MESSAGE, commentService.updById(COMMENT_1_ID, COMMENT_1_TEXT_UPD));
    }

    @DisplayName("выводить сообщение об успешном удалении комментария")
    @Test
    void shouldDelById() {
        assertEquals(SUCCESSFUL_DEL_MESSAGE, commentService.delById(COMMENT_1_ID));
    }

    private List<Comment> getByBookId(long bookId) {
        return DataProducer.getCommentsByBookId(bookId);
    }

    private List<Comment> getByBookName(String bookName) {
        return DataProducer.getCommentsByBookName(bookName);
    }

    private Comment getById(long id) {
        return DataProducer.getCommentById(id);
    }
}