package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
    public String showCountByBookId(String bookId) {
        if (!bookRepository.existsById(bookId)) {
            logger.warn("Book with id={} was not found", bookId);
            return "Book not found";
        }
        return String.valueOf(commentRepository.countByBookId(bookId));
    }

    @Override
    public String showCountByBookName(String bookName) {
        if (!bookRepository.existsByName(bookName)) {
            logger.warn("Book with name={} was not found", bookName);
            return "Book not found";
        }
        return String.valueOf(commentRepository.countByBookName(bookName));
    }

    @Override
    public String showById(String id) {
        try {
            return CommentConverter.toString(commentRepository.findById(id).orElseThrow(CommentNotFoundEx::new));
        } catch (CommentNotFoundEx e) {
            logger.warn("Comment with id={} was not found", id);
            return "Comment not found";
        }
    }

    @Override
    public String showAllByBookId(String bookId) {
        if (!bookRepository.existsById(bookId)) {
            logger.warn("Book with id={} was not found", bookId);
            return "Book not found";
        }
        return CommentConverter.toString(commentRepository.findAllByBookId(bookId));
    }

    @Override
    public String showAllByBookName(String bookName) {
        if (!bookRepository.existsByName(bookName)) {
            logger.warn("Book with name={} was not found", bookName);
            return "Book not found";
        }
        return CommentConverter.toString(commentRepository.findAllByBookName(bookName));
    }

    @Override
    public String addByBookName(String bookName, String text) {
        Book book;
        try {
            book = bookRepository.findByName(bookName).orElseThrow(BookNotFoundEx::new);
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} was not found", bookName);
            return "Book not found";
        }
        commentRepository.save(new Comment(book, text));
        return "Comment added";
    }

    @Override
    public String updateById(String id, String text) {
        Comment comment;
        try {
            comment = commentRepository.findById(id).orElseThrow(CommentNotFoundEx::new);
        } catch (CommentNotFoundEx e) {
            logger.warn("Comment with id={} not found", id);
            return "Comment not found";
        }
        comment.setText(text);
        commentRepository.save(comment);
        return "Comment updated";
    }

    @Override
    public String deleteById(String id) {
        Comment comment;
        try {
            comment = commentRepository.findById(id).orElseThrow(CommentNotFoundEx::new);
        } catch (CommentNotFoundEx e) {
            logger.warn("Comment with id={} not found", id);
            return "Comment not found";
        }
        commentRepository.delete(comment);
        return "Comment deleted";
    }
}
