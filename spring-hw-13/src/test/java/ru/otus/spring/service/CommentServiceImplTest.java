package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.spring.converter.CommentConverter;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.UserAccountRepository;
import ru.otus.spring.security.acl.AclServiceWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.otus.spring.util.DataProducer.*;

@DisplayName("Сервис работы с комментариями должен")
@SpringBootTest
@Import(CommentServiceImpl.class)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private AclServiceWrapper aclServiceWrapper;

    private static final String BOOK_1_ID = "1";
    private static final String COMMENT_1_ID = "1";
    private static final String COMMENT_9_ID = "9";
    private static final String BOOK_1_NAME = "Book 1";
    private static final String COMMENT_9_TEXT = "Comment 9";
    private static final String COMMENT_1_TEXT_UPDATED = "Comment 1 updated";
    private static final String USER_1_NAME = "user1";

    @DisplayName("возвращать комментарии по идентификатору книги")
    @Test
    void shouldReturnAllCommentsByBookId() throws BookNotFoundEx {
        when(bookRepository.existsById(BOOK_1_ID)).thenReturn(true);
        when(commentRepository.findAllByBookId(BOOK_1_ID)).thenReturn(getAllCommentsByBookId(BOOK_1_ID));

        assertThat(commentService.getAllByBookId(BOOK_1_ID))
                .hasSameElementsAs(CommentConverter.toDto(getAllCommentsByBookId(BOOK_1_ID)));
    }

    @DisplayName("добавлять и возвращать комментарий по имени книги")
    @Test
    @WithMockUser(username = "user")
    void shouldAddAndReturnCommentByBookName() throws BookNotFoundEx {
        Comment comment = new Comment(COMMENT_9_ID, getBookByName(BOOK_1_NAME).orElseThrow(), COMMENT_9_TEXT,
                getUserByName(USER_1_NAME).orElseThrow());
        when(bookRepository.findById(BOOK_1_ID)).thenReturn(getBookById(BOOK_1_ID));
        when(commentRepository.save(any())).thenReturn(comment);

        assertEquals(comment.toDto(), commentService.addByBookId(BOOK_1_ID, COMMENT_9_TEXT));
    }

    @DisplayName("изменять и возвращать комментарий по идентификатору")
    @Test
    void shouldUpdateAndReturnCommentById() throws CommentNotFoundEx {
        Comment comment = getCommentById(COMMENT_1_ID).orElseThrow();
        comment.setText(COMMENT_1_TEXT_UPDATED);
        when(commentRepository.findById(COMMENT_1_ID)).thenReturn(getCommentById(COMMENT_1_ID));

        assertEquals(comment.toDtoWithBookName(), commentService.updateById(COMMENT_1_ID, COMMENT_1_TEXT_UPDATED));
    }

    @DisplayName("удалять комментарий по идентификатору")
    @Test
    void shouldDeleteCommentByID() throws CommentNotFoundEx {
        when(commentRepository.findById(COMMENT_1_ID)).thenReturn(getCommentById(COMMENT_1_ID));

        commentService.deleteById(COMMENT_1_ID);

        verify(commentRepository, times(1)).delete(getCommentById(COMMENT_1_ID).orElseThrow());
    }
}