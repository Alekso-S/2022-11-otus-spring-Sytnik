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
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.exception.ObjectNotFoundEx;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/comments")
    public String commentsPage(Model model, @RequestParam("bookId") String bookId) throws BookNotFoundEx {
        model.addAttribute("bookId", bookId);
        model.addAttribute("bookName", bookService.getById(bookId).getName());
        addCounts(model);
        return "comments";
    }

    private void addCounts(Model model) {
        model.addAttribute("booksCount", bookService.count());
        model.addAttribute("authorsCount", authorService.count());
        model.addAttribute("genresCount", genreService.count());
    }

    @ExceptionHandler({CommentNotFoundEx.class, BookNotFoundEx.class})
    private ResponseEntity<String> handleNotFound(ObjectNotFoundEx ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
