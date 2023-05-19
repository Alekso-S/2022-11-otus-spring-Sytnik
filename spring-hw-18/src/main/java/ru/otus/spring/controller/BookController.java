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
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/books")
    public String booksPage(Model model) {
        addAttributes(model);
        return "books";
    }

    @GetMapping(value = "/books", params = "author")
    public String booksPageByAuthor(Model model, @RequestParam("author") String authorName) {
        model.addAttribute("authorName", authorName);
        addAttributes(model);
        return "books";
    }

    @GetMapping(value = "/books", params = "genre")
    public String booksPageByGenre(Model model, @RequestParam("genre") String genreName) {
        model.addAttribute("genreName", genreName);
        addAttributes(model);
        return "books";
    }

    private void addAttributes(Model model) {
        model.addAttribute("booksCount", bookService.count());
        model.addAttribute("authorsCount", authorService.count());
        model.addAttribute("genresCount", genreService.count());
//        model.addAttribute("username",
//                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @ExceptionHandler({BookNotFoundEx.class, BookAlreadyExistsEx.class})
    private ResponseEntity<String> handleNotFound(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
