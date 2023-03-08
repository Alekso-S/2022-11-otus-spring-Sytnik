package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.spring.repository.ReactiveAuthorRepository;
import ru.otus.spring.repository.ReactiveBookRepository;
import ru.otus.spring.repository.ReactiveGenreRepository;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final ReactiveAuthorRepository authorRepository;
    private final ReactiveGenreRepository genreRepository;
    private final ReactiveBookRepository bookRepository;

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        model.addAttribute("booksCount", bookRepository.count());
        model.addAttribute("authorsCount", authorRepository.count());
        model.addAttribute("genresCount", genreRepository.count());
        return "authors";
    }
}
