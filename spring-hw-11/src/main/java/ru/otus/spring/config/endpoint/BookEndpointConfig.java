package ru.otus.spring.config.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.rest.BookRestDto;
import ru.otus.spring.exception.BookAlreadyExistsEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.repository.ReactiveBookRepository;

import java.util.Comparator;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class BookEndpointConfig {

    private final ReactiveBookRepository bookRepository;

    @Bean
    public RouterFunction<ServerResponse> bookRouterFunction() {
        return route()
                .GET("/api/books", queryParam("author", s -> true), this::getBookByAuthorName)
                .GET("/api/books", queryParam("genre", s -> true), this::getBookByGenreName)
                .GET("/api/books", this::getAllBooks)
                .POST("/api/books", this::addBook)
                .DELETE("/api/books/{id}", this::deleteBook)
                .build();
    }

    private Mono<ServerResponse> getBookByAuthorName(ServerRequest request) {
        Flux<BookDto> books = Mono.justOrEmpty(request.queryParam("author"))
                .flatMapMany(bookRepository::findAllByAuthorName)
                .map(Book::toDto)
                .sort(Comparator.comparing(BookDto::getName));
        return ok().body(books, BookDto.class);
    }

    private Mono<ServerResponse> getBookByGenreName(ServerRequest request) {
        Flux<BookDto> books = Mono.justOrEmpty(request.queryParam("genre"))
                .flatMapMany(bookRepository::findAllByGenresName)
                .map(Book::toDto)
                .sort(Comparator.comparing(BookDto::getName));
        return ok().body(books, BookDto.class);
    }

    private Mono<ServerResponse> getAllBooks(ServerRequest request) {
        Flux<BookDto> books = bookRepository.findAll()
                .map(Book::toDto)
                .sort(Comparator.comparing(BookDto::getName));
        return ok().body(books, BookDto.class);
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
}

