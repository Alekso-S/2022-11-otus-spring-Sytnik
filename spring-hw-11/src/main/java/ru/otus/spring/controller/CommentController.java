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
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.exception.ObjectNotFoundEx;
import ru.otus.spring.repository.ReactiveAuthorRepository;
import ru.otus.spring.repository.ReactiveBookRepository;
import ru.otus.spring.repository.ReactiveGenreRepository;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final ReactiveAuthorRepository authorRepository;
    private final ReactiveGenreRepository genreRepository;
    private final ReactiveBookRepository bookRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @GetMapping("/comments")
    public String commentsPage(Model model, @RequestParam("bookId") String bookId) {
        model.addAttribute("bookId", bookId);
        model.addAttribute("bookName", bookRepository.findById(bookId).map(Book::getName));
        addCounts(model);
        return "comments";
    }

    private void addCounts(Model model) {
        model.addAttribute("booksCount", bookRepository.count());
        model.addAttribute("authorsCount", authorRepository.count());
        model.addAttribute("genresCount", genreRepository.count());
    }

    @ExceptionHandler({CommentNotFoundEx.class, BookNotFoundEx.class})
    private ResponseEntity<String> handleNotFound(ObjectNotFoundEx ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
