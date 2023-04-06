package ru.otus.spring.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.rest.BookRestDto;
import ru.otus.spring.exception.BookAlreadyExistsEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> getAll() {
        return bookService.getAll();
    }

    @GetMapping(value = "/api/books", params = "author")
    public List<BookDto> getAllByAuthorName(@RequestParam("author") String authorName) {
        return bookService.getAllByAuthorName(authorName);
    }

    @GetMapping(value = "/api/books", params = "genre")
    public List<BookDto> getAllByGenreName(@RequestParam("genre") String genreName) {
        return bookService.getAllByGenreName(genreName);
    }

    @PostMapping("/api/books")
    public BookDto add(@RequestBody BookRestDto book) throws BookAlreadyExistsEx {
        return bookService.add(book.getName(), book.getAuthorName(), book.getGenreNames());
    }

    @DeleteMapping("/api/books/{id}")
    public void delete(@PathVariable String id) throws BookNotFoundEx {
        bookService.deleteById(id);
    }
}
