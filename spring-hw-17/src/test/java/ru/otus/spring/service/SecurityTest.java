package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.UserAccountRepository;
import ru.otus.spring.security.acl.AclConfigTest;
import ru.otus.spring.security.acl.AclMethodSecurityConfiguration;
import ru.otus.spring.security.acl.AclServiceWrapper;
import ru.otus.spring.util.DataProducer;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import({
        AclConfigTest.class,
        CommentServiceImpl.class,
        AclMethodSecurityConfiguration.class})
@DisplayName("Механизм безопасности по ролям и ACL должен")
public class SecurityTest {

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

    private static final String COMMENT_1_ID = "1";
    private static final String COMMENT_1_TEXT_UPDATED = "Comment 1 text updated";

    @BeforeEach
    public void setUp() {
        when(commentRepository.findById(COMMENT_1_ID)).thenReturn(DataProducer.getCommentById(COMMENT_1_ID));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = "ADMIN")
    @DisplayName("разрешать админу удалять комментарий")
    public void shouldAllowDeleteCommentToAdmin() throws CommentNotFoundEx {
        commentService.deleteById(COMMENT_1_ID);

        verify(commentRepository, times(1)).delete(any());
    }

    @Test
    @WithMockUser(
            username = "user1",
            roles = "USER")
    @DisplayName("разрешать пользователю удалять свой комментарий")
    public void shouldAllowDeleteCommentToOwner() throws CommentNotFoundEx {
        commentService.deleteById(COMMENT_1_ID);

        verify(commentRepository, times(1)).delete(any());
    }

    @Test
    @WithMockUser(
            username = "user2",
            roles = "USER")
    @DisplayName("запрещать пользователю удалять чужой комментарий")
    public void shouldDenyDeleteCommentToAnyUser() {
        assertThrowsExactly(AccessDeniedException.class, () -> commentService.deleteById(COMMENT_1_ID));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = "ADMIN")
    @DisplayName("разрешать админу изменять комментарий")
    public void shouldAllowEditCommentToAdmin() throws CommentNotFoundEx {
        commentService.updateById(COMMENT_1_ID, COMMENT_1_TEXT_UPDATED);

        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @WithMockUser(
            username = "user1",
            roles = "USER")
    @DisplayName("разрешать пользователю изменять свой комментарий")
    public void shouldAllowEditCommentToOwner() throws CommentNotFoundEx {
        commentService.updateById(COMMENT_1_ID, COMMENT_1_TEXT_UPDATED);

        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @WithMockUser(
            username = "user2",
            roles = "USER")
    @DisplayName("запрещать пользователю изменять чужой комментарий")
    public void shouldDenyEditCommentToAnyUser() {
        assertThrowsExactly(AccessDeniedException.class,
                () -> commentService.updateById(COMMENT_1_ID, COMMENT_1_TEXT_UPDATED));
    }
}
