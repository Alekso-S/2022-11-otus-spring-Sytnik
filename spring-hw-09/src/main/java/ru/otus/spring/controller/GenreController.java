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
public class GenreController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/genres")
    public String genresPage(Model model) {
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("booksCount", bookService.count());
        model.addAttribute("authorsCount", authorService.count());
        model.addAttribute("genresCount", genreService.count());
        return "genres";
    }
}
