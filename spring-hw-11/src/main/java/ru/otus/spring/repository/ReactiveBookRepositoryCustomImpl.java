package ru.otus.spring.repository;

import org.springframework.context.annotation.Lazy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;

public class ReactiveBookRepositoryCustomImpl implements ReactiveBookRepositoryCustom {

    private final ReactiveCommentRepository commentRepository;
    private final ReactiveBookRepository bookRepository;

    public ReactiveBookRepositoryCustomImpl(
            ReactiveCommentRepository commentRepository,
            @Lazy ReactiveBookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Mono<Void> deleteWithComments(Book book) {
        return Flux.merge(bookRepository.delete(book),
                        commentRepository.deleteByBookId(book.getId()))
                .then();
    }
}
