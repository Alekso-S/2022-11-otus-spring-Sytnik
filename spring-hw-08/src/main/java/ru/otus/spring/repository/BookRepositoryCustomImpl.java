package ru.otus.spring.repository;

import org.springframework.context.annotation.Lazy;
import ru.otus.spring.domain.Book;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public BookRepositoryCustomImpl(
            CommentRepository commentRepository,
            @Lazy BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void deleteWithComments(Book book) {
        commentRepository.deleteByBookName(book.getName());
        bookRepository.delete(book);
    }
}
