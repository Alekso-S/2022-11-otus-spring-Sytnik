package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("booksCount", bookService.count());
        model.addAttribute("authorsCount", authorService.count());
        model.addAttribute("genresCount", genreService.count());
        return "authors";
    }
}
