package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.model.jpa.JpaAuthor;
import ru.otus.spring.model.jpa.JpaBook;
import ru.otus.spring.model.jpa.JpaComment;
import ru.otus.spring.model.jpa.JpaGenre;
import ru.otus.spring.model.mongo.MongoBook;
import ru.otus.spring.model.mongo.MongoComment;
import ru.otus.spring.repository.jpa.JpaAuthorRepository;
import ru.otus.spring.repository.jpa.JpaBookRepository;
import ru.otus.spring.repository.jpa.JpaCommentRepository;
import ru.otus.spring.repository.jpa.JpaGenreRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaServiceImpl implements JpaService {

    private final JpaAuthorRepository jpaAuthorRepository;
    private final JpaGenreRepository jpaGenreRepository;
    private final JpaBookRepository jpaBookRepository;
    private final JpaCommentRepository jpaCommentRepository;

    @Override
    public void erase() {
        jpaCommentRepository.deleteAll();
    }

    @Override
    public JpaBook prepareJpaBook(MongoBook book) {
        return new JpaBook(
                book.getName(),
                jpaAuthorRepository.findByName(book.getAuthor().getName())
                        .orElse(new JpaAuthor(book.getAuthor().getName())),
                book.getGenres().stream()
                        .map(genre -> jpaGenreRepository.findByName(genre.getName())
                                .orElse(new JpaGenre(genre.getName())))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public JpaComment prepareJpaComment(MongoComment comment) {
        return new JpaComment(
                comment.getText(),
                jpaBookRepository.findByName(comment.getBook().getName())
                        .orElseThrow()
        );
    }
}
