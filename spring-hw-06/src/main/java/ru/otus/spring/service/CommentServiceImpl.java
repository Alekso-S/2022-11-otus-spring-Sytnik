package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.converter.CommentConverter;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Override
    public String showCountByBookId(long bookId) {
        if (!bookRepository.checkExistenceById(bookId)) {
            logger.warn("Book with id={} was not found", bookId);
            return "Book not found";
        }
        return String.valueOf(commentRepository.getCountByBookId(bookId));
    }

    @Override
    public String showCountByBookName(String bookName) {
        if (!bookRepository.checkExistenceByName(bookName)) {
            logger.warn("Book with name={} was not found", bookName);
            return "Book not found";
        }
        return String.valueOf(commentRepository.getCountByBookName(bookName));
    }

    @Override
    public String showById(long id) {
        try {
            return CommentConverter.toString(commentRepository.getById(id));
        } catch (CommentNotFoundEx e) {
            logger.warn("Comment with id={} was not found", id);
            return "Comment not found";
        }
    }

    @Override
    public String showByBookId(long bookId) {
        if (!bookRepository.checkExistenceById(bookId)) {
            logger.warn("Book with id={} was not found", bookId);
            return "Book not found";
        }
        return CommentConverter.toString(commentRepository.getByBookId(bookId));
    }

    @Override
    public String showByBookName(String bookName) {
        if (!bookRepository.checkExistenceByName(bookName)) {
            logger.warn("Book with name={} was not found", bookName);
            return "Book not found";
        }
        return CommentConverter.toString(commentRepository.getByBookName(bookName));
    }

    @Override
    @Transactional
    public String addByBookName(String bookName, String text) {
        Book book;
        try {
            book = bookRepository.getByName(bookName);
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} was not found", bookName);
            return "Book not found";
        }
        commentRepository.add(new Comment(book, text));
        return "Comment added";
    }

    @Override
    @Transactional
    public String updById(long id, String text) {
        try {
            commentRepository.updById(id, text);
        } catch (CommentNotFoundEx e) {
            logger.warn("Comment with id={} not found", id);
            return "Comment not found";
        }
        return "Comment updated";
    }

    @Override
    @Transactional
    public String delById(long id) {
        Comment comment;
        try {
            comment = commentRepository.getById(id);
        } catch (CommentNotFoundEx e) {
            logger.warn("Comment with id={} not found", id);
            return "Comment not found";
        }
        commentRepository.del(comment);
        return "Comment deleted";
    }
}
