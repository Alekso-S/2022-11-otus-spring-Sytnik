package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
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
        model.addAttribute("books", bookService.getAll());
        addCounts(model);
        return "books";
    }

    @GetMapping(value = "/books", params = "author")
    public String booksPageByAuthor(Model model, @RequestParam("author") String authorName) {
        model.addAttribute("books", bookService.getAllByAuthorName(authorName));
        model.addAttribute("authorName", authorName);
        addCounts(model);
        return "books";
    }

    @GetMapping(value = "/books", params = "genre")
    public String booksPageByGenre(Model model, @RequestParam("genre") String genreName) {
        model.addAttribute("books", bookService.getAllByGenreName(genreName));
        model.addAttribute("genreName", genreName);
        addCounts(model);
        return "books";
    }

    @GetMapping("/books/add")
    public String addPage(Model model) {
        addCounts(model);
        return "book-add";
    }

    @PostMapping("/books/add")
    public String add(@RequestParam("name") String name,
                      @RequestParam("author") String authorName,
                      @RequestParam("genres") String genres) throws BookAlreadyExistsEx {
        bookService.add(name, authorName, genres);
        return "redirect:/books";
    }

    @PostMapping("/books/delete")
    public ModelAndView delete(@RequestParam("name") String name) throws BookNotFoundEx {
        bookService.deleteByName(name);
        return new ModelAndView("redirect:/books");
    }

    private void addCounts(Model model) {
        model.addAttribute("booksCount", bookService.count());
        model.addAttribute("authorsCount", authorService.count());
        model.addAttribute("genresCount", genreService.count());
    }

    @ExceptionHandler({BookNotFoundEx.class, BookAlreadyExistsEx.class})
    private ResponseEntity<String> handleNotFound(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
