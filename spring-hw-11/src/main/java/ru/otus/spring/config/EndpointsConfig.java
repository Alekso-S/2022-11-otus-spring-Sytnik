package ru.otus.spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.dto.rest.BookRestDto;
import ru.otus.spring.dto.rest.CommentRestDto;
import ru.otus.spring.exception.BookAlreadyExistsEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.repository.ReactiveAuthorRepository;
import ru.otus.spring.repository.ReactiveBookRepository;
import ru.otus.spring.repository.ReactiveCommentRepository;
import ru.otus.spring.repository.ReactiveGenreRepository;

import java.util.Comparator;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class EndpointsConfig {

    private final ReactiveAuthorRepository authorRepository;
    private final ReactiveBookRepository bookRepository;
    private final ReactiveCommentRepository commentRepository;
    private final ReactiveGenreRepository genreRepository;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route()
                .GET("/api/authors", this::getAllAuthors)
                .GET("/api/books", queryParam("author", s -> true), this::getBookByAuthorName)
                .GET("/api/books", queryParam("genre", s -> true), this::getBookByGenreName)
                .GET("/api/books", this::getAllBooks)
                .POST("/api/books", this::addBook)
                .DELETE("/api/books/{id}", this::deleteBook)
                .GET("/api/comments", queryParam("bookId", s -> true), this::getCommentsByBookId)
                .POST("/api/comments", this::addComment)
                .PUT("/api/comments", this::updateComment)
                .DELETE("/api/comments/{id}", this::deleteComment)
                .GET("/api/genres", this::getAllGenres)
                .build();
    }

    public Mono<ServerResponse> getAllAuthors(ServerRequest request) {
        return authorRepository.findAll()
                .map(Author::toDto)
                .sort(Comparator.comparing(AuthorDto::getName))
                .collectList()
                .flatMap(authors -> ok().body(fromValue(authors)));
    }

    private Mono<ServerResponse> getBookByAuthorName(ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam("author"))
                .flatMapMany(bookRepository::findAllByAuthorName)
                .map(Book::toDto)
                .sort(Comparator.comparing(BookDto::getName))
                .collectList()
                .flatMap(books -> ok().body(fromValue(books)));
    }

    private Mono<ServerResponse> getBookByGenreName(ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam("genre"))
                .flatMapMany(bookRepository::findAllByGenresName)
                .map(Book::toDto)
                .sort(Comparator.comparing(BookDto::getName))
                .collectList()
                .flatMap(books -> ok().body(fromValue(books)));
    }

    private Mono<ServerResponse> getAllBooks(ServerRequest request) {
        return bookRepository.findAll()
                .map(Book::toDto)
                .sort(Comparator.comparing(BookDto::getName))
                .collectList()
                .flatMap(books -> ok().body(fromValue(books)));
    }

    private Mono<ServerResponse> addBook(ServerRequest request) {
        return request.bodyToMono(BookRestDto.class)
                .flatMap(book -> bookRepository.existsByName(book.getName())
                        .filter(exists -> !exists)
                        .switchIfEmpty(Mono.error(new BookAlreadyExistsEx(book.getName())))
                        .then(Mono.just(book)))
                .flatMap(book ->
                        bookRepository.insert(new Book(book.getName(), book.getAuthorName(), book.getGenreNames())))
                .map(Book::toDto)
                .flatMap(book -> ok().body(fromValue(book)));
    }

    private Mono<ServerResponse> deleteBook(ServerRequest request) {
        return bookRepository.findById(request.pathVariable("id"))
                .switchIfEmpty(Mono.error(new BookNotFoundEx("id", request.pathVariable("id"))))
                .flatMap(bookRepository::deleteWithComments)
                .then(ok().build());
    }

    private Mono<ServerResponse> getCommentsByBookId(ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam("bookId"))
                .flatMap(bookId -> bookRepository.existsById(bookId)
                        .filter(Boolean::booleanValue)
                        .switchIfEmpty(Mono.error(new BookNotFoundEx("id", bookId)))
                        .then(Mono.just(bookId)))
                .flatMapMany(commentRepository::findAllByBookId)
                .map(Comment::toDto)
                .sort(Comparator.comparing(CommentDto::getId))
                .collectList()
                .flatMap(comments -> ok().body(fromValue(comments)));
    }

    private Mono<ServerResponse> addComment(ServerRequest request) {
        return request.bodyToMono(CommentRestDto.class)
                .flatMap(comment -> bookRepository.findById(comment.getBookId())
                        .switchIfEmpty(Mono.error(new BookNotFoundEx("id", comment.getBookId())))
                        .flatMap(book -> Mono.just(new Comment(book, comment.getText()))))
                .flatMap(commentRepository::insert)
                .map(Comment::toDto)
                .flatMap(comment -> ok().body(fromValue(comment)));
    }

    private Mono<ServerResponse> updateComment(ServerRequest request) {
        return request.bodyToMono(CommentRestDto.class)
                .flatMap(comment -> commentRepository.findById(comment.getId())
                        .switchIfEmpty(Mono.error(new CommentNotFoundEx(comment.getId())))
                        .doOnNext(c -> c.setText(comment.getText()))
                        .flatMap(Mono::just))
                .flatMap(commentRepository::save)
                .then(ok().build());
    }

    private Mono<ServerResponse> deleteComment(ServerRequest request) {
        return commentRepository.findById(request.pathVariable("id"))
                .switchIfEmpty(Mono.error(new CommentNotFoundEx(request.pathVariable("id"))))
                .flatMap(commentRepository::delete)
                .then(ok().build());
    }

    private Mono<ServerResponse> getAllGenres(ServerRequest request) {
        return genreRepository.findAll()
                .map(Genre::toDto)
                .sort(Comparator.comparing(GenreDto::getName))
                .collectList()
                .flatMap(genres -> ok().body(fromValue(genres)));
    }
}

