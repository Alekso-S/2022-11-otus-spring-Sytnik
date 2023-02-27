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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.exception.ObjectNotFoundEx;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.GenreService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/comments")
    public String commentsPage(Model model, @RequestParam("book") String bookName) throws BookNotFoundEx {
        model.addAttribute("comments", commentService.getAllByBookName(bookName));
        model.addAttribute("bookName", bookName);
        addCounts(model);
        return "comments";
    }

    @GetMapping("/comments/add")
    public String addPage(Model model, @RequestParam("book") String bookName) {
        model.addAttribute("bookName", bookName);
        addCounts(model);
        return "comment-add";
    }

    @GetMapping("/comments/edit")
    public String editPage(Model model, @RequestParam("id") String id) throws CommentNotFoundEx {
        model.addAttribute("comment", commentService.getById(id));
        addCounts(model);
        return "comment-edit";
    }

    @GetMapping("/comments/cancel")
    public String cancel(RedirectAttributes redirectAttributes,
                         @RequestParam("book") String bookName) {
        redirectAttributes.addAttribute("book", bookName);
        return "redirect:/comments?book={book}";
    }

    @PostMapping("/comments/add")
    public String add(RedirectAttributes redirectAttributes,
                      @RequestParam("book") String bookName,
                      @RequestParam("text") String text) throws BookNotFoundEx {
        commentService.addByBookName(bookName, text);
        redirectAttributes.addAttribute("book", bookName);
        return "redirect:/comments?book={book}";
    }

    @PostMapping("/comments/edit")
    public String edit(RedirectAttributes redirectAttributes,
                       @RequestParam("id") String id,
                       @RequestParam("text") String text) throws CommentNotFoundEx {
        redirectAttributes.addAttribute("book", commentService.updateById(id, text).getBookName());
        return "redirect:/comments?book={book}";
    }

    @PostMapping("/comments/delete")
    public String delete(RedirectAttributes redirectAttributes,
                         @RequestParam("id") String id) throws CommentNotFoundEx {
        redirectAttributes.addAttribute("book", commentService.deleteById(id).getBookName());
        return "redirect:/comments?book={book}";
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
