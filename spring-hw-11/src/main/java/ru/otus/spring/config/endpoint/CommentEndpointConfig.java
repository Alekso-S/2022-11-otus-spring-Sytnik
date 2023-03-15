package ru.otus.spring.config.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.rest.CommentRestDto;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.repository.ReactiveBookRepository;
import ru.otus.spring.repository.ReactiveCommentRepository;

import java.util.Comparator;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class CommentEndpointConfig {

    private final ReactiveBookRepository bookRepository;
    private final ReactiveCommentRepository commentRepository;

    @Bean
    public RouterFunction<ServerResponse> commentRouterFunction() {
        return route()
                .GET("/api/comments", queryParam("bookId", s -> true), this::getCommentsByBookId)
                .POST("/api/comments", this::addComment)
                .PUT("/api/comments", this::updateComment)
                .DELETE("/api/comments/{id}", this::deleteComment)
                .build();
    }

    private Mono<ServerResponse> getCommentsByBookId(ServerRequest request) {
        Flux<CommentDto> comments = Mono.justOrEmpty(request.queryParam("bookId"))
                .flatMap(bookId -> bookRepository.existsById(bookId)
                        .filter(Boolean::booleanValue)
                        .switchIfEmpty(Mono.error(new BookNotFoundEx("id", bookId)))
                        .then(Mono.just(bookId)))
                .flatMapMany(commentRepository::findAllByBookId)
                .map(Comment::toDto)
                .sort(Comparator.comparing(CommentDto::getId));
        return ok().body(comments, CommentDto.class);
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
}

