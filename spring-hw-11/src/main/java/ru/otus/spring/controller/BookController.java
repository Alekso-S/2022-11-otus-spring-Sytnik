package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.exception.BookAlreadyExistsEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.repository.ReactiveAuthorRepository;
import ru.otus.spring.repository.ReactiveBookRepository;
import ru.otus.spring.repository.ReactiveGenreRepository;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final ReactiveAuthorRepository authorRepository;
    private final ReactiveGenreRepository genreRepository;
    private final ReactiveBookRepository bookRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @GetMapping("/books")
    public String booksPage(Model model) {
        addCounts(model);
        return "books";
    }

    @GetMapping(value = "/books", params = "author")
    public String booksPageByAuthor(Model model, @RequestParam("author") String authorName) {
        model.addAttribute("authorName", authorName);
        addCounts(model);
        return "books";
    }

    @GetMapping(value = "/books", params = "genre")
    public String booksPageByGenre(Model model, @RequestParam("genre") String genreName) {
        model.addAttribute("genreName", genreName);
        addCounts(model);
        return "books";
    }

    private void addCounts(Model model) {
        model.addAttribute("booksCount", bookRepository.count());
        model.addAttribute("authorsCount", authorRepository.count());
        model.addAttribute("genresCount", genreRepository.count());
    }

    @ExceptionHandler({BookNotFoundEx.class, BookAlreadyExistsEx.class})
    private ResponseEntity<String> handleNotFound(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
