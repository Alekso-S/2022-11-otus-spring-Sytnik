package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.CommentConverter;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.UserAccount;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.UserAccountRepository;
import ru.otus.spring.security.acl.AclServiceWrapper;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserAccountRepository userAccountRepository;
    private final AclServiceWrapper aclServiceWrapper;

    @Override
    public List<CommentDto> getAllByBookId(String bookId) throws BookNotFoundEx {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundEx("id", bookId);
        }
        return CommentConverter.toDto(commentRepository.findAllByBookId(bookId));
    }

    @Override
    @Transactional
    public CommentDto addByBookId(String bookId, String text) throws BookNotFoundEx {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundEx("id", bookId));
        UserAccount user = userAccountRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        Comment comment = commentRepository.save(new Comment(book, text, user));
        aclServiceWrapper.addCommentToAcl(comment);
        return comment.toDto();
    }

    @Override
    @PreAuthorize("hasPermission(#id,'ru.otus.spring.domain.Comment','WRITE') || hasRole('ADMIN')")
    public CommentDto updateById(String id, String text) throws CommentNotFoundEx {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundEx(id));
        comment.setText(text);
        commentRepository.save(comment);
        return comment.toDtoWithBookName();
    }

    @Override
    @PreAuthorize("hasPermission(#id,'ru.otus.spring.domain.Comment','DELETE') || hasRole('ADMIN')")
    public void deleteById(String id) throws CommentNotFoundEx {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundEx(id));
        commentRepository.delete(comment);
    }
}
