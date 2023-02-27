package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.otus.spring.util.DataProducer.getAllCommentsByBookName;
import static ru.otus.spring.util.DataProducer.getCommentById;

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

    private static final String BOOK_1_NAME = "Book 1";
    private static final String COMMENT_1_ID = "1";

    @BeforeEach
    void setUp() {
        when(bookRepository.existsByName(BOOK_1_NAME)).thenReturn(true);
        when(commentRepository.findAllByBookName(BOOK_1_NAME)).thenReturn(getAllCommentsByBookName(BOOK_1_NAME));
        when(commentRepository.findById(COMMENT_1_ID)).thenReturn(getCommentById(COMMENT_1_ID));
    }

    @DisplayName("возвращать комментарий по идентификатору")
    @Test
    void shouldGetById() throws CommentNotFoundEx {
        assertEquals(getCommentById(COMMENT_1_ID)
                .orElseThrow(CommentNotFoundEx::new).toDtoWithBookName(), commentService.getById(COMMENT_1_ID));
    }

    @DisplayName("возвращать комментарии по имени книги")
    @Test
    void shouldGetAllByBookName() throws BookNotFoundEx {
        List<CommentDto> comments = getAllCommentsByBookName(BOOK_1_NAME).stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
        assertThat(commentService.getAllByBookName(BOOK_1_NAME)).hasSameElementsAs(comments);
    }
}